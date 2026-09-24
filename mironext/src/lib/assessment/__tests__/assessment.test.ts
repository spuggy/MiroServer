import { describe, expect, it } from "vitest";
import { calculateScores, isInterpretableResult } from "@/lib/report/scoring";
import { rogerTestV11Response, v11Questions } from "@/lib/report/__tests__/fixtures";
import {
  AssessmentError,
  buildSurvey,
  buildTrails,
  isComplete,
  toPublicQuestions,
  toSurveyDefinition,
  validateAnswer,
  type Progress,
} from "../survey";
import { createInviteToken, hashInviteToken, looksLikeInviteToken } from "../tokens";

// Rebuild the MiroReport11Test survey as DB rows: each question gets options
// containing the words its fixture answer used, plus fillers.
const fixtureAnswers = new Map(
  rogerTestV11Response.questionTrail
    .split("~")
    .map((qid, i) => [qid, rogerTestV11Response.answerTrail.split("~")[i]] as const),
);

let optionId = 1000;
const rawQuestions = v11Questions.map((q) => {
  const answer = fixtureAnswers.get(String(q.id)) ?? "";
  const kind = q.qMeta.toLowerCase();
  let texts: string[] = [];
  if (kind === "q" || kind === "tie") {
    const [most, least] = answer.split(";");
    texts = [most, "Filler#D", least, "Other#A"];
  } else if (kind === "q11") {
    const chosen = answer.split(";")[1];
    texts = [chosen, chosen.startsWith("true") ? "false#minus" : "true#plus"];
  }
  return {
    id: BigInt(q.id),
    shortname: q.shortname,
    qMeta: q.qMeta,
    qTxt: kind === "q" || kind === "tie" ? "tbc" : `Statement ${q.id}`,
    jQuestionId: BigInt(q.jQuestionId),
    deleted: false,
    options: texts.map((oText) => ({ id: BigInt(optionId++), oText, deleted: false })),
  };
});

const survey = buildSurvey({ id: 5, firstQuestionId: 33 }, rawQuestions);

function fixtureProgress(): Progress {
  const progress: Progress = {};
  for (const q of survey.questions) {
    if (q.kind === "miro") progress[q.id] = { most: q.options[0].id, least: q.options[2].id };
    if (q.kind === "truefalse") progress[q.id] = { choice: q.options[0].id };
  }
  return progress;
}

describe("survey model", () => {
  it("follows the question chain and classifies question kinds", () => {
    expect(survey.questions).toHaveLength(50);
    expect(survey.questions.filter((q) => q.kind === "miro")).toHaveLength(30);
    expect(survey.questions.filter((q) => q.kind === "info")).toHaveLength(1);
    expect(survey.questions.filter((q) => q.kind === "truefalse")).toHaveLength(19);
  });

  it("never sends mode letters to the browser", () => {
    const json = JSON.stringify(toPublicQuestions(survey));
    expect(json).not.toMatch(/#[DEAO]\b/);
    expect(json).not.toContain("#plus");
    expect(toPublicQuestions(survey)[0].options[0].label).toBe("Tolerant");
  });

  it("rebuilds the legacy answer trail and scores it the same as the Java test", () => {
    const progress = fixtureProgress();
    expect(isComplete(survey, progress)).toBe(true);
    const { answerTrail, questionTrail } = buildTrails(survey, progress);

    // Byte-for-byte the legacy string (the fixture's trail has one extra empty entry).
    expect(answerTrail).toBe(rogerTestV11Response.answerTrail.split("~").slice(0, 50).join("~"));
    expect(questionTrail.split("~")[0]).toBe("33");

    const scores = calculateScores(
      toSurveyDefinition(survey),
      { surveyId: 5, answerTrail, questionTrail },
      { testOffset: 32 },
    );
    expect(scores.resultLetters).toEqual(["O", "E", "D", "A"]);
    expect(isInterpretableResult(scores.results, 32)).toBe(true);
  });

  it("validates answers", () => {
    const miro = survey.questions[0];
    const [a, b] = miro.options;
    expect(validateAnswer(miro, { most: a.id, least: b.id })).toEqual({ most: a.id, least: b.id });
    expect(() => validateAnswer(miro, { most: a.id, least: a.id })).toThrow(AssessmentError);
    expect(() => validateAnswer(miro, { most: "999", least: b.id })).toThrow(AssessmentError);
    const tf = survey.questions.find((q) => q.kind === "truefalse")!;
    expect(() => validateAnswer(tf, { choice: miro.options[0].id })).toThrow(AssessmentError);
  });

  it("detects unfinished assessments", () => {
    const progress = fixtureProgress();
    delete progress[survey.questions[5].id];
    expect(isComplete(survey, progress)).toBe(false);
    expect(() => buildTrails(survey, progress)).toThrow(AssessmentError);
  });
});

describe("isInterpretableResult (MiroResponse.isValid)", () => {
  it("rejects flat profiles", () => {
    expect(isInterpretableResult([33, 32, 31, 30], 32)).toBe(false); // all within ±2
    expect(isInterpretableResult([50, 40, 36, 32], 32)).toBe(false); // all at/above offset
    expect(isInterpretableResult([48, 41, 25, 14], 32)).toBe(true);
  });
});

describe("invite tokens", () => {
  it("creates unguessable tokens and stores only a hash", () => {
    const { token, tokenHash } = createInviteToken();
    expect(looksLikeInviteToken(token)).toBe(true);
    expect(tokenHash).toMatch(/^[0-9a-f]{64}$/);
    expect(hashInviteToken(token)).toBe(tokenHash);
    expect(createInviteToken().token).not.toBe(token);
    expect(looksLikeInviteToken("../../etc")).toBe(false);
  });
});
