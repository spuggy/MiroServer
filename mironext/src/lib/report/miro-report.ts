// Port of the report-building parts of MiroResponse and MiroReport:
// score bands, which template sections make up each page, and the
// variables / images substituted into the template.

import {
  LEGEND_IMAGES,
  MODE_NAMES,
  POSITION_LABELS,
  STATE_TEXT,
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

/** Port of MiroResponse.getReportPageList for the V10 report. */
export function getReportPageList(
  scores: MiroScores,
  t: ScoreThresholds,
  reportVersion: number,
  isFreeReport: boolean,
): ReportPage[] {
  if (reportVersion !== SURVEY_ID_MIRO_V10) {
    throw new MiroReportError(`Report version ${reportVersion} is not ported yet`);
  }
  const { results: r, resultLetters: l } = scores;
  const pages: ReportPage[] = [];

  pages.push(["homepage"], ["TOC"], ["U2"], ["U2a"], ["U3"], ["U4"]);

  pages.push([isExcess(r[0], t) ? `${l[0]}1.1` : `${l[0]}1`]);

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

  pages.push(["U5"], ["U6"], [isFreeReport ? "FREEU7" : "U7"]);
  return pages;
}

export interface LegendEntry {
  letter: MiroLetter;
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

/** Legacy file name: punctuation stripped from names, e.g. "Roger_Test_1". */
export function getReportFileName(candidate: Candidate, responseId: number): string {
  const clean = (s: string) => s.replace(/[!-/:-@[-`{-~]+/g, "");
  return `${clean(candidate.firstName)}_${clean(candidate.lastName)}_${responseId}`;
}

export function getReportVariables(input: ReportInput, reportVersion: number) {
  const { candidate, scores } = input;
  const fullName = `${candidate.firstName} ${candidate.lastName}`;
  if (reportVersion !== SURVEY_ID_MIRO_V10 && reportVersion !== SURVEY_ID_MIRO_V11) {
    throw new MiroReportError(`Unknown report version ${reportVersion}`);
  }
  return {
    id: String(input.testId),
    firstname: candidate.firstName,
    lastname: candidate.lastName,
    name: fullName,
    name2: fullName,
    mbti: getMbtiType(scores) ?? "",
    report_title: "Miro Behavioural Mode Assessment",
    report_type: "YOUR MIRO REPORT",
    sub_report_type: "Miro Individual Report",
    report_type_colour: "MIROYELLOW",
    report_img: "miro_individual_report_icon.png",
    ...getPractitionerLines(input.practitioner),
  } as Record<string, string>;
}
