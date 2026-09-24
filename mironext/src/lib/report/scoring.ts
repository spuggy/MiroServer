// Port of MiroResponse.forceCalculateResults and its helpers.
//
// A response is a chain of questions (first question id → jQuestionId → ...).
// Each answer is "Word#L;Word#L" (most-like ; least-like). Tie-breaker
// questions ("tie" meta) score double and also record which letter wins a tie.

import {
  DEFAULT_MIRO_LETTERS,
  JAVA_HASHMAP_LETTER_ORDER,
  MBTI_MAP,
  SURVEY_ID_MIRO_V11,
  type MiroLetter,
} from "./constants";

export interface SurveyQuestion {
  id: number;
  shortname: string;
  /** "Q", "tie", "Q11Inst", ... */
  qMeta: string;
  /** Next question in the chain; 0 or a missing id ends it. */
  jQuestionId: number;
}

export interface SurveyDefinition {
  id: number;
  firstQuestionId: number;
  questions: SurveyQuestion[];
}

export interface SurveyResponseInput {
  surveyId: number;
  /** "~"-separated answers, parallel to questionTrail. */
  answerTrail: string;
  /** "~"-separated question ids. */
  questionTrail: string;
}

export interface MiroScores {
  /** Scores in descending (result) order. */
  results: number[];
  /** Letters in result order, e.g. ["O", "E", "D", "A"]. */
  resultLetters: MiroLetter[];
  extroValue: number;
  intraValue: number;
  /** V11 only: e.g. "MEX". */
  extroIntroStrata: string | null;
}

export class MiroScoringError extends Error {}

export function buildAnswerMap(response: SurveyResponseInput): Map<string, string> {
  const questions = response.questionTrail.split("~");
  const answers = response.answerTrail.split("~");
  const map = new Map<string, string>();
  answers.forEach((answer, i) => map.set(questions[i], answer));
  return map;
}

function parseAnswerLetters(raw: string): { most: string; least: string } {
  const [mostPart, leastPart] = raw.split(";");
  if (mostPart === undefined || leastPart === undefined) {
    throw new MiroScoringError(`Malformed answer "${raw}"`);
  }
  return {
    most: mostPart.split("#")[1].trim(),
    least: leastPart.split("#")[1].trim(),
  };
}

export function calculateScores(
  survey: SurveyDefinition,
  response: SurveyResponseInput,
  options: { testOffset: number; letters?: MiroLetter[] },
): MiroScores {
  if (survey.id !== response.surveyId) {
    throw new MiroScoringError("survey id does not match response survey id");
  }

  const letters = options.letters ?? DEFAULT_MIRO_LETTERS;
  const questionMap = new Map(survey.questions.map((q) => [q.id, q]));
  const answers = buildAnswerMap(response);

  const most = new Map<string, number>(letters.map((l) => [l, 0]));
  const least = new Map<string, number>(letters.map((l) => [l, 0]));
  const tieBreakers = new Map<string, string>();

  let question = questionMap.get(survey.firstQuestionId);

  while (question && question.qMeta.toLowerCase() !== "q11inst") {
    const raw = answers.get(String(question.id)) ?? "n/a";
    const { most: mostLetter, least: leastLetter } = parseAnswerLetters(raw);
    const isTieBreaker = question.qMeta.trim().toLowerCase() === "tie";
    if (isTieBreaker) {
      tieBreakers.set(question.shortname, mostLetter);
    }
    const weight = isTieBreaker ? 2 : 1;
    most.set(mostLetter, (most.get(mostLetter) ?? 0) + weight);
    least.set(leastLetter, (least.get(leastLetter) ?? 0) + weight);

    question = questionMap.get(question.jQuestionId);
  }

  const scores = new Map<MiroLetter, number>();
  for (const letter of letters) {
    scores.set(letter, most.get(letter)! - least.get(letter)! + options.testOffset);
  }

  const worker = new Map(scores);
  let loopBreaker = 0;
  while (removeEqualScores(worker, tieBreakers) && loopBreaker < 100) {
    loopBreaker++;
  }
  if (loopBreaker >= 100) {
    throw new MiroScoringError("Loopbreaker over 100!!!");
  }

  const { results, resultLetters } = orderResults(scores, worker);

  let extroValue = 0;
  let intraValue = 0;
  let extroIntroStrata: string | null = null;

  if (response.surveyId >= SURVEY_ID_MIRO_V11) {
    // Skip the interstitial page, then count plus/minus answers.
    if (question) question = questionMap.get(question.jQuestionId);
    while (question) {
      const raw = answers.get(String(question.id)) ?? "n/a";
      const answer = raw.split("#")[1];
      if (answer?.toLowerCase() === "plus") extroValue++;
      else intraValue++;
      question = questionMap.get(question.jQuestionId);
    }
    extroIntroStrata = getExtroIntroStrata(extroValue, intraValue);
  }

  return { results, resultLetters, extroValue, intraValue, extroIntroStrata };
}

