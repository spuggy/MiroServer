// Entry point for MiRo individual report generation.
// TypeScript port of uk.co.bluetrail.miro.MiroReport#generateReportV10,
// #generateReportV11 and #generateReportLeadership01.

import path from "node:path";
import {
  DEFAULT_THRESHOLDS,
  MODE_COLOURS,
  MODE_NAMES,
  MODE_SHORT_NAMES,
  POSITION_LABELS,
  SURVEY_ID_MIRO_V10,
  SURVEY_ID_MIRO_V11,
} from "./constants";
import {
  getAdjustedResults,
  getJungianLegend,
  getLeadershipPageList,
  getLeadershipVariables,
  getPieLegend,
  getPopulationChartValues,
  getReportFileName,
  getReportPageList,
  getReportVariables,
  isAttached,
  MiroReportError,
  supportsVersion,
  type LegendEntry,
  type ReportInput,
  type ReportPage,
} from "./miro-report";
import { getMbtiType } from "./scoring";
import { htmlToPdf } from "./pdf";
import { renderPieSvg } from "./pie-chart";
import { renderReportHtml } from "./render-html";
import { assembleReport, loadTemplate, serializeReport } from "./template";

export * from "./constants";
export * from "./miro-report";
export * from "./scoring";
export { closePdfRenderer } from "./pdf";

export interface GenerateOptions {
  /** Folder holding xhtml/, images/ and fonts/. Defaults to <cwd>/report-assets. */
  assetsDir?: string;
  /** Footer copyright year. Defaults to the current year. */
  year?: number;
  /**
   * Legacy MiroReport shifted pie values by results[3] * adjustment, but a bug
   * in its init() meant the configured 0.75 was never applied — so the
   * reports people have been receiving use 0. Kept as an option.
   */
  miroGraphAdjustment?: number;
  /** Skip Chromium and only build the HTML (fast; for previews and tests). */
  htmlOnly?: boolean;
  /**
   * Force the MBTI type used by the V11 and leadership reports (the Java
   * tests stubbed getMBTIValue to render every leadership variant).
   */
  mbtiType?: string;
}

/** The downloadable individual reports. */
export type ReportType = "v10" | "v11" | "leadership";

export const REPORT_TYPES: { type: ReportType; label: string; minSurveyId: number }[] = [
  { type: "v10", label: "MiRo Report", minSurveyId: SURVEY_ID_MIRO_V10 },
  { type: "v11", label: "MiRo Enhanced Report", minSurveyId: SURVEY_ID_MIRO_V11 },
  { type: "leadership", label: "MiRo Leadership Report", minSurveyId: SURVEY_ID_MIRO_V11 },
];

export function isReportType(value: unknown): value is ReportType {
  return REPORT_TYPES.some((r) => r.type === value);
}

/** Reports a response supports: V11 assessments unlock the enhanced and leadership reports. */
export function availableReportTypes(surveyId: number): ReportType[] {
  return REPORT_TYPES.filter((r) => surveyId >= r.minSurveyId).map((r) => r.type);
}

export function generateReport(
  type: ReportType,
  input: ReportInput,
  options: GenerateOptions = {},
): Promise<GeneratedReport> {
  if (type === "v11") return generateReportV11(input, options);
  if (type === "leadership") return generateReportLeadership(input, options);
  return generateReportV10(input, options);
}

export interface GeneratedReport {
  fileName: string;
  /** Section ids per output page, e.g. [["homepage"], ["TOC"], …]. */
  pages: string[][];
  /** Assembled template document (the legacy out/<name>.xhtml equivalent). */
  xhtml: string;
  html: string;
  pdf?: Buffer;
  pageCount?: number;
}

export async function generateReportV10(
  input: ReportInput,
  options: GenerateOptions = {},
): Promise<GeneratedReport> {
  if (!supportsVersion(input.surveyId, SURVEY_ID_MIRO_V10)) {
    throw new MiroReportError(`Response ${input.testId} does not support version v10`);
  }
  const thresholds = input.thresholds ?? DEFAULT_THRESHOLDS;
  return generate(input, options, {
    template: "mirosource11.xhtml",
    fileName: getReportFileName(input.candidate, input.testId),
    pages: getReportPageList(
      input.scores,
      thresholds,
      SURVEY_ID_MIRO_V10,
      input.isFreeReport ?? false,
    ),
    variables: getReportVariables(input, SURVEY_ID_MIRO_V10),
    images: { imgU2: "U2.png", imgU3: "U3.png", imgU6: "U6.png" },
    title: "MiRo Report",
  });
}

export async function generateReportV11(
  input: ReportInput,
  options: GenerateOptions = {},
): Promise<GeneratedReport> {
  if (!supportsVersion(input.surveyId, SURVEY_ID_MIRO_V11)) {
    throw new MiroReportError(`Response ${input.testId} does not support version v11`);
  }
  const thresholds = input.thresholds ?? DEFAULT_THRESHOLDS;
  const mbtiType = options.mbtiType ?? requireMbti(input);
  return generate(input, options, {
    template: "mirosource11.xhtml",
    fileName: getReportFileName(input.candidate, input.testId, "_v11"),
    pages: getReportPageList(
      input.scores,
      thresholds,
      SURVEY_ID_MIRO_V11,
      input.isFreeReport ?? false,
    ),
    variables: {
      ...getReportVariables(input, SURVEY_ID_MIRO_V11),
      mbti: mbtiType,
      ...getPopulationChartValues(input.scores, options.miroGraphAdjustment ?? 0),
    },
    images: { imgU2: "U2.png", imgU3: "U3.png", imgU6: "U6.png" },
    extraLegend: getJungianLegend(mbtiType, input.scores),
    title: "MiRo Enhanced Report",
  });
}

