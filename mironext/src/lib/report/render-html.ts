// Turns the assembled report XHTML into print-ready HTML + CSS.
//
// Replaces the iText handlers (MiroReportPDFGenerator, HandlerFactory, P, H,
// Ul, Img, PieChart, FourIcons, Practitioner, TitlePage, PageHeader,
// PageFooter, TOC). Pagination, running header/footer, "Page N of M" and TOC
// page numbers are done by Paged.js (CSS Paged Media) inside Chromium.

import { readFileSync } from "node:fs";
import path from "node:path";
import type { Document, Element, Node } from "@xmldom/xmldom";
import {
  FOOTER_TEXT,
  FOUR_ICON_IMAGES,
  HEADING_ICON_IMAGES,
  MIRO_COLOURS,
  MODE_COLOURS,
  MODE_NAMES,
  type MiroLetter,
} from "./constants";

const ELEMENT_NODE = 1;
const TEXT_NODE = 3;
const CDATA_NODE = 4;

/** iText scaled template pixel sizes by this to get points. */
const IMAGE_CONSTANT = 0.48;

export interface RenderOptions {
  assetsDir: string;
  /** Inline SVG for the "chart:pie" image. */
  pieSvg: string;
  isFreeReport: boolean;
  year: number;
  title: string;
  /** Shown in the running header. */
  candidateName?: string;
  /** Facts listed at the foot of the cover. */
  cover?: CoverDetails;
  /** Include the Paged.js polyfill (needed for PDF; handy for browser preview). */
  includePagedJs?: boolean;
  /** Page 1 is the TOC (the individual reports); the leadership report has none. */
  hasTableOfContents?: boolean;
  /** 11pt body text, as the leadership report used. */
  largeText?: boolean;
}

export interface CoverDetails {
  completedOn?: string;
  preparedBy?: string;
  organisation?: string;
}

export function escapeHtml(s: string): string {
  return s
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;");
}

const collapse = (s: string) => s.replace(/\s+/g, " ");

function children(node: Node): Element[] {
  const out: Element[] = [];
  for (let i = 0; i < node.childNodes.length; i++) {
    const c = node.childNodes[i];
    if (c.nodeType === ELEMENT_NODE) out.push(c as Element);
  }
  return out;
}

const tag = (el: Element) => (el.localName ?? el.nodeName).toLowerCase();
const cls = (el: Element) => (el.getAttribute("class") ?? "").trim();

class AssetResolver {
  private cache = new Map<string, string>();
  constructor(private dir: string) {}

  dataUri(file: string, folder = "images"): string {
    const key = `${folder}/${file}`;
    let uri = this.cache.get(key);
    if (!uri) {
      const ext = path.extname(file).slice(1).toLowerCase();
      const mime =
        ext === "png"
          ? "image/png"
          : ext === "jpg" || ext === "jpeg"
            ? "image/jpeg"
            : ext === "ttf"
              ? "font/ttf"
              : "application/octet-stream";
      const data = readFileSync(path.join(this.dir, folder, file)).toString("base64");
      uri = `data:${mime};base64,${data}`;
      this.cache.set(key, uri);
    }
    return uri;
  }

  /** Template img src → usable URL. Legacy absolute paths are reduced to their file name. */
  image(src: string): string | null {
    if (!src.trim()) return null;
    return this.dataUri(path.basename(src));
  }
}

export function renderReportHtml(doc: Document, options: RenderOptions): string {
  const assets = new AssetResolver(options.assetsDir);
  const body = doc.getElementsByTagName("body")[0];
  const pageDivs = children(body).filter((el) => tag(el) === "div");

  const sections = pageDivs.map((div, index) => {
    const id = div.getAttribute("id");
    if (id === "0") return renderCover(div, assets, options.cover ?? {});
    if (id === "1" && options.hasTableOfContents !== false) return renderToc(div);
    return `<section class="report-page" data-page-index="${index}">${renderBlocks(div, assets, options)}</section>`;
  });

  const pagedJs = options.includePagedJs === false ? "" : pagedJsScripts(options.assetsDir);

  return `<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<title>${escapeHtml(options.title)}</title>
<style>${fontFaces(assets)}</style>
<style>${reportCss(assets, options)}</style>
${pagedJs}
</head>
<body class="${[options.isFreeReport && "free-report", options.largeText && "large-text"].filter(Boolean).join(" ")}">
${sections.join("\n")}
</body>
</html>`;
}

