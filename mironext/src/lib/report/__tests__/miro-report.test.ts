// TypeScript port of MiroReport10Test (and MiroReport11Test#testGenerateFreeReport).
//
// The PDF tests launch headless Chromium via Playwright. They write the output
// to test-results/report/ so it can be inspected by eye.

import { mkdirSync, writeFileSync } from "node:fs";
import path from "node:path";
import { PDFDocument } from "pdf-lib";
import { afterAll, describe, expect, it } from "vitest";
import {
  calculateScores,
  closePdfRenderer,
  DEFAULT_THRESHOLDS,
  generateReportV10,
  generateReportV11,
  getMbtiType,
  getReportPageList,
  getPractitionerLines,
  getReportFileName,
  MiroReportError,
  SURVEY_ID_MIRO_V10,
  type ReportInput,
} from "../index";
import { renderPieSvg, computePieAngles } from "../pie-chart";
import {
  kennyPractitioner,
  rogerTestResponse,
  rogerTestV11Response,
  v10Survey,
  v11Survey,
} from "./fixtures";

const OUT_DIR = path.join(process.cwd(), "test-results", "report");
const thresholds = DEFAULT_THRESHOLDS; // engaged 31, excess 55, latent 8, offset 32

const candidate = { firstName: "Roger", lastName: "Test" };

function v10Input(isFreeReport = false): ReportInput {
  return {
    testId: 1,
    surveyId: v10Survey.id,
    candidate,
    practitioner: kennyPractitioner,
    scores: calculateScores(v10Survey, rogerTestResponse, { testOffset: thresholds.testOffset }),
    thresholds,
    isFreeReport,
  };
}

function v11Input(isFreeReport = false): ReportInput {
  return {
    testId: 1,
    surveyId: v11Survey.id,
    candidate,
    practitioner: kennyPractitioner,
    scores: calculateScores(v11Survey, rogerTestV11Response, {
      testOffset: thresholds.testOffset,
    }),
    thresholds,
    isFreeReport,
  };
}

async function countPdfPages(pdf: Buffer): Promise<number> {
  return (await PDFDocument.load(pdf)).getPageCount();
}

function save(name: string, data: Buffer | string) {
  mkdirSync(OUT_DIR, { recursive: true });
  writeFileSync(path.join(OUT_DIR, name), data);
}

afterAll(async () => {
  await closePdfRenderer();
});

describe("scoring (MiroResponse)", () => {
  it("scores the MiroReport10Test response", () => {
    const scores = calculateScores(v10Survey, rogerTestResponse, { testOffset: 32 });
    expect(scores.resultLetters).toEqual(["E", "O", "A", "D"]);
    expect(scores.results).toEqual([48, 41, 25, 14]);
    expect(getMbtiType(scores)).toBe("INFP");
  });

  it("scores the MiroReport11Test response, including extro/intro", () => {
    const scores = calculateScores(v11Survey, rogerTestV11Response, { testOffset: 32 });
    // Matches the reference Roger_Test_1.pdf: Organising / Energising / Driving / Analysing.
    expect(scores.resultLetters).toEqual(["O", "E", "D", "A"]);
    expect(scores.intraValue).toBeGreaterThan(scores.extroValue);
    expect(getMbtiType(scores)).toBe("ISFP");
  });

  it("rejects a response for a different survey", () => {
    expect(() => calculateScores(v11Survey, rogerTestResponse, { testOffset: 32 })).toThrow(
      /does not match/,
    );
  });

  it("breaks tied scores with the tie-breaker question answers", () => {
    const survey = {
      id: 4,
      firstQuestionId: 1,
      questions: [
        { id: 1, shortname: "1", qMeta: "Q", jQuestionId: 2 },
        { id: 2, shortname: "2", qMeta: "Q", jQuestionId: 3 },
        { id: 3, shortname: "D-E", qMeta: "tie", jQuestionId: 0 },
      ],
    };
    // E +1 A -1, E +1 O -1, then the D-E tie-breaker (double weight): D +2 A -2.
    // → D 34, E 34, O 31, A 29; the tie-breaker answer (D) decides the order.
    const response = {
      surveyId: 4,
      questionTrail: "1~2~3",
      answerTrail: "a#E;b#A~c#E;d#O~e#D;f#A",
    };
    const scores = calculateScores(survey, response, { testOffset: 32 });
    expect(scores.resultLetters).toEqual(["D", "E", "O", "A"]);
    expect(scores.results).toEqual([34, 34, 31, 29]);
  });
});

