// Invite links and the candidate's assessment: create/send an invite, resolve
// a token, save answers as the candidate goes, and finish (score + store the
// response in the legacy format).

import type { Prisma } from "@prisma/client";
import { prisma } from "@/lib/prisma";
import { escapeHtml, sendEmail, type EmailResult } from "@/lib/email";
import { allocateLegacyId } from "@/lib/server/ids";
import { CANDIDATE_STATUS, isEnabledFlagValue } from "@/lib/candidates/status";
import { DEFAULT_THRESHOLDS } from "@/lib/report/constants";
import { calculateScores, isInterpretableResult } from "@/lib/report/scoring";
import { createInviteToken, hashInviteToken, looksLikeInviteToken } from "./tokens";
import {
  AssessmentError,
  buildSurvey,
  buildTrails,
  isComplete,
  toSurveyDefinition,
  validateAnswer,
  type LoadedSurvey,
  type Progress,
} from "./survey";

const DEFAULT_SURVEY_ID = 5; // MiRo Survey 1.1 (V11)
const INVITE_TTL_DAYS = Number(process.env.INVITE_TTL_DAYS || 30);

const surveyCache = new Map<string, Promise<LoadedSurvey>>();

export function loadSurvey(surveyId: bigint | number): Promise<LoadedSurvey> {
  const key = surveyId.toString();
  let cached = surveyCache.get(key);
  if (!cached) {
    cached = (async () => {
      const survey = await prisma.survey.findUnique({
        where: { id: BigInt(surveyId) },
        select: { id: true, firstQuestionId: true },
      });
      if (!survey) throw new AssessmentError(`Survey ${surveyId} not found`);
      const questions = await prisma.question.findMany({
        where: { surveyId: survey.id },
        select: {
          id: true,
          shortname: true,
          qMeta: true,
          qTxt: true,
          jQuestionId: true,
          deleted: true,
          options: { select: { id: true, oText: true, deleted: true } },
        },
      });
      return buildSurvey(survey, questions);
    })();
    cached.catch(() => surveyCache.delete(key));
    surveyCache.set(key, cached);
  }
  return cached;
}

// ---------------------------------------------------------------------------
// Invites

export interface SendInviteResult {
  email: EmailResult;
  /** Only returned when email isn't configured (local dev), so the link can be opened. */
  devInviteUrl?: string;
}

export async function sendAssessmentInvite(opts: {
  projectId: bigint;
  candidateId: bigint;
  practitionerId: bigint;
  origin: string;
}): Promise<SendInviteResult> {
  const project = await prisma.miroProject.findFirst({
    where: { id: opts.projectId, createdById: opts.practitionerId },
    select: {
      projectTitle: true,
      emailInviteSubject: true,
      emailInviteText: true,
      bccPractitioner: true,
      createdBy: {
        select: { firstName: true, lastName: true, email: true, defaultSurveyId: true },
      },
    },
  });
  if (!project) throw new AssessmentError("Project not found");

  const candidate = await prisma.appUser.findFirst({
    where: { id: opts.candidateId, projectId: opts.projectId, deleted: { not: true } },
    select: { id: true, firstName: true, lastName: true, email: true, status: true },
  });
  if (!candidate) throw new AssessmentError("Candidate not found");
  if ((candidate.status ?? 0) >= CANDIDATE_STATUS.ASSESSMENT_COMPLETE) {
    throw new AssessmentError("This candidate has already completed the assessment");
  }

  const surveyId = BigInt(project.createdBy?.defaultSurveyId || DEFAULT_SURVEY_ID);
  const { token, tokenHash } = createInviteToken();
  const now = new Date();
  const expiresAt = new Date(now.getTime() + INVITE_TTL_DAYS * 24 * 60 * 60 * 1000);

  await prisma.$transaction(async (tx: Prisma.TransactionClient) => {
    // A new invite replaces any earlier link, but keeps answers given so far.
    const previous = await tx.assessmentInvite.findFirst({
      where: { candidateId: candidate.id, revokedAt: null, completedAt: null },
      orderBy: { id: "desc" },
      select: { progress: true, surveyId: true },
    });
    await tx.assessmentInvite.updateMany({
      where: { candidateId: candidate.id, revokedAt: null, completedAt: null },
      data: { revokedAt: now },
    });
    await tx.assessmentInvite.create({
      data: {
        tokenHash,
        candidateId: candidate.id,
        surveyId,
        expiresAt,
        progress: previous && previous.surveyId === surveyId ? (previous.progress ?? {}) : {},
      },
    });
    if ((candidate.status ?? 0) < CANDIDATE_STATUS.INVITE_SENT) {
      await tx.appUser.update({
        where: { id: candidate.id },
        data: { status: CANDIDATE_STATUS.INVITE_SENT, updatedAt: now },
      });
    }
  });

  const url = `${opts.origin.replace(/\/$/, "")}/assess/${token}`;
  const practitionerName = [project.createdBy?.firstName, project.createdBy?.lastName]
    .filter(Boolean)
    .join(" ");
  const subject = project.emailInviteSubject?.trim() || project.projectTitle;
  const intro =
    project.emailInviteText?.trim() || "You have been invited to complete a MiRo assessment.";
  const bcc =
    isEnabledFlagValue(project.bccPractitioner) && project.createdBy?.email
      ? [project.createdBy.email]
      : undefined;

  const email = await sendEmail({
    to: candidate.email,
    bcc,
    replyTo: project.createdBy?.email || undefined,
    subject,
    text: [
      `Hi ${candidate.firstName},`,
      "",
      intro,
      "",
      `Start your assessment: ${url}`,
      "",
      `This link is personal to you and expires on ${expiresAt.toDateString()}.`,
      practitionerName ? `\n${practitionerName}` : "",
    ].join("\n"),
    html: inviteHtml({
      firstName: candidate.firstName,
      intro,
      url,
      expires: expiresAt.toDateString(),
      practitionerName,
    }),
  });

  return { email, devInviteUrl: email.delivered ? undefined : url };
}

