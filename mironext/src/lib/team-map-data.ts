// Loads team map members for a set of projects (port of
// UserManager.getUsersByProjects + MiroResponseManager.getTeamMap).

import { prisma } from "@/lib/prisma";
import { loadSurvey } from "@/lib/assessment/service";
import { toSurveyDefinition } from "@/lib/assessment/survey";
import { calculateScores, DEFAULT_THRESHOLDS, isAttached } from "@/lib/report";
import type { SurveyDefinition } from "@/lib/report";
import { CANDIDATE_STATUS } from "@/lib/candidates/status";
import { uniqueInitials } from "@/lib/team-map";

interface CandidateRow {
  id: bigint;
  firstName: string | null;
  lastName: string | null;
  responseId: bigint | null;
}

interface ResponseRow {
  id: bigint;
  surveyId: bigint | null;
  answerTrail: string | null;
  questionTrail: string | null;
}

export interface TeamMapMember {
  initials: string;
  fullName: string;
  leadingMode: string;
  secondaryMode: string | null;
}

/** Candidates whose report has been bought, scored for the team map. */
export async function loadTeamMapMembers(projectIds: bigint[]): Promise<TeamMapMember[]> {
  const candidates: CandidateRow[] = await prisma.appUser.findMany({
    where: {
      projectId: { in: projectIds },
      deleted: { not: true },
      oldreport: { not: true },
      status: CANDIDATE_STATUS.DOWNLOAD_REPORT,
      responseId: { not: null },
    },
    orderBy: [{ lastName: "asc" }, { firstName: "asc" }, { id: "asc" }],
    select: { id: true, firstName: true, lastName: true, responseId: true },
  });
  if (candidates.length === 0) return [];

  const responses: ResponseRow[] = await prisma.response.findMany({
    where: { id: { in: candidates.map((c) => c.responseId!) } },
    select: { id: true, surveyId: true, answerTrail: true, questionTrail: true },
  });
  const responsesById = new Map(responses.map((r) => [r.id.toString(), r]));

  const surveys = new Map<string, Promise<SurveyDefinition>>();
  function surveyDefinition(surveyId: bigint) {
    const key = surveyId.toString();
    if (!surveys.has(key)) surveys.set(key, loadSurvey(surveyId).then(toSurveyDefinition));
    return surveys.get(key)!;
  }

  const usedInitials = new Set<string>();
  const members: TeamMapMember[] = [];

  for (const candidate of candidates) {
    const response = responsesById.get(candidate.responseId!.toString());
    if (!response?.surveyId || !response.answerTrail || !response.questionTrail) continue;

    try {
      const scores = calculateScores(
        await surveyDefinition(response.surveyId),
        {
          surveyId: Number(response.surveyId),
          answerTrail: response.answerTrail,
          questionTrail: response.questionTrail,
        },
        { testOffset: DEFAULT_THRESHOLDS.testOffset },
      );
      members.push({
        initials: uniqueInitials(candidate.firstName, candidate.lastName, usedInitials),
        fullName: `${candidate.firstName ?? ""} ${candidate.lastName ?? ""}`.trim(),
        leadingMode: scores.resultLetters[0],
        secondaryMode: isAttached(scores.results[1], DEFAULT_THRESHOLDS)
          ? scores.resultLetters[1]
          : null,
      });
    } catch (error) {
      // As in the legacy app, one unscorable response shouldn't sink the map.
      console.error(`Unable to score candidate ${candidate.id} for the team map`, error);
    }
  }

  return members;
}
