// Port of the report-building parts of MiroResponse and MiroReport:
// score bands, which template sections make up each page, and the
// variables / images substituted into the template.

import {
  JP_POSITIONS,
  JUNGIAN_FUNCTION_NAMES,
  JUNGIAN_FUNCTION_ORDER,
  JUNGIAN_POSITION_LABELS,
  LEGEND_IMAGES,
  MODE_NAMES,
  PIVOT_FUNCTION_BY_LETTERS,
  POSITION_LABELS,
  STATE_TEXT,
  STRATA_STRENGTH,
  SURVEY_ID_MIRO_V10,
  SURVEY_ID_MIRO_V11,
  type MiroLetter,
} from "./constants";
import { getMbtiType, type MiroScores } from "./scoring";

export interface ScoreThresholds {
  engagedScore: number;
  excessScore: number;
  latentScore: number;
}

export interface Practitioner {
  name: string;
  email?: string | null;
  telNo?: string | null;
  company?: string | null;
  webAddress?: string | null;
  /** Address lines (address1, address2, city, county, postcode); blanks allowed. */
  address?: (string | null | undefined)[];
}

export interface Candidate {
  firstName: string;
  lastName: string;
}

export interface ReportInput {
  testId: number;
  surveyId: number;
  candidate: Candidate;
  practitioner: Practitioner;
  scores: MiroScores;
  thresholds: ScoreThresholds;
  isFreeReport?: boolean;
  /** When the assessment was completed; shown on the cover. Defaults to today. */
  completedOn?: Date | null;
}

/** One output page = one or more template section ids, in order. */
export type ReportPage = string[];

export class MiroReportError extends Error {}

export function isExcess(score: number, t: ScoreThresholds): boolean {
  return score >= t.excessScore;
}

export function isEngaged(score: number, t: ScoreThresholds): boolean {
  return score > t.engagedScore && score < t.excessScore;
}

export function isLatent(score: number, t: ScoreThresholds): boolean {
  return score <= t.latentScore;
}

export type ModeState = "Engaged" | "Excess" | "Disengaged" | "Latent";

export function getModeState(score: number, t: ScoreThresholds): ModeState {
  if (isEngaged(score, t) || isExcess(score, t)) {
    return isExcess(score, t) ? STATE_TEXT.excess : STATE_TEXT.engaged;
  }
  return isLatent(score, t) ? STATE_TEXT.latent : STATE_TEXT.disengaged;
}

/** "Attached" modes (engaged or excess) sit in the pie; the rest are exploded out. */
export function isAttached(score: number, t: ScoreThresholds): boolean {
  return isEngaged(score, t) || isExcess(score, t);
}

export function supportsVersion(surveyId: number, version: number): boolean {
  return surveyId >= version;
}

/** Scores shifted down by a fraction of the lowest score (MiroResponse.getResults(double)). */
export function getAdjustedResults(results: number[], miroGraphAdjustment: number): number[] {
  const adjustment = Math.trunc(results[3] * miroGraphAdjustment);
  return results.map((r) => r - adjustment);
}

/**
 * Port of MiroResponse.getExtraIntroMappingKey: the V11 pivot-function section,
 * e.g. "NMIN" for Driving/Energising/Analysing with a moderate introvert score.
 */
export function getExtroIntroSectionId(scores: MiroScores): string | null {
  if (!scores.extroIntroStrata) return null;
  const pivot = PIVOT_FUNCTION_BY_LETTERS[scores.resultLetters.slice(0, 3).join("")];
  return pivot ? `${pivot}${scores.extroIntroStrata}` : null;
}