/** Breaks one tie (if any) using the tie-breaker answers. Returns true if one was found. */
function removeEqualScores(
  worker: Map<MiroLetter, number>,
  tieBreakers: Map<string, string>,
): boolean {
  const order = JAVA_HASHMAP_LETTER_ORDER.filter((l) => worker.has(l));
  for (const letter of order) {
    for (const key of order) {
      if (key !== letter && worker.get(letter) === worker.get(key)) {
        const winner = tieBreakers.get(`${key}-${letter}`) ?? tieBreakers.get(`${letter}-${key}`);
        if (winner !== undefined) {
          const totalKey = winner === key ? key : letter;
          worker.set(totalKey, worker.get(totalKey)! + 1);
        }
        // Mirrors the Java: a tie is reported even when no tie-breaker exists.
        return true;
      }
    }
  }
  return false;
}

function orderResults(
  scores: Map<MiroLetter, number>,
  worker: Map<MiroLetter, number>,
): { results: number[]; resultLetters: MiroLetter[] } {
  const remaining = new Map(worker);
  const order = JAVA_HASHMAP_LETTER_ORDER.filter((l) => remaining.has(l));
  const results: number[] = [];
  const resultLetters: MiroLetter[] = [];

  for (let i = 0; i < 4; i++) {
    let maxValue = 0;
    let maxLetter: MiroLetter | null = null;
    for (const letter of order) {
      const v = remaining.get(letter)!;
      if (v > maxValue) {
        maxLetter = letter;
        maxValue = v;
      }
    }
    if (!maxLetter) throw new MiroScoringError("Could not order results");
    results.push(scores.get(maxLetter)!);
    resultLetters.push(maxLetter);
    remaining.set(maxLetter, -1000);
  }
  return { results, resultLetters };
}

export function getExtroIntroStrata(extroValue: number, intraValue: number): string {
  const suffix = extroValue > intraValue ? "EX" : "IN";
  const attitude = extroValue > intraValue ? extroValue : intraValue;
  if (attitude >= 9 && attitude <= 12) return `L${suffix}`;
  if (attitude >= 13 && attitude <= 16) return `M${suffix}`;
  if (attitude >= 17 && attitude <= 19) return `H${suffix}`;
  throw new MiroScoringError(`Could not find ExtraInto for value ${extroValue}, ${intraValue}`);
}

/**
 * Port of MiroResponse.isValid: a result can't be interpreted when all four
 * scores sit within ±2 of the offset, or all are at/above it.
 */
export function isInterpretableResult(results: number[], testOffset: number): boolean {
  const inRange = results.filter((r) => r >= testOffset - 2 && r <= testOffset + 2).length;
  const overOffset = results.filter((r) => r >= testOffset).length;
  return !(inRange === 4 || overOffset === 4);
}

export function getMbtiType(scores: MiroScores): string | undefined {
  const suffix = scores.extroValue > scores.intraValue ? "EX" : "IN";
  return MBTI_MAP[scores.resultLetters.join("") + suffix];
}
