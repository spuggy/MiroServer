// Buying and downloading a candidate's report — replaces the 5-minute cron.
// Buying is a quick DB transaction; the PDF is generated when it's downloaded.

import type { Prisma } from "@prisma/client";
import { prisma } from "@/lib/prisma";
import { allocateLegacyId } from "@/lib/server/ids";
import { loadSurvey } from "@/lib/assessment/service";
import { toSurveyDefinition } from "@/lib/assessment/survey";
import {
  availableReportTypes,
  calculateScores,
  DEFAULT_THRESHOLDS,
  generateReport,
  type ReportType,
} from "@/lib/report";
import { CANDIDATE_STATUS, TRANSACTION_OK_BUY_REPORT } from "./status";

export class ReportAccessError extends Error {
  constructor(
    message: string,
    public readonly httpStatus: number,
  ) {
    super(message);
  }
}

interface CandidateRef {
  projectId: bigint;
  candidateId: bigint;
  practitionerId: bigint;
}

async function loadOwnedCandidate({ projectId, candidateId, practitionerId }: CandidateRef) {
  const candidate = await prisma.appUser.findFirst({
    where: { id: candidateId, projectId, deleted: { not: true } },
    select: {
      id: true,
      firstName: true,
      lastName: true,
      status: true,
      responseId: true,
      userType: true,
    },
  });
  const project = await prisma.miroProject.findFirst({
    where: { id: projectId, createdById: practitionerId },
    select: { id: true },
  });
  if (!candidate || !project) throw new ReportAccessError("Candidate not found", 404);
  return candidate;
}

/** Port of AjaxMiroProjectManagerImpl.buyReport + UserManagerImpl.buyReport. */
export async function buyReport(ref: CandidateRef): Promise<{ creditBalance: number }> {
  const candidate = await loadOwnedCandidate(ref);
  if (candidate.status !== CANDIDATE_STATUS.PURCHASE_REPORT || !candidate.responseId) {
    throw new ReportAccessError("This report isn't available to buy", 409);
  }

  const now = new Date();
  return prisma.$transaction(async (tx: Prisma.TransactionClient) => {
    // Guard against double clicks: only one request can move 30 → 40.
    const moved = await tx.appUser.updateMany({
      where: { id: candidate.id, status: CANDIDATE_STATUS.PURCHASE_REPORT },
      data: { status: CANDIDATE_STATUS.DOWNLOAD_REPORT, updatedAt: now },
    });
    if (moved.count !== 1) throw new ReportAccessError("This report has already been bought", 409);

    // The legacy app let the balance go negative (its zero-balance check is commented out).
    const practitioner = await tx.appUser.update({
      where: { id: ref.practitionerId },
      data: { creditBalance: { decrement: 1 }, updatedAt: now },
      select: { creditBalance: true },
    });

    await tx.miroTransaction.create({
      data: {
        id: await allocateLegacyId(tx, "mr.mirotransactions"),
        version: 1,
        userId: ref.practitionerId,
        status: TRANSACTION_OK_BUY_REPORT,
        errorCode: 0,
        paymentMethod: 0,
        credits: 1,
        checkPoint: 0n,
        createdOn: now,
        updatedAt: now,
        deleted: false,
        createdById: candidate.id,
        lastUpdatedById: candidate.id,
        transValue: 0,
        paymentStatus: "",
        paymentStatusDetail: `${candidate.firstName} ${candidate.lastName}`,
        paymentTransId: candidate.id.toString(),
      },
    });

    return { creditBalance: practitioner.creditBalance ?? 0 };
  });
}

/**
 * Generates a purchased report PDF on demand. One purchase covers every report
 * the response supports: the standard report, plus the enhanced and leadership
 * reports for V11 assessments (the legacy cron generated all of them).
 */
export async function buildCandidateReport(
  ref: CandidateRef,
  type: ReportType = "v10",
): Promise<{ pdf: Buffer; fileName: string }> {
  const candidate = await loadOwnedCandidate(ref);
  if (candidate.status !== CANDIDATE_STATUS.DOWNLOAD_REPORT || !candidate.responseId) {
    throw new ReportAccessError("This report hasn't been bought yet", 403);
  }

  const response = await prisma.response.findUnique({
    where: { id: candidate.responseId },
    select: {
      id: true,
      surveyId: true,
      answerTrail: true,
      questionTrail: true,
      createdOn: true,
      updatedAt: true,
    },
  });
  if (!response?.surveyId || !response.answerTrail || !response.questionTrail) {
    throw new ReportAccessError("The assessment response is missing", 404);
  }

  const practitioner = await prisma.appUser.findUnique({
    where: { id: ref.practitionerId },
    select: {
      firstName: true,
      lastName: true,
      email: true,
      phoneNumber: true,
      company: true,
      webaddress: true,
      address1: true,
      address2: true,
      city: true,
      county: true,
      postcode: true,
    },
  });
  if (!practitioner) throw new ReportAccessError("Practitioner not found", 404);

  const surveyId = Number(response.surveyId);
  if (!availableReportTypes(surveyId).includes(type)) {
    throw new ReportAccessError("This report needs the enhanced (V11) assessment", 409);
  }

  const survey = await loadSurvey(response.surveyId);
  const scores = calculateScores(
    toSurveyDefinition(survey),
    { surveyId, answerTrail: response.answerTrail, questionTrail: response.questionTrail },
    { testOffset: DEFAULT_THRESHOLDS.testOffset },
  );

  const report = await generateReport(type, {
    testId: Number(response.id),
    surveyId,
    candidate: { firstName: candidate.firstName, lastName: candidate.lastName },
    practitioner: {
      name: `${practitioner.firstName} ${practitioner.lastName}`,
      email: practitioner.email,
      telNo: practitioner.phoneNumber,
      company: practitioner.company,
      webAddress: practitioner.webaddress,
      address: [
        practitioner.address1,
        practitioner.address2,
        practitioner.city,
        practitioner.county,
        practitioner.postcode,
      ],
    },
    scores,
    thresholds: DEFAULT_THRESHOLDS,
    isFreeReport: candidate.userType === "FREE",
    completedOn: response.updatedAt ?? response.createdOn,
  });

  return { pdf: report.pdf!, fileName: `${report.fileName}.pdf` };
}