export async function generateReportLeadership(
  input: ReportInput,
  options: GenerateOptions = {},
): Promise<GeneratedReport> {
  if (!supportsVersion(input.surveyId, SURVEY_ID_MIRO_V11)) {
    throw new MiroReportError(`Response ${input.testId} does not support version v11`);
  }
  const mbtiType = options.mbtiType ?? requireMbti(input);
  return generate(input, options, {
    template: "miro_leadership01.xhtml",
    fileName: getReportFileName(input.candidate, input.testId, "_lship"),
    pages: getLeadershipPageList(mbtiType),
    variables: {
      ...getLeadershipVariables(input),
      ...getPopulationChartValues(input.scores, options.miroGraphAdjustment ?? 0),
    },
    images: {},
    extraLegend: getJungianLegend(mbtiType, input.scores),
    title: "MiRo Leadership Report",
    hasTableOfContents: false,
    largeText: true,
  });
}

function requireMbti(input: ReportInput): string {
  const mbti = getMbtiType(input.scores);
  if (!mbti) throw new MiroReportError(`No MBTI type for response ${input.testId}`);
  return mbti;
}

interface ReportSpec {
  template: string;
  fileName: string;
  pages: ReportPage[];
  variables: Record<string, string>;
  /** Template image ids → asset file names (the pie chart and legend are added here). */
  images: Record<string, string>;
  /** Legend rows 5–8 (Jungian functions) for the V11 and leadership reports. */
  extraLegend?: LegendEntry[];
  title: string;
  hasTableOfContents?: boolean;
  /** The leadership report used 11pt body text (MiroReportPDFGeneratorContext.leaderShipContext). */
  largeText?: boolean;
}

async function generate(
  input: ReportInput,
  options: GenerateOptions,
  spec: ReportSpec,
): Promise<GeneratedReport> {
  const assetsDir = options.assetsDir ?? path.join(process.cwd(), "report-assets");
  const thresholds = input.thresholds ?? DEFAULT_THRESHOLDS;
  const isFreeReport = input.isFreeReport ?? false;
  const { scores } = input;
  const { pages, fileName } = spec;

  const variables = { ...spec.variables };
  const images: Record<string, string> = { graph: "chart:pie", ...spec.images };
  const legend = [...getPieLegend(scores, thresholds), ...(spec.extraLegend ?? [])];
  legend.forEach((entry, i) => {
    images[`miropie_img_leg${i + 1}`] = entry.image;
    variables[`miropie_txt_leg${i + 1}`] = entry.text;
    variables[`miropie_subtxt_leg${i + 1}`] = entry.subText;
  });

  const template = loadTemplate(path.join(assetsDir, "xhtml", spec.template));
  const assembled = assembleReport(template, { pages, variables, images });
  if (assembled.missingSections.length) {
    throw new MiroReportError(
      `Template is missing sections: ${assembled.missingSections.join(", ")}`,
    );
  }

  const pieValues = getAdjustedResults(scores.results, options.miroGraphAdjustment ?? 0);
  const pieSvg = renderPieSvg(
    scores.resultLetters.map((letter, i) => ({
      value: pieValues[i],
      colour: MODE_COLOURS[letter],
      exploded: !isAttached(scores.results[i], thresholds),
      label: MODE_NAMES[letter],
    })),
    {
      innerRadiusFraction: 0.56,
      explodeFraction: 0.08,
      centreLabel: {
        title: MODE_SHORT_NAMES[scores.resultLetters[0]],
        subtitle: POSITION_LABELS[0],
      },
    },
  );

  const candidateName = `${input.candidate.firstName} ${input.candidate.lastName}`;
  const { practitioner } = input;

  const html = renderReportHtml(assembled.document, {
    assetsDir,
    pieSvg,
    isFreeReport,
    year: options.year ?? new Date().getFullYear(),
    title: `${candidateName} — ${spec.title}`,
    candidateName,
    hasTableOfContents: spec.hasTableOfContents ?? true,
    largeText: spec.largeText ?? false,
    cover: {
      completedOn: formatReportDate(input.completedOn ?? new Date()),
      preparedBy: practitioner.name,
      organisation: practitioner.company?.trim() || undefined,
    },
  });

  const result: GeneratedReport = {
    fileName,
    pages,
    xhtml: serializeReport(assembled.document),
    html,
  };

  if (!options.htmlOnly) {
    const { pdf, pageCount } = await htmlToPdf(html);
    result.pdf = pdf;
    result.pageCount = pageCount;
  }
  return result;
}

/** e.g. "13 February 2026" */
export function formatReportDate(date: Date): string {
  return date.toLocaleDateString("en-GB", { day: "numeric", month: "long", year: "numeric" });
}