/** Port of MiroResponse.getReportPageList (V10 and V11 reports). */
export function getReportPageList(
  scores: MiroScores,
  t: ScoreThresholds,
  reportVersion: number,
  isFreeReport: boolean,
): ReportPage[] {
  if (reportVersion !== SURVEY_ID_MIRO_V10 && reportVersion !== SURVEY_ID_MIRO_V11) {
    throw new MiroReportError(`Unknown report version ${reportVersion}`);
  }
  const isV11 = reportVersion === SURVEY_ID_MIRO_V11;
  const { results: r, resultLetters: l } = scores;
  const pages: ReportPage[] = [];

  pages.push(["homepage"], ["TOC"], ["U2"], ["U2a"], ["U3"], ["U4"]);

  pages.push([isExcess(r[0], t) ? `${l[0]}1.1` : `${l[0]}1`]);

  if (isV11 && scores.extroIntroStrata) {
    const key = getExtroIntroSectionId(scores);
    if (!key) throw new MiroReportError(`No ExtroIntraMapping for ${l.join("")}`);
    pages.push([key]);
  }

  if (isEngaged(r[1], t)) {
    pages.push([`${l[1]}2`], [`${l[0]}-${l[1]}`]);
  } else {
    pages.push([l[0]], [`${l[1]}3`]);
  }

  pages.push([
    isEngaged(r[2], t) ? `${l[2]}4` : `${l[2]}5`,
    isLatent(r[3], t) ? `${l[3]}6.1` : `${l[3]}6`,
  ]);

  pages.push([isEngaged(r[0], t) && isEngaged(r[1], t) ? `${l[0]}-${l[1]}7` : `${l[0]}7`]);

  pages.push(["U5"]);
  if (isV11) pages.push(["barChartPage1"], ["barChartPage2"]);
  pages.push(["U6"], [isFreeReport ? "FREEU7" : "U7"]);
  return pages;
}

/** Port of MiroReport.generateReportLeadership01's page list. */
export function getLeadershipPageList(mbtiType: string): ReportPage[] {
  return [["homepage"], ...[1, 2, 3, 4, 5].map((n) => [`${mbtiType}_${n}`])];
}

export interface LegendEntry {
  letter?: MiroLetter;
  image: string;
  text: string;
  subText: string;
}

export function getPieLegend(scores: MiroScores, t: ScoreThresholds): LegendEntry[] {
  return scores.resultLetters.map((letter, i) => ({
    letter,
    image: LEGEND_IMAGES[letter],
    text: MODE_NAMES[letter],
    subText: `${POSITION_LABELS[i]} ${getModeState(scores.results[i], t)}`,
  }));
}

/** Port of MiroReport.addPractitionerVariables: fills v1..vN in order. */
export function getPractitionerLines(p: Practitioner): Record<string, string> {
  const vars: Record<string, string> = { v1: p.name };
  let n = 2;
  if (p.company?.trim()) vars[`v${n++}`] = p.company;
  for (const line of p.address ?? []) {
    if (line?.trim()) vars[`v${n++}`] = line;
    vars[`v${n++}`] = " ";
  }
  if (p.telNo?.trim()) vars[`v${n++}`] = `Tel: ${p.telNo}`;
  if (p.email?.trim()) vars[`v${n++}`] = `Email: ${p.email}`;
  if (p.webAddress?.trim()) vars[`v${n++}`] = `Web: ${p.webAddress}`;
  return vars;
}

/**
 * Legacy file name: punctuation stripped from names, e.g. "Roger_Test_1",
 * plus MiroResponse.getMiroReportName's "_v11" / "_lship" suffix.
 */