// ---------------------------------------------------------------------------
// Title page (TitlePage.java)

function bannerValues(div: Element): Record<string, string> {
  const vars: Record<string, string> = {};
  for (const el of children(div)) {
    const id = el.getAttribute("id");
    if (id) vars[id] = el.textContent ?? "";
  }
  return vars;
}

function renderCover(div: Element, assets: AssetResolver, details: CoverDetails): string {
  const v = bannerValues(div);
  const colourKey = v.report_type_colour as keyof typeof MIRO_COLOURS;
  const accent = MIRO_COLOURS[colourKey] ?? "#808080";
  const facts: [string, string | undefined][] = [
    ["Report", v.sub_report_type],
    ["Completed", details.completedOn],
    [
      "Prepared by",
      [details.preparedBy, details.organisation].filter((s) => s?.trim()).join(", ") || undefined,
    ],
  ];
  const factsHtml = facts
    .filter(([, value]) => value?.trim())
    .map(([label, value]) => `<div><dt>${label}</dt><dd>${escapeHtml(value!)}</dd></div>`)
    .join("");
  return `<section class="cover">
  ${coverArtwork()}
  <img class="cover-logo" src="${assets.dataUri("mirologo-big.png")}" alt="MiRo — understanding people" />
  <div class="cover-main">
    ${v.report_type ? `<div class="cover-report-type"><span style="background:${accent}"></span>${escapeHtml(v.report_type)}</div>` : ""}
    ${v.name ? `<h1 class="cover-name">${escapeHtml(v.name)}</h1>` : ""}
    ${v.report_title ? `<div class="cover-title">${escapeHtml(v.report_title)}</div>` : ""}
  </div>
  ${factsHtml ? `<dl class="cover-facts">${factsHtml}</dl>` : ""}
</section>`;
}

/** The four modes as overlapping circles, echoing the summary model diagram. */
function coverArtwork(): string {
  const circle = (letter: MiroLetter, cx: number, cy: number) =>
    `<circle cx="${cx}" cy="${cy}" r="112" fill="${MODE_COLOURS[letter]}" />`;
  return `<svg class="cover-art" viewBox="0 0 400 400" aria-hidden="true">
    ${circle("D", 140, 140)}${circle("E", 260, 140)}${circle("A", 140, 260)}${circle("O", 260, 260)}
  </svg>`;
}

// ---------------------------------------------------------------------------
// Table of contents (TOC.java) — page numbers resolved by Paged.js.

function renderToc(div: Element): string {
  let header = "Table of Contents";
  const items: string[] = [];
  for (const el of children(div)) {
    if (tag(el) !== "p") continue;
    const id = el.getAttribute("id") ?? "";
    const text = collapse(el.textContent ?? "").trim();
    if (id === "header") header = text;
    else
      items.push(
        `<li><a href="#target-${escapeHtml(id)}"><span class="toc-text">${escapeHtml(text)}</span><span class="toc-leader"></span></a></li>`,
      );
  }
  return `<section class="report-page toc"><h3>${escapeHtml(header)}</h3><ol class="toc-list">${items.join("")}</ol></section>`;
}

// ---------------------------------------------------------------------------
// Block content (HandlerFactory dispatch)

function renderBlocks(container: Element, assets: AssetResolver, options: RenderOptions): string {
  return children(container)
    .map((el) => renderBlock(el, assets, options))
    .join("\n");
}