function inviteHtml(p: {
  firstName: string;
  intro: string;
  url: string;
  expires: string;
  practitionerName: string;
}): string {
  return `<!doctype html><html><body style="margin:0;background:#f4f4f4;font-family:Arial,Helvetica,sans-serif;color:#292929">
<table role="presentation" width="100%" cellpadding="0" cellspacing="0"><tr><td align="center" style="padding:32px 12px">
<table role="presentation" width="100%" style="max-width:560px;background:#fff;border-radius:8px" cellpadding="0" cellspacing="0">
<tr><td style="height:6px;background:linear-gradient(90deg,#d53f35 0 25%,#fbc726 25% 50%,#43add5 50% 75%,#42b449 75%);border-radius:8px 8px 0 0"></td></tr>
<tr><td style="padding:28px 32px 8px;font-size:15px;line-height:1.55">
<p style="margin:0 0 14px">Hi ${escapeHtml(p.firstName)},</p>
<p style="margin:0 0 22px">${escapeHtml(p.intro)}</p>
<p style="margin:0 0 22px"><a href="${escapeHtml(p.url)}" style="display:inline-block;background:#288eb5;color:#fff;text-decoration:none;padding:12px 22px;border-radius:6px;font-weight:bold">Start my assessment</a></p>
<p style="margin:0 0 6px;font-size:13px;color:#6b6b6b">It takes about 15 minutes. Your answers are saved as you go, so you can come back to this link.</p>
<p style="margin:0 0 22px;font-size:13px;color:#6b6b6b">This link is personal to you — please don't forward it. It expires on ${escapeHtml(p.expires)}.</p>
${p.practitionerName ? `<p style="margin:0 0 24px">${escapeHtml(p.practitionerName)}</p>` : ""}
</td></tr></table></td></tr></table></body></html>`;
}

// ---------------------------------------------------------------------------
// Token resolution (candidate side)

export type InviteState = "ok" | "invalid" | "expired" | "completed";

export async function resolveInvite(token: string) {
  if (!looksLikeInviteToken(token)) return { state: "invalid" as const };
  const invite = await prisma.assessmentInvite.findUnique({
    where: { tokenHash: hashInviteToken(token) },
    include: {
      candidate: {
        select: {
          id: true,
          firstName: true,
          lastName: true,
          projectId: true,
          status: true,
          userType: true,
          deleted: true,
        },
      },
    },
  });
  if (!invite || invite.revokedAt || invite.candidate.deleted) return { state: "invalid" as const };
  if (invite.completedAt) return { state: "completed" as const, invite };
  if (invite.expiresAt.getTime() < Date.now()) return { state: "expired" as const, invite };
  return { state: "ok" as const, invite };
}

