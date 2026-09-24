// Loads a MiRo survey (mr.surveys / mr.questions / mr.options) as the ordered
// question chain the candidate answers, and turns saved answers into the
// legacy answer_trail / question_trail strings the scoring code expects.

import type { SurveyDefinition } from "@/lib/report/scoring";

export type QuestionKind = "miro" | "info" | "truefalse";

export interface SurveyOption {
  id: string;
  /** Legacy option text, e.g. "Charming#E" or "true#plus". Server side only. */
  oText: string;
  /** What the candidate sees, e.g. "Charming" or "True". */
  label: string;
}

export interface SurveyQuestion {
  id: string;
  kind: QuestionKind;
  text: string;
  shortname: string;
  qMeta: string;
  jQuestionId: number;
  options: SurveyOption[];
}

export interface LoadedSurvey {
  id: number;
  firstQuestionId: number;
  questions: SurveyQuestion[];
}

/** What the browser receives: no option texts, so no mode letters. */
export interface PublicQuestion {
  id: string;
  kind: QuestionKind;
  text: string;
  options: { id: string; label: string }[];
}

export type MiroAnswer = { most: string; least: string };
export type TrueFalseAnswer = { choice: string };
export type Answer = MiroAnswer | TrueFalseAnswer;
/** question id → answer (option ids). */
export type Progress = Record<string, Answer>;

export class AssessmentError extends Error {}

interface RawQuestion {
  id: bigint;
  shortname: string | null;
  qMeta: string | null;
  qTxt: string | null;
  jQuestionId: bigint | null;
  deleted: boolean | null;
  options: { id: bigint; oText: string | null; deleted: boolean | null }[];
}

export function questionKind(qMeta: string): QuestionKind {
  const meta = qMeta.trim().toLowerCase();
  if (meta === "q11inst") return "info";
  if (meta === "q11") return "truefalse";
  return "miro";
}

function optionLabel(oText: string, kind: QuestionKind): string {
  const word = oText.split("#")[0].trim();
  if (kind === "truefalse") return word.toLowerCase() === "true" ? "True" : "False";
  return word;
}

/** Follows firstQuestionId → jQuestionId → … the same way the legacy UI and scorer do. */
export function buildSurvey(
  survey: { id: bigint | number; firstQuestionId: bigint | number | null },
  rawQuestions: RawQuestion[],
): LoadedSurvey {
  const byId = new Map(rawQuestions.filter((q) => !q.deleted).map((q) => [q.id.toString(), q]));
  const questions: SurveyQuestion[] = [];
  const seen = new Set<string>();
  let next = survey.firstQuestionId ? survey.firstQuestionId.toString() : null;

  while (next && next !== "0" && byId.has(next) && !seen.has(next)) {
    seen.add(next);
    const q = byId.get(next)!;
    const qMeta = q.qMeta ?? "";
    const kind = questionKind(qMeta);
    const options = q.options
      .filter((o) => !o.deleted && o.oText)
      .sort((a, b) => (a.id < b.id ? -1 : 1))
      .map((o) => ({ id: o.id.toString(), oText: o.oText!, label: optionLabel(o.oText!, kind) }));
    questions.push({
      id: q.id.toString(),
      kind,
      text: kind === "miro" ? "" : (q.qTxt ?? "").trim(),
      shortname: q.shortname ?? "",
      qMeta,
      jQuestionId: Number(q.jQuestionId ?? 0),
      options,
    });
    next = q.jQuestionId ? q.jQuestionId.toString() : null;
  }

  if (!questions.length) throw new AssessmentError(`Survey ${survey.id} has no questions`);
  return { id: Number(survey.id), firstQuestionId: Number(survey.firstQuestionId), questions };
}

export function toPublicQuestions(survey: LoadedSurvey): PublicQuestion[] {
  return survey.questions.map((q) => ({
    id: q.id,
    kind: q.kind,
    text: q.text,
    options: q.options.map(({ id, label }) => ({ id, label })),
  }));
}

export function toSurveyDefinition(survey: LoadedSurvey): SurveyDefinition {
  return {
    id: survey.id,
    firstQuestionId: survey.firstQuestionId,
    questions: survey.questions.map((q) => ({
      id: Number(q.id),
      shortname: q.shortname,
      qMeta: q.qMeta,
      jQuestionId: q.jQuestionId,
    })),
  };
}

/** Validates one answer against its question; returns a normalised copy. */
export function validateAnswer(question: SurveyQuestion, answer: unknown): Answer {
  const optionIds = new Set(question.options.map((o) => o.id));
  const a = (answer ?? {}) as Record<string, unknown>;
  if (question.kind === "miro") {
    const most = String(a.most ?? "");
    const least = String(a.least ?? "");
    if (!optionIds.has(most) || !optionIds.has(least)) {
      throw new AssessmentError("Choose one word for most and one for least");
    }
    if (most === least) throw new AssessmentError("Most and least must be different words");
    return { most, least };
  }
  if (question.kind === "truefalse") {
    const choice = String(a.choice ?? "");
    if (!optionIds.has(choice)) throw new AssessmentError("Choose true or false");
    return { choice };
  }
  throw new AssessmentError("This question does not take an answer");
}

export function isComplete(survey: LoadedSurvey, progress: Progress): boolean {
  return survey.questions.every((q) => q.kind === "info" || Boolean(progress[q.id]));
}

/** Legacy formats: "Most#L;Least#L", "null;true#plus", and "null;N/A" for info pages. */
export function buildTrails(
  survey: LoadedSurvey,
  progress: Progress,
): { answerTrail: string; questionTrail: string } {
  const answers = survey.questions.map((q) => {
    if (q.kind === "info") return "null;N/A";
    const answer = progress[q.id];
    if (!answer) throw new AssessmentError(`Question ${q.id} has not been answered`);
    const text = (optionId: string) => {
      const option = q.options.find((o) => o.id === optionId);
      if (!option) throw new AssessmentError(`Unknown option ${optionId}`);
      return option.oText;
    };
    if (q.kind === "miro") {
      const { most, least } = answer as MiroAnswer;
      return `${text(most)};${text(least)}`;
    }
    return `null;${text((answer as TrueFalseAnswer).choice)}`;
  });
  return {
    answerTrail: answers.join("~"),
    questionTrail: survey.questions.map((q) => q.id).join("~"),
  };
}
