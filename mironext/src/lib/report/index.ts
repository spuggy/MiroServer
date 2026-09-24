// Entry point for MiRo individual report generation.
// TypeScript port of uk.co.bluetrail.miro.MiroReport#generateReportV10.

import path from "node:path";
import {
  DEFAULT_THRESHOLDS,
  MODE_COLOURS,
  MODE_NAMES,
  SURVEY_ID_MIRO_V10,
  SURVEY_ID_MIRO_V11,
} from "./constants";
import {
  getAdjustedResults,
  getPieLegend,
  getReportFileName,
  getReportPageList,
  getReportVariables,
  isAttached,
  MiroReportError,
  supportsVersion,
  type ReportInput,
} from "./miro-report";
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
  return generate(input, SURVEY_ID_MIRO_V10, options);
}

export async function generateReportV11(
  input: ReportInput,
  _options: GenerateOptions = {},
): Promise<GeneratedReport> {
  if (!supportsVersion(input.surveyId, SURVEY_ID_MIRO_V11)) {
    throw new MiroReportError(`Response ${input.testId} does not support version v11`);
  }
  throw new MiroReportError("The V11 report has not been ported yet");
}

async function generate(
  input: ReportInput,
  reportVersion: number,
  options: GenerateOptions,
): Promise<GeneratedReport> {
  const assetsDir = options.assetsDir ?? path.join(process.cwd(), "report-assets");
  const thresholds = input.thresholds ?? DEFAULT_THRESHOLDS;
  const isFreeReport = input.isFreeReport ?? false;
  const { scores } = input;
  const fileName = getReportFileName(input.candidate, input.testId);

  const pages = getReportPageList(scores, thresholds, reportVersion, isFreeReport);

  const variables = getReportVariables(input, reportVersion);
  const images: Record<string, string> = {
    graph: "chart:pie",
    imgU2: "U2.png",
    imgU3: "U3.png",
    imgU6: "U6.png",
  };
  getPieLegend(scores, thresholds).forEach((entry, i) => {
    images[`miropie_img_leg${i + 1}`] = entry.image;
    variables[`miropie_txt_leg${i + 1}`] = entry.text;
    variables[`miropie_subtxt_leg${i + 1}`] = entry.subText;
  });

  const template = loadTemplate(path.join(assetsDir, "xhtml", "mirosource11.xhtml"));
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
  );

  const html = renderReportHtml(assembled.document, {
    assetsDir,
    pieSvg,
    isFreeReport,
    year: options.year ?? new Date().getFullYear(),
    title: `${input.candidate.firstName} ${input.candidate.lastName} — MiRo Report`,
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