async function requireOpenInvite(token: string) {
  const resolved = await resolveInvite(token);
  if (resolved.state !== "ok") throw new AssessmentError(`Invite is ${resolved.state}`);
  return resolved.invite;
}

export async function getPractitionerForProject(projectId: bigint | null) {
  if (!projectId) return null;
  const project = await prisma.miroProject.findUnique({
    where: { id: projectId },
    select: {
      createdBy: { select: { firstName: true, lastName: true, email: true, phoneNumber: true } },
    },
  });
  return project?.createdBy ?? null;
}

export async function saveAnswer(
  token: string,
  questionId: string,
  answer: unknown,
): Promise<{ answered: number }> {
  const invite = await requireOpenInvite(token);
  const survey = await loadSurvey(invite.surveyId);
  const question = survey.questions.find((q) => q.id === questionId);
  if (!question) throw new AssessmentError("Unknown question");
  const normalised = validateAnswer(question, answer);

  // Merge this one answer in a single atomic UPDATE. Reading the whole object
  // and writing it back lost answers when saves overlapped (quick clicking).
  const rows = await prisma.$queryRaw<{ answered: number }[]>`
    UPDATE public.assessment_invite
       SET progress = progress || jsonb_build_object(${questionId}::text, ${JSON.stringify(normalised)}::jsonb),
           last_used_at = now()
     WHERE id = ${invite.id} AND completed_at IS NULL AND revoked_at IS NULL
     RETURNING (SELECT count(*) FROM jsonb_object_keys(progress))::int AS answered`;
  if (!rows.length) throw new AssessmentError("Invite is no longer open");
  return { answered: rows[0].answered };
}

/** Thrown when the server is missing answers; lists them so the page can resend. */
export class MissingAnswersError extends AssessmentError {
  constructor(public readonly questionIds: string[]) {
    super("Please answer every question");
  }
}

export type CompleteResult = { status: "completed" } | { status: "uninterpretable" };

export async function completeAssessment(token: string): Promise<CompleteResult> {
  const invite = await requireOpenInvite(token);
  const survey = await loadSurvey(invite.surveyId);
  const progress = (invite.progress as Progress) ?? {};
  if (!isComplete(survey, progress)) {
    throw new MissingAnswersError(
      survey.questions.filter((q) => q.kind !== "info" && !progress[q.id]).map((q) => q.id),
    );
  }

  const { answerTrail, questionTrail } = buildTrails(survey, progress);
  const scores = calculateScores(
    toSurveyDefinition(survey),
    { surveyId: survey.id, answerTrail, questionTrail },
    { testOffset: DEFAULT_THRESHOLDS.testOffset },
  );

  if (!isInterpretableResult(scores.results, DEFAULT_THRESHOLDS.testOffset)) {
    // Legacy behaviour: nothing is saved and the candidate is asked to retake.
    await prisma.assessmentInvite.update({
      where: { id: invite.id },
      data: { progress: {}, lastUsedAt: new Date() },
    });
    return { status: "uninterpretable" };
  }

  const candidate = invite.candidate;
  const now = new Date();
  await prisma.$transaction(async (tx: Prisma.TransactionClient) => {
    const claimed = await tx.assessmentInvite.updateMany({
      where: { id: invite.id, completedAt: null, revokedAt: null },
      data: { completedAt: now, lastUsedAt: now },
    });
    if (claimed.count !== 1)
      throw new AssessmentError("This assessment has already been submitted");

    const responseId = await allocateLegacyId(tx, "mr.responses");
    await tx.response.create({
      data: {
        id: responseId,
        version: 0,
        answerTrail,
        questionTrail,
        deleted: false,
        checkPoint: 0n,
        createdById: candidate.id,
        lastUpdatedById: candidate.id,
        updatedAt: now,
        surveyId: BigInt(survey.id),
        total: 0,
        createdOn: now,
        photosStamped: false,
        // The old cron flipped this after making PDFs; reports are now made on demand.
        alertsProcessed: true,
      },
    });

    // Legacy cron: FREE candidates went straight to "download", everyone else
    // to "ready to buy" (saveAsPurchased).
    const isFree = candidate.userType === "FREE";
    await tx.appUser.update({
      where: { id: candidate.id },
      data: {
        responseId,
        status: isFree ? CANDIDATE_STATUS.DOWNLOAD_REPORT : CANDIDATE_STATUS.PURCHASE_REPORT,
        updatedAt: now,
      },
    });
  });

  return { status: "completed" };
}