export function getReportFileName(
  candidate: Candidate,
  responseId: number,
  suffix: "" | "_v11" | "_lship" = "",
): string {
  const clean = (s: string) => s.replace(/[!-/:-@[-`{-~]+/g, "");
  return `${clean(candidate.firstName)}_${clean(candidate.lastName)}_${responseId}${suffix}`;
}

// ---------------------------------------------------------------------------
// V11 / leadership extras (MiroReport.addMiroPopulationChartValues, addSubPieValues)

/** MiroReport.adjustToPercent: integer percentage, 0 when either side is 0. */
function toPercent(value: number, total: number): number {
  if (value === 0 || total === 0) return 0;
  return Math.trunc((value / total) * 100);
}

function halfOf(value: number): number {
  return Math.trunc(value / 2);
}

/**
 * Values for the four "Working Styles" butterfly charts. Each bar has a left
 * and a right value (roughly 0–100); ids match the template, e.g. pmBar_2lv.
 */
export function getPopulationChartValues(
  scores: MiroScores,
  miroGraphAdjustment = 0,
): Record<string, string> {
  const adjusted = getAdjustedResults(scores.results, miroGraphAdjustment);
  const score = (letter: MiroLetter) =>
    Math.trunc(adjusted[scores.resultLetters.indexOf(letter)] * 1.15);
  const D = score("D");
  const E = score("E");
  const A = score("A");
  const O = score("O");
  const total = Math.trunc(66 - scores.results[3] * miroGraphAdjustment);
  const intra = toPercent(scores.intraValue, 20);
  const extro = toPercent(scores.extroValue, 20);
  const pct = (v: number) => toPercent(v, total);

  // Judging vs Perceiving: the mean of two (unadjusted) results each side.
  const jp = JP_POSITIONS[scores.resultLetters.join("") + extroSuffix(scores)];
  const mean = (a: number, b: number) =>
    Math.trunc((scores.results[a - 1] + scores.results[b - 1]) * 0.5);
  const jpBar: [number, number] = jp ? [pct(mean(jp[0], jp[1])), pct(mean(jp[2], jp[3]))] : [0, 0];

  const bars: Record<string, [number, number]> = {
    leadershipBar_1: [pct(D), pct(O)],
    leadershipBar_2: [pct(E), pct(A)],
    leadershipBar_3: [intra, extro],
    pmBar_1: jpBar,
    pmBar_2: [pct(A + halfOf(O)), pct(D + halfOf(E))],
    pmBar_3: [pct(E + halfOf(O)), pct(D + halfOf(A))],
    negInfluBar_1: [intra, extro],
    negInfluBar_2: [pct(E), pct(A)],
    negInfluBar_3: [pct(O), pct(D)],
    manChangeBar_1: [pct(D + halfOf(E)), pct(A + halfOf(O))],
    manChangeBar_2: [pct(D + halfOf(A)), pct(E + halfOf(O))],
    manChangeBar_3: [extro, intra],
  };
  const vars: Record<string, string> = {};
  for (const [id, [left, right]] of Object.entries(bars)) {
    vars[`${id}lv`] = String(left);
    vars[`${id}rv`] = String(right);
  }
  return vars;
}

function extroSuffix(scores: MiroScores): "EX" | "IN" {
  return scores.extroValue > scores.intraValue ? "EX" : "IN";
}

/**
 * Legend entries 5–8: the Jungian function stack for the MBTI type, with the
 * pivot function annotated by how strongly the attitude is expressed.
 */
export function getJungianLegend(mbtiType: string, scores: MiroScores): LegendEntry[] {
  const order = JUNGIAN_FUNCTION_ORDER[mbtiType];
  if (!order) throw new MiroReportError(`No Jungian function order for "${mbtiType}"`);
  const strength = scores.extroIntroStrata
    ? STRATA_STRENGTH[scores.extroIntroStrata[0]]
    : undefined;
  return order.map((fn, i) => {
    let subText = JUNGIAN_FUNCTION_NAMES[fn];
    if (i === 0 && strength && getExtroIntroSectionId(scores)) {
      subText += ` (${strength} expressed)`;
    }
    return { image: `${fn}.png`, text: JUNGIAN_POSITION_LABELS[i], subText };
  });
}

export function getReportVariables(input: ReportInput, reportVersion: number) {
  const { candidate, scores } = input;
  const fullName = `${candidate.firstName} ${candidate.lastName}`;
  if (reportVersion !== SURVEY_ID_MIRO_V10 && reportVersion !== SURVEY_ID_MIRO_V11) {
    throw new MiroReportError(`Unknown report version ${reportVersion}`);
  }
  const isV11 = reportVersion === SURVEY_ID_MIRO_V11;
  return {
    id: String(input.testId),
    firstname: candidate.firstName,
    lastname: candidate.lastName,
    name: fullName,
    name2: fullName,
    mbti: getMbtiType(scores) ?? "",
    report_title: "Miro Behavioural Mode Assessment",
    ...(isV11
      ? {
          report_type: "YOUR MIRO ENHANCED REPORT",
          sub_report_type: "Miro Enhanced Individual Report",
          report_type_colour: "MIRORED",
          report_img: "miro_individual_enhanced_report_icon.png",
        }
      : {
          report_type: "YOUR MIRO REPORT",
          sub_report_type: "Miro Individual Report",
          report_type_colour: "MIROYELLOW",
          report_img: "miro_individual_report_icon.png",
        }),
    ...getPractitionerLines(input.practitioner),
  } as Record<string, string>;
}

/** Variables for the leadership report (generateReportLeadership01). */
export function getLeadershipVariables(input: ReportInput): Record<string, string> {
  const { candidate } = input;
  const fullName = `${candidate.firstName} ${candidate.lastName}`;
  return {
    id: String(input.testId),
    firstname: candidate.firstName,
    lastname: candidate.lastName,
    name: fullName,
    name2: fullName,
    report_type_colour: "MIROGREEN",
    ...getPractitionerLines(input.practitioner),
  };
}