describe("page selection (MiroResponse.getReportPageList)", () => {
  it("picks sections for an E/O engaged profile", () => {
    const pages = getReportPageList(v10Input().scores, thresholds, SURVEY_ID_MIRO_V10, false);
    expect(pages).toEqual([
      ["homepage"],
      ["TOC"],
      ["U2"],
      ["U2a"],
      ["U3"],
      ["U4"],
      ["E1"],
      ["O2"],
      ["E-O"],
      ["A5", "D6"],
      ["E-O7"],
      ["U5"],
      ["U6"],
      ["U7"],
    ]);
  });

  it("uses FREEU7 as the last page for free reports", () => {
    const pages = getReportPageList(v11Input().scores, thresholds, SURVEY_ID_MIRO_V10, true);
    expect(pages.at(-1)).toEqual(["FREEU7"]);
    expect(pages.slice(6, 11)).toEqual([["O1"], ["E2"], ["O-E"], ["D5", "A6"], ["O-E7"]]);
  });
});

describe("helpers", () => {
  it("builds practitioner lines like addPractitionerVariables", () => {
    expect(getPractitionerLines(kennyPractitioner)).toEqual({
      v1: "Kenny Practitioner",
      v2: "80 Sandringham Road",
      v3: " ",
      v4: " ",
      v5: "Swindon",
      v6: " ",
      v7: " ",
      v8: " ",
      v9: "Tel: 07961236235",
      v10: "Email: rspence@pac.com",
    });
  });

  it("strips punctuation from report file names", () => {
    expect(getReportFileName({ firstName: "Mary-Jane", lastName: "O'Neil" }, 7)).toBe(
      "MaryJane_ONeil_7",
    );
  });

  it("centres the leading slice at the bottom and runs clockwise", () => {
    const [first, second] = computePieAngles([40, 30, 20, 10]);
    expect(first.mid).toBeCloseTo(270);
    expect(second.mid).toBeLessThan(first.mid);
    expect(renderPieSvg([{ value: 1, colour: "#000", exploded: false }])).toContain("<circle");
  });
});

describe("MiroReport10Test", () => {
  it("testGenerate: produces a 14 page PDF", async () => {
    const report = await generateReportV10(v10Input(), { year: 2026 });
    expect(report.fileName).toBe("Roger_Test_1");
    save(`${report.fileName}.pdf`, report.pdf!);
    save(`${report.fileName}.html`, report.html);
    save(`${report.fileName}.xhtml`, report.xhtml);

    expect(report.pageCount).toBe(14);
    expect(await countPdfPages(report.pdf!)).toBe(14);
  }, 120_000);

  it("testGenerateUnsupportedVersion: V10 data cannot make a V11 report", async () => {
    await expect(generateReportV11(v10Input())).rejects.toBeInstanceOf(MiroReportError);
    await expect(generateReportV11(v10Input())).rejects.toThrow(/does not support version v11/);
  });
});

describe("MiroReport11Test#testGenerateFreeReport (V10 part)", () => {
  it("produces the 14 page free report matching web/miro/out/Roger_Test_1.pdf", async () => {
    const report = await generateReportV10(v11Input(true), { year: 2026 });
    save(`${report.fileName}_free.pdf`, report.pdf!);
    save(`${report.fileName}_free.html`, report.html);

    expect(report.pageCount).toBe(14);
    expect(await countPdfPages(report.pdf!)).toBe(14);
    expect(report.html).toContain("Organising Mode");
    expect(report.html).toContain(">ISFP<");
    expect(report.html).toContain("Thanks for trying MiRo");
  }, 120_000);
});