function tocAnchor(el: Element): string {
  const match = cls(el).match(/\btoc_\d+\b/);
  return match ? ` id="target-${match[0]}"` : "";
}

function renderBlock(el: Element, assets: AssetResolver, options: RenderOptions): string {
  const name = tag(el);
  const className = cls(el);

  if (name === "ul" && className === "population_bar_chart") return renderBarChart(el);
  if (name === "ul") return renderList(el, assets);
  if (name === "img") return renderImage(el, assets, "block-image");
  if (name === "p" && className === "fouricons") return renderFourIcons(assets);
  if (name === "p" && className === "practitioner") return renderPractitioner(el);
  if (name === "p" && className === "miropie") return renderPie(el, assets, options);
  if (name === "p") {
    const inner = renderInline(el, assets).trim();
    if (!inner) return "";
    const pClass = paragraphClass(el);
    const extra = pClass ? ` class="${pClass}"` : "";
    return `<p${extra}${tocAnchor(el)}>${inner}</p>`;
  }
  if (/^h[1-4]$/.test(name)) return renderHeading(el, assets);
  if (name === "b") return `<p><b>${escapeHtml(collapse(el.textContent ?? ""))}</b></p>`;
  return ""; // DefaultHandler → empty paragraph
}

/** Template paragraph styles: class="firstpage" / "italics", or an iText font name in style. */
function paragraphClass(el: Element): string {
  const className = cls(el);
  if (className === "firstpage" || className === "italics") return className;
  const font = /font-family:\s*([\w-]+)/i.exec(el.getAttribute("style") ?? "")?.[1]?.toLowerCase();
  if (font === "bigtextfont") return "big-text";
  if (font === "smallitalics") return "small-italics";
  if (font === "italics") return "italics";
  return "";
}

function renderHeading(el: Element, assets: AssetResolver): string {
  const name = tag(el);
  const className = cls(el);
  const text = collapse(el.textContent ?? "").trim();
  if (!text) return "";
  const classes: string[] = [];
  if (className.toLowerCase() === "center") classes.push("center");
  if (el.getAttribute("id") === "mbti") classes.push("mbti");
  const classAttr = classes.length ? ` class="${classes.join(" ")}"` : "";
  const html = `<${name}${classAttr}${tocAnchor(el)}>${escapeHtml(text)}</${name}>`;

  const icons = className.match(/^icon_([a-z]+)$/i);
  if (!icons) return html;
  const letters = icons[1]
    .toUpperCase()
    .split("")
    .filter((c): c is MiroLetter => c in MODE_COLOURS);
  const imgs = letters
    .map((c) => HEADING_ICON_IMAGES[c.toLowerCase()])
    .filter(Boolean)
    .map((file) => `<img src="${assets.dataUri(file)}" alt="" />`)
    .join("");
  return `<div class="title-block" style="--rule:${modeRule(letters)}">${html}<div class="heading-icons">${imgs}</div></div>`;
}

/** Underline for a mode title block: one colour, or equal hard-edged bands. */
function modeRule(letters: MiroLetter[]): string {
  if (letters.length <= 1) return MODE_COLOURS[letters[0]] ?? "var(--ink)";
  const step = 100 / letters.length;
  const stops = letters.map(
    (l, i) => `${MODE_COLOURS[l]} ${(i * step).toFixed(1)}% ${((i + 1) * step).toFixed(1)}%`,
  );
  return `linear-gradient(90deg, ${stops.join(", ")})`;
}

function renderInline(el: Element, assets: AssetResolver): string {
  let out = "";
  for (let i = 0; i < el.childNodes.length; i++) {
    const node = el.childNodes[i];
    if (node.nodeType === TEXT_NODE || node.nodeType === CDATA_NODE) {
      out += escapeHtml(collapse(node.nodeValue ?? ""));
    } else if (node.nodeType === ELEMENT_NODE) {
      const child = node as Element;
      const childName = tag(child);
      if (childName === "b") {
        const bClass = cls(child) === "firstpage" ? ` class="firstpage"` : "";
        out += `<b${bClass}>${escapeHtml(collapse(child.textContent ?? ""))}</b>`;
      } else if (childName === "img") {
        out += renderImage(child, assets, "inline-image");
      } else if (childName === "br") {
        out += "<br />";
      } else {
        out += renderInline(child, assets);
      }
    }
  }
  return out;
}

