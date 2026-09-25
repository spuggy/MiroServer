// TypeScript port of MiroReport10Test, MiroReport11Test and MiroReportLeadership01Test.
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
  availableReportTypes,
  generateReport,
  generateReportLeadership,
  generateReportV10,
  generateReportV11,
  getExtroIntroSectionId,
  getJungianLegend,
  getMbtiType,
  getPopulationChartValues,
  MBTI_TYPES,
  getReportPageList,
  getPractitionerLines,
  getReportFileName,
  MiroReportError,
  SURVEY_ID_MIRO_V10,
  SURVEY_ID_MIRO_V11,
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

describe("V11 page selection and extras", () => {
  const scores = () => v11Input().scores; // O E D A, introvert

  it("adds the pivot-function page and the working-styles pages", () => {
    expect(getExtroIntroSectionId(scores())).toMatch(/^F[LMH]IN$/);
    const pages = getReportPageList(scores(), thresholds, SURVEY_ID_MIRO_V11, false);
    expect(pages).toHaveLength(17);
    expect(pages[7]).toEqual([getExtroIntroSectionId(scores())]);
    expect(pages.slice(-5)).toEqual([["U5"], ["barChartPage1"], ["barChartPage2"], ["U6"], ["U7"]]);
  });

  it("lists the ISFP Jungian function stack, pivot annotated with its strength", () => {
    const legend = getJungianLegend("ISFP", scores());
    expect(legend.map((l) => l.image)).toEqual(["Fi.png", "Se.png", "Ne.png", "Ti.png"]);
    expect(legend[0].text).toBe("Pivot Point (Dominant Function)");
    expect(legend[0].subText).toMatch(
      /^Introverted Feeling \((Slightly|Moderately|Strongly) expressed\)$/,
    );
    expect(legend[1].subText).toBe("Extroverted Sensing");
  });

  it("computes the working-style bar values like addMiroPopulationChartValues", () => {
    const s = scores();
    const vars = getPopulationChartValues(s);
    const letter = (l: string) => Math.trunc(s.results[s.resultLetters.indexOf(l as never)] * 1.15);
    const pct = (v: number) => Math.trunc((v / 66) * 100);
    expect(vars.leadershipBar_1lv).toBe(String(pct(letter("D"))));
    expect(vars.leadershipBar_1rv).toBe(String(pct(letter("O"))));
    expect(vars.leadershipBar_3lv).toBe(String(Math.trunc((s.intraValue / 20) * 100)));
    expect(vars.manChangeBar_3lv).toBe(vars.leadershipBar_3rv);
    expect(Object.keys(vars)).toHaveLength(24);
  });

  it("offers the enhanced and leadership reports only for V11 responses", () => {
    expect(availableReportTypes(SURVEY_ID_MIRO_V10)).toEqual(["v10"]);
    expect(availableReportTypes(SURVEY_ID_MIRO_V11)).toEqual(["v10", "v11", "leadership"]);
  });
});

describe("MiroReport11Test", () => {
  it("testGenerate: V10 is 14 pages and V11 is 17 pages", async () => {
    const v10 = await generateReportV10(v11Input(), { year: 2026 });
    expect(v10.pageCount).toBe(14);

    const report = await generateReportV11(v11Input(), { year: 2026 });
    expect(report.fileName).toBe("Roger_Test_1_v11");
    save(`${report.fileName}.pdf`, report.pdf!);
    save(`${report.fileName}.html`, report.html);

    expect(report.pageCount).toBe(17);
    expect(await countPdfPages(report.pdf!)).toBe(17);
    expect(report.html).toContain("YOUR MIRO ENHANCED REPORT");
    expect(report.html).toContain("Working Styles");
    expect(report.html).toContain("Jungian functions");
    expect(report.html).toContain('class="bar-chart"');
  }, 120_000);

  it("testGenerateFreeReport: the free V11 report is also 17 pages", async () => {
    const report = await generateReportV11(v11Input(true), { year: 2026 });
    save(`${report.fileName}_free.pdf`, report.pdf!);
    expect(report.pageCount).toBe(17);
    expect(report.html).toContain("Thanks for trying MiRo");
  }, 120_000);
});

describe("MiroReportLeadership01Test", () => {
  it("testGenerateLeadership01: produces a 6 page PDF", async () => {
    const report = await generateReport("leadership", v11Input(), { year: 2026 });
    expect(report.fileName).toBe("Roger_Test_1_lship");
    save(`${report.fileName}.pdf`, report.pdf!);
    save(`${report.fileName}.html`, report.html);

    expect(report.pages).toEqual([
      ["homepage"],
      ["ISFP_1"],
      ["ISFP_2"],
      ["ISFP_3"],
      ["ISFP_4"],
      ["ISFP_5"],
    ]);
    expect(report.pageCount).toBe(6);
    expect(await countPdfPages(report.pdf!)).toBe(6);
    expect(report.html).toContain("LEADERSHIP STYLE REPORT");
    expect(report.html).not.toContain(`<ol class="toc-list">`);
  }, 120_000);

  it("testGenerateAllPermsLeadership: every MBTI variant is 6 pages", async () => {
    const counts: Record<string, number> = {};
    for (const mbtiType of MBTI_TYPES) {
      const report = await generateReportLeadership(v11Input(), { year: 2026, mbtiType });
      save(`Roger_${mbtiType}_Test_1_lship.pdf`, report.pdf!);
      counts[mbtiType] = report.pageCount!;
    }
    expect(counts).toEqual(Object.fromEntries(MBTI_TYPES.map((t) => [t, 6])));
  }, 300_000);

  it("rejects V10 responses", async () => {
    await expect(generateReportLeadership(v10Input())).rejects.toThrow(
      /does not support version v11/,
    );
  });
});