function renderList(el: Element, assets: AssetResolver): string {
  const items = children(el)
    .filter((li) => tag(li) === "li")
    .map((li) => `<li>${renderInline(li, assets).trim()}</li>`)
    .join("");
  const compact = cls(el).toLowerCase() === "compact" ? ` class="compact"` : "";
  return `<ul${compact}>${items}</ul>`;
}

/**
 * PopulationBarChart.java: three rows of "left text | left bar ← centre → right
 * bar | right text". Values come from ids like pmBar_2lv; roughly 0–100.
 */
function renderBarChart(el: Element): string {
  const chartId = el.getAttribute("id") ?? "";
  const byId = new Map<string, string>();
  for (const li of children(el)) {
    const id = li.getAttribute("id");
    if (id) byId.set(id, collapse(li.textContent ?? "").trim());
  }
  const width = (v: string | undefined) => {
    const n = Number(v);
    return Number.isFinite(n) ? Math.max(0, Math.min(100, n)) : 0;
  };
  const rows: string[] = [];
  for (let n = 1; n < 4; n++) {
    const key = `${chartId}_${n}`;
    const left = byId.get(`${key}tl`);
    const right = byId.get(`${key}tr`);
    if (left === undefined && right === undefined) continue;
    const lv = width(byId.get(`${key}lv`));
    const rv = width(byId.get(`${key}rv`));
    rows.push(`<div class="bar-row">
      <div class="bar-text bar-text-left">${escapeHtml(left ?? "")}</div>
      <div class="bar-track bar-track-left"><span style="width:${lv}%"></span></div>
      <div class="bar-track bar-track-right"><span style="width:${rv}%"></span></div>
      <div class="bar-text bar-text-right">${escapeHtml(right ?? "")}</div>
    </div>`);
  }
  return rows.length ? `<div class="bar-chart">${rows.join("")}</div>` : "";
}

function renderImage(el: Element, assets: AssetResolver, className: string): string {
  const src = el.getAttribute("src") ?? "";
  const url = assets.image(src);
  if (!url) return "";
  const w = Number(el.getAttribute("width")) || 0;
  const h = Number(el.getAttribute("height")) || 0;
  const style = w && h ? ` style="width:${(w * IMAGE_CONSTANT).toFixed(1)}pt"` : "";
  return `<img class="${className}" src="${url}" alt="${escapeHtml(el.getAttribute("alt") ?? "")}"${style} />`;
}

function renderFourIcons(assets: AssetResolver): string {
  const labels = ["Driving", "Energising", "Analysing", "Organising"];
  const cells = FOUR_ICON_IMAGES.map(
    (file, i) => `<figure><img src="${assets.dataUri(file)}" alt="${labels[i]}" /></figure>`,
  ).join("");
  return `<div class="four-icons">${cells}</div>`;
}

function renderPractitioner(el: Element): string {
  const lines = children(el)
    .map((c) => collapse(c.textContent ?? "").trim())
    .filter(Boolean)
    .map((line) => `${escapeHtml(line)}<br />`)
    .join("");
  return `<p class="practitioner">${lines}</p>`;
}

function renderPie(el: Element, assets: AssetResolver, options: RenderOptions): string {
  const byId = new Map<string, Element>();
  for (const c of children(el)) {
    const id = c.getAttribute("id");
    if (id) byId.set(id, c);
  }
  const modeByName = new Map(
    (Object.keys(MODE_NAMES) as MiroLetter[]).map((l) => [MODE_NAMES[l], l]),
  );
  const modes: string[] = [];
  const functions: string[] = [];
  for (let i = 1; i < 9; i++) {
    const img = byId.get(`miropie_img_leg${i}`);
    const text = byId.get(`miropie_txt_leg${i}`)?.textContent?.trim();
    const sub = byId.get(`miropie_subtxt_leg${i}`)?.textContent?.trim();
    const letter = text ? modeByName.get(text) : undefined;
    const imgUrl = img && !letter ? assets.image(img.getAttribute("src") ?? "") : null;
    if (!letter && !imgUrl && !text && !sub) continue;
    const marker = letter
      ? `<i class="swatch" style="background:${MODE_COLOURS[letter]}"></i>`
      : imgUrl
        ? `<img src="${imgUrl}" alt="" />`
        : "<span></span>";
    (i <= 4 ? modes : functions).push(`<li>
      ${marker}
      <div>${text ? `<strong>${escapeHtml(text)}</strong>` : ""}${sub ? `<span>${escapeHtml(sub)}</span>` : ""}</div>
    </li>`);
  }
  const graph = byId.get("graph");
  const chart = graph?.getAttribute("src") === "chart:pie" ? options.pieSvg : "";
  const group = (label: string, rows: string[]) =>
    rows.length
      ? `<div class="legend-group"><div class="legend-label">${label}</div><ul class="miro-pie-legend">${rows.join("")}</ul></div>`
      : "";
  return `<div class="miro-pie"><div class="miro-pie-chart">${chart}</div><div class="miro-pie-key">${group("Your modes", modes)}${group("Jungian functions", functions)}</div></div>`;
}

// ---------------------------------------------------------------------------
// Styles

function fontFaces(assets: AssetResolver): string {
  const face = (family: string, file: string, weight: number) =>
    `@font-face{font-family:"${family}";src:url(${assets.dataUri(file, "fonts")}) format("truetype");font-weight:${weight};font-style:normal;}`;
  return [
    face("MiRo DIN", "DINRegular.ttf", 400),
    face("MiRo DIN", "DINBold.ttf", 700),
    face("MiRo Helvetica", "HelveticaLT-55-Roman.ttf", 400),
    face("MiRo Helvetica", "HelveticaLT-75-Bold.ttf", 700),
  ].join("\n");
}

function reportCss(assets: AssetResolver, options: RenderOptions): string {
  const headerLogo = assets.dataUri("mirologo_header.png");
  const cssString = (s: string) => s.replace(/\\/g, "\\\\").replace(/"/g, '\\"');
  const footerText = cssString(`${FOOTER_TEXT}${options.year}`);
  const runningTitle = cssString(options.candidateName ?? "");
  // A4 with the iText margins (64/56/56/56pt). Header: logo left, candidate
  // name right, over a hairline. Footer: hairline, "Page N of M" / copyright.
  return `
:root {
  --miro-yellow: ${MIRO_COLOURS.MIROYELLOW};
  --miro-green: ${MIRO_COLOURS.MIROGREEN};
  --miro-blue: ${MIRO_COLOURS.MIROBLUE};
  --miro-red: ${MIRO_COLOURS.MIRORED};
  --ink: #1f232a;
  --muted: #6b717c;
  --hairline: #d6d9df;
  --tint: #f3f4f6;
}

@page {
  size: A4;
  margin: 64pt 56pt 56pt 56pt;
  @top-left {
    content: "";
    height: 36.5pt;
    margin-top: 19pt;
    background-image: url("${headerLogo}");
    background-repeat: no-repeat;
    background-size: 64pt 27.4pt;
    background-position: left 2pt;
    border-bottom: 0.5pt solid var(--hairline);
    vertical-align: top;
  }
  @top-center {
    content: "";
    height: 36.5pt;
    margin-top: 19pt;
    border-bottom: 0.5pt solid var(--hairline);
  }
  @top-right {
    content: "${runningTitle}";
    height: 36.5pt;
    margin-top: 19pt;
    font: 8pt "MiRo Helvetica", Helvetica, Arial, sans-serif;
    color: var(--muted);
    text-align: right;
    vertical-align: bottom;
    padding-bottom: 7pt;
    border-bottom: 0.5pt solid var(--hairline);
  }
  @bottom-left {
    content: "Page " counter(page) " of " counter(pages);
    font: 7.5pt "MiRo Helvetica", Helvetica, Arial, sans-serif;
    color: var(--muted);
    border-top: 0.5pt solid var(--hairline);
    vertical-align: top;
    padding-top: 6pt;
    margin-top: 14pt;
    height: 20pt;
  }
  @bottom-right {
    content: "${footerText}";
    font: 7.5pt "MiRo Helvetica", Helvetica, Arial, sans-serif;
    color: var(--muted);
    text-align: right;
    border-top: 0.5pt solid var(--hairline);
    vertical-align: top;
    padding-top: 6pt;
    margin-top: 14pt;
    height: 20pt;
  }
}

@page cover {
  margin: 0;
  @top-left { content: none; border: none; background: none; }
  @top-center { content: none; border: none; }
  @top-right { content: none; border: none; }
  @bottom-left { content: none; border: none; }
  @bottom-right { content: none; border: none; }
}

html, body { margin: 0; padding: 0; }
body {
  font-family: "MiRo Helvetica", Helvetica, Arial, sans-serif;
  font-size: 10pt;
  line-height: 13.5pt;
  color: var(--ink);
  -webkit-print-color-adjust: exact;
  print-color-adjust: exact;
}

.report-page { break-before: page; }

h1, h2, h3, h4 {
  font-family: "MiRo DIN", "DIN Alternate", Arial, sans-serif;
  font-weight: 700;
  margin: 0;
  break-after: avoid;
  text-wrap: balance;
}
h1 { font-size: 40pt; line-height: 43pt; margin-bottom: 24pt; letter-spacing: -0.4pt; }
h2 { font-size: 12pt; line-height: 15pt; margin-bottom: 12pt; }
h3 { font-size: 17pt; line-height: 20pt; margin-bottom: 12pt; letter-spacing: -0.1pt; }
h4 { font-size: 11pt; line-height: 14pt; margin: 13pt 0 3pt; }
.center { text-align: center; }
h1.mbti { font-size: 44pt; margin: 4pt 0 22pt; }

p { margin: 0 0 9pt; orphans: 2; widows: 2; }
b { font-weight: 700; }
p.firstpage {
  font-family: "MiRo DIN", Arial, sans-serif;
  font-size: 13.5pt;
  line-height: 18pt;
}
b.firstpage { font-family: "MiRo DIN", Arial, sans-serif; }

ul {
  list-style: none;
  margin: 0 40pt 9pt 0;
  padding: 0;
}
ul.compact { margin-bottom: 0; }
li {
  position: relative;
  padding-left: 11pt;
  margin-bottom: 1.5pt;
  break-inside: avoid;
}
li::before {
  content: "";
  position: absolute;
  left: 1pt;
  top: 5pt;
  width: 3.5pt;
  height: 3.5pt;
  border-radius: 50%;
  background: var(--muted);
}

/* Mode title block: heading + icons over a mode-coloured rule */
.title-block {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 14pt;
  margin-bottom: 12pt;
  padding-bottom: 8pt;
  position: relative;
  break-after: avoid;
  break-inside: avoid;
}
.title-block::after {
  content: "";
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  height: 2.5pt;
  border-radius: 2pt;
  background: var(--rule);
}
.title-block > h1, .title-block > h2, .title-block > h3, .title-block > h4 { margin: 0; }
.heading-icons { display: flex; gap: 5pt; flex: none; }
.heading-icons img { width: 46pt; height: 46pt; }

.block-image {
  display: block;
  max-width: 100%;
  height: auto;
  margin: 18pt auto 10pt;
  break-inside: avoid;
}

.four-icons {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 8pt;
  margin: 20pt 0;
}
.four-icons figure { margin: 0; text-align: center; }
.four-icons img { width: 89pt; height: auto; }

/* Results donut + key */
.miro-pie {
  display: grid;
  grid-template-columns: 1.25fr 1fr;
  gap: 20pt;
  align-items: center;
  margin: 6pt 0 18pt;
  padding-bottom: 16pt;
  border-bottom: 0.5pt solid var(--hairline);
  break-inside: avoid;
}
.miro-pie-chart svg { width: 100%; height: auto; max-height: 230pt; display: block; }
.pie-centre-title { font-family: "MiRo DIN", Arial, sans-serif; fill: var(--ink); }
.pie-centre-subtitle { font-family: "MiRo DIN", Arial, sans-serif; fill: var(--muted); }
.miro-pie-key { display: grid; gap: 12pt; }
.legend-label {
  font-family: "MiRo DIN", Arial, sans-serif;
  font-weight: 700;
  font-size: 7pt;
  letter-spacing: 1pt;
  text-transform: uppercase;
  color: var(--muted);
  padding-bottom: 4pt;
  margin-bottom: 6pt;
  border-bottom: 0.5pt solid var(--hairline);
}
.miro-pie-legend { margin: 0; }
.miro-pie-legend li {
  display: grid;
  grid-template-columns: 18pt 1fr;
  column-gap: 7pt;
  align-items: center;
  padding: 0;
  margin-bottom: 6pt;
}
.miro-pie-legend li::before { content: none; }
.miro-pie-legend img { width: 18pt; height: 18pt; }
.miro-pie-legend .swatch {
  display: block;
  width: 11pt;
  height: 11pt;
  border-radius: 50%;
  margin-left: 3.5pt;
}
.miro-pie-legend strong {
  display: block;
  font-family: "MiRo DIN", Arial, sans-serif;
  font-size: 9.5pt;
  line-height: 11pt;
}
.miro-pie-legend span {
  display: block;
  font-family: "MiRo DIN", Arial, sans-serif;
  font-size: 7.5pt;
  line-height: 9.5pt;
  color: var(--muted);
}

.practitioner { line-height: 14pt; }

p.italics { font-style: italic; }
p.small-italics { font-style: italic; font-size: 7.5pt; line-height: 10pt; color: var(--muted); }
p.big-text {
  font-family: "MiRo DIN", Arial, sans-serif;
  font-size: 14pt;
  line-height: 18pt;
  margin-bottom: 6pt;
}
.large-text { font-size: 11pt; line-height: 14pt; }
.large-text p { margin-bottom: 7pt; }

/* Working-style butterfly charts (PopulationBarChart) */
.bar-chart { margin: 8pt 0 18pt; }
.bar-row {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr;
  align-items: center;
  border-bottom: 0.5pt solid var(--hairline);
  break-inside: avoid;
}
.bar-text { font-size: 9pt; line-height: 11.5pt; padding: 8pt 8pt 8pt 0; }
.bar-text-right { text-align: right; padding: 8pt 0 8pt 8pt; }
.bar-track { display: flex; align-self: stretch; align-items: center; }
.bar-track-left { justify-content: flex-end; border-right: 0.75pt solid #9aa0aa; }
.bar-track span { display: block; height: 20pt; }
.bar-track-left span { background: #ff9200; border-radius: 2pt 0 0 2pt; }
.bar-track-right span { background: #932092; border-radius: 0 2pt 2pt 0; }

/* Table of contents with dotted leaders */
.toc h3 { font-size: 22pt; line-height: 26pt; margin: 6pt 0 18pt; }
.toc-list { list-style: none; margin: 0; padding: 0; }
.toc-list li { padding: 0; margin: 0; border-bottom: 0.5pt solid var(--tint); }
.toc-list li::before { content: none; }
.toc-list a {
  color: inherit;
  text-decoration: none;
  display: flex;
  align-items: baseline;
  gap: 6pt;
  padding: 7pt 0;
  font-size: 11pt;
}
.toc-leader { flex: 1; border-bottom: 1pt dotted #b7bcc5; transform: translateY(-3pt); }
.toc-list a::after {
  content: target-counter(attr(href), page);
  font-family: "MiRo DIN", Arial, sans-serif;
  font-weight: 700;
  min-width: 14pt;
  text-align: right;
}

/* Cover */
.cover {
  page: cover;
  position: relative;
  width: 210mm;
  height: 296mm;
  overflow: hidden;
}
.cover-art {
  position: absolute;
  right: -118pt;
  top: -112pt;
  width: 440pt;
  height: 440pt;
}
.cover-art circle { opacity: 0.86; }
.cover-logo { position: absolute; left: 56pt; top: 64pt; width: 150pt; }
.cover-main {
  position: absolute;
  left: 56pt;
  right: 56pt;
  top: 430pt;
}
.cover-report-type {
  display: flex;
  align-items: center;
  gap: 8pt;
  font-family: "MiRo DIN", Arial, sans-serif;
  font-weight: 700;
  font-size: 10pt;
  letter-spacing: 1.6pt;
  text-transform: uppercase;
  color: var(--muted);
  margin-bottom: 14pt;
}
.cover-report-type span { width: 22pt; height: 4pt; border-radius: 2pt; }
.cover-name {
  font-size: 44pt;
  line-height: 46pt;
  letter-spacing: -0.6pt;
  margin: 0 0 12pt;
}
.cover-title {
  font-family: "MiRo DIN", Arial, sans-serif;
  font-size: 15pt;
  line-height: 19pt;
  color: var(--muted);
}
.cover-facts {
  position: absolute;
  left: 56pt;
  right: 56pt;
  bottom: 56pt;
  margin: 0;
  padding-top: 14pt;
  border-top: 0.5pt solid var(--hairline);
  display: grid;
  grid-template-columns: 1fr 1fr 1.4fr;
  gap: 16pt;
}
.cover-facts dt {
  font-family: "MiRo DIN", Arial, sans-serif;
  font-weight: 700;
  font-size: 7pt;
  letter-spacing: 1pt;
  text-transform: uppercase;
  color: var(--muted);
  margin-bottom: 3pt;
}
.cover-facts dd { margin: 0; font-size: 10pt; line-height: 13pt; }

/* "SAMPLE" watermark for free reports */
.free-report .pagedjs_page { position: relative; }
.free-report .pagedjs_page::after {
  content: "SAMPLE";
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%) rotate(-45deg);
  font: 96pt "MiRo Helvetica", Helvetica, Arial, sans-serif;
  color: rgba(0, 0, 0, 0.1);
  pointer-events: none;
  z-index: 10;
}

@media screen {
  body { background: #d9d9d9; }
  .pagedjs_page { background: #fff; margin: 12pt auto; box-shadow: 0 1pt 6pt rgba(0,0,0,.25); }
}
`;
}

// ---------------------------------------------------------------------------
// Paged.js

// Paged.js 0.4.3 is vendored in report-assets/vendor (MIT, see pagedjs-LICENSE.md).
// Loading it from node_modules broke under Next.js, whose bundler rewrites
// require.resolve paths.
const pagedJsCache = new Map<string, string>();

function pagedJsScripts(assetsDir: string): string {
  let source = pagedJsCache.get(assetsDir);
  if (!source) {
    source = readFileSync(path.join(assetsDir, "vendor", "paged.polyfill.min.js"), "utf8").replace(
      /<\/script/gi,
      "<\\/script",
    );
    pagedJsCache.set(assetsDir, source);
  }
  return `<script>
window.PagedConfig = {
  auto: true,
  after: function (flow) { window.__MIRO_PAGED__ = { pages: flow.total }; }
};
</script>
<script>${source}</script>`;
}
