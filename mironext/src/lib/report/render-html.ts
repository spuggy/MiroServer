// Turns the assembled report XHTML into print-ready HTML + CSS.
//
// Replaces the iText handlers (MiroReportPDFGenerator, HandlerFactory, P, H,
// Ul, Img, PieChart, FourIcons, Practitioner, TitlePage, PageHeader,
// PageFooter, TOC). Pagination, running header/footer, "Page N of M" and TOC
// page numbers are done by Paged.js (CSS Paged Media) inside Chromium.

import { readFileSync } from "node:fs";
import path from "node:path";
import type { Document, Element, Node } from "@xmldom/xmldom";
import { FOOTER_TEXT, FOUR_ICON_IMAGES, HEADING_ICON_IMAGES, MIRO_COLOURS } from "./constants";

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
  /** Include the Paged.js polyfill (needed for PDF; handy for browser preview). */
  includePagedJs?: boolean;
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
    if (id === "0") return renderCover(div, assets);
    if (id === "1") return renderToc(div);
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
<body class="${options.isFreeReport ? "free-report" : ""}">
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

function renderCover(div: Element, assets: AssetResolver): string {
  const v = bannerValues(div);
  const colourKey = v.report_type_colour as keyof typeof MIRO_COLOURS;
  const bannerColour = MIRO_COLOURS[colourKey] ?? "#808080";
  const reportImg = v.report_img ? assets.dataUri(v.report_img) : null;
  return `<section class="cover">
  <img class="cover-logo" src="${assets.dataUri("mirologo-big.png")}" alt="MiRo — understanding people" />
  ${v.report_type ? `<div class="cover-report-type" style="background:${bannerColour}">${escapeHtml(v.report_type)}</div>` : ""}
  ${v.name ? `<h1 class="cover-name">${escapeHtml(v.name)}</h1>` : ""}
  <div class="cover-panel">
    ${reportImg ? `<img class="cover-panel-icon" src="${reportImg}" alt="" />` : ""}
    <div class="cover-panel-text">
      <div class="cover-title">${escapeHtml(v.report_title ?? "")}</div>
      <div class="cover-subtitle">${escapeHtml(v.sub_report_type ?? "")}</div>
    </div>
  </div>
</section>`;
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
    else items.push(`<li><a href="#target-${escapeHtml(id)}">${escapeHtml(text)}</a></li>`);
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

  if (name === "ul" && className === "population_bar_chart") return ""; // V11 only
  if (name === "ul") return renderList(el, assets);
  if (name === "img") return renderImage(el, assets, "block-image");
  if (name === "p" && className === "fouricons") return renderFourIcons(assets);
  if (name === "p" && className === "practitioner") return renderPractitioner(el);
  if (name === "p" && className === "miropie") return renderPie(el, assets, options);
  if (name === "p") {
    const inner = renderInline(el, assets).trim();
    if (!inner) return "";
    const extra = className === "firstpage" ? ` class="firstpage"` : "";
    return `<p${extra}${tocAnchor(el)}>${inner}</p>`;
  }
  if (/^h[1-4]$/.test(name)) return renderHeading(el, assets);
  if (name === "b") return `<p><b>${escapeHtml(collapse(el.textContent ?? ""))}</b></p>`;
  return ""; // DefaultHandler → empty paragraph
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
  let html = `<${name}${classAttr}${tocAnchor(el)}>${escapeHtml(text)}</${name}>`;

  const icons = className.match(/^icon_([a-z]+)$/i);
  if (icons) {
    const imgs = icons[1]
      .toLowerCase()
      .split("")
      .map((c) => HEADING_ICON_IMAGES[c])
      .filter(Boolean)
      .map((file) => `<img src="${assets.dataUri(file)}" alt="" />`)
      .join("");
    html += `<div class="heading-icons">${imgs}</div>`;
  }
  return html;
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
  const rows: string[] = [];
  for (let i = 1; i < 9; i++) {
    const img = byId.get(`miropie_img_leg${i}`);
    const text = byId.get(`miropie_txt_leg${i}`)?.textContent?.trim();
    const sub = byId.get(`miropie_subtxt_leg${i}`)?.textContent?.trim();
    const imgUrl = img ? assets.image(img.getAttribute("src") ?? "") : null;
    if (!imgUrl && !text && !sub) continue;
    rows.push(`<li>
      ${imgUrl ? `<img src="${imgUrl}" alt="" />` : "<span></span>"}
      <div>${text ? `<strong>${escapeHtml(text)}</strong>` : ""}${sub ? `<span>${escapeHtml(sub)}</span>` : ""}</div>
    </li>`);
  }
  const graph = byId.get("graph");
  const chart = graph?.getAttribute("src") === "chart:pie" ? options.pieSvg : "";
  return `<div class="miro-pie"><div class="miro-pie-chart">${chart}</div><ul class="miro-pie-legend">${rows.join("")}</ul></div>`;
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
  const footerText = `${FOOTER_TEXT}${options.year}`.replace(/"/g, '\\"');
  // Page geometry follows the iText version: A4, margins 64/56/56/56pt,
  // logo + rule at the top, rule + "Page N of M" / copyright at the bottom.
  return `
:root {
  --miro-yellow: ${MIRO_COLOURS.MIROYELLOW};
  --miro-green: ${MIRO_COLOURS.MIROGREEN};
  --miro-blue: ${MIRO_COLOURS.MIROBLUE};
  --miro-red: ${MIRO_COLOURS.MIRORED};
  --title-grey: ${MIRO_COLOURS.TITLE_BG_GREY};
  --rule: #9a9a9a;
  --ink: #1d1d1b;
}

@page {
  size: A4;
  margin: 64pt 56pt 56pt 56pt;
  @top-left {
    content: "";
    width: 100%;
    height: 36.5pt;
    margin-top: 19pt;
    background-image: url("${headerLogo}");
    background-repeat: no-repeat;
    background-size: 73.9pt 31.7pt;
    background-position: left top;
    border-bottom: 0.75pt solid var(--rule);
    vertical-align: top;
  }
  @bottom-left {
    content: "Page " counter(page) " of " counter(pages);
    font: 8pt "MiRo Helvetica", Helvetica, Arial, sans-serif;
    color: var(--ink);
    border-top: 0.75pt solid var(--rule);
    vertical-align: top;
    padding-top: 5pt;
    margin-top: 14pt;
    height: 20pt;
  }
  @bottom-right {
    content: "${footerText}";
    font: 8pt "MiRo Helvetica", Helvetica, Arial, sans-serif;
    color: var(--ink);
    text-align: right;
    border-top: 0.75pt solid var(--rule);
    vertical-align: top;
    padding-top: 5pt;
    margin-top: 14pt;
    height: 20pt;
  }
}

@page cover {
  margin: 0;
  @top-left { content: none; border: none; background: none; }
  @bottom-left { content: none; border: none; }
  @bottom-right { content: none; border: none; }
}

html, body { margin: 0; padding: 0; }
body {
  font-family: "MiRo Helvetica", Helvetica, Arial, sans-serif;
  font-size: 10pt;
  line-height: 13pt;
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
}
h1 { font-size: 44pt; line-height: 47pt; margin-bottom: 26pt; letter-spacing: -0.2pt; }
h2 { font-size: 12pt; line-height: 15pt; margin-bottom: 12pt; }
h3 { font-size: 16pt; line-height: 19pt; margin-bottom: 10pt; }
h4 { font-size: 11.5pt; line-height: 14pt; margin: 12pt 0 3pt; }
.center { text-align: center; }
h1.mbti { font-size: 44pt; margin: 4pt 0 22pt; }

p { margin: 0 0 10pt; orphans: 2; widows: 2; }
b { font-weight: 700; }
p.firstpage {
  font-family: "MiRo DIN", Arial, sans-serif;
  font-size: 14pt;
  line-height: 17.5pt;
}
b.firstpage { font-family: "MiRo DIN", Arial, sans-serif; }

ul {
  list-style: none;
  margin: 0 50pt 10pt 0;
  padding: 0;
}
ul.compact { margin-bottom: 0; }
li {
  position: relative;
  padding-left: 9pt;
  break-inside: avoid;
}
li::before {
  content: "\\2022";
  position: absolute;
  left: 0;
}

.heading-icons {
  float: right;
  display: flex;
  gap: 4pt;
  margin: 0 0 6pt 10pt;
}
.heading-icons img { width: 80pt; height: 80pt; }

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

.miro-pie {
  display: grid;
  grid-template-columns: 3fr 1fr;
  gap: 6pt;
  margin: 4pt 0 18pt;
  break-inside: avoid;
}
.miro-pie-chart svg { width: 100%; height: auto; max-height: 240pt; display: block; }
.miro-pie-legend { margin: 6pt 0 0; }
.miro-pie-legend li {
  display: grid;
  grid-template-columns: 22pt 1fr;
  column-gap: 6pt;
  align-items: center;
  padding: 0;
  margin-bottom: 7pt;
}
.miro-pie-legend li::before { content: none; }
.miro-pie-legend img { width: 22pt; height: 22pt; }
.miro-pie-legend strong {
  display: block;
  font-family: "MiRo DIN", Arial, sans-serif;
  font-size: 9pt;
  line-height: 10.5pt;
}
.miro-pie-legend span {
  display: block;
  font-family: "MiRo DIN", Arial, sans-serif;
  font-size: 7pt;
  line-height: 9pt;
  color: #555;
}

.practitioner { line-height: 13pt; }

/* Table of contents */
.toc h3 { margin-bottom: 14pt; }
.toc-list { list-style: none; margin: 0; padding: 0; width: 340pt; }
.toc-list li { padding: 0; margin: 0 0 3pt; }
.toc-list li::before { content: none; }
.toc-list a {
  color: inherit;
  text-decoration: none;
  display: flex;
  justify-content: space-between;
  gap: 8pt;
}
.toc-list a::after {
  content: target-counter(attr(href), page);
}

/* Cover page (TitlePage.java coordinates, in points from the top-left) */
.cover {
  page: cover;
  position: relative;
  width: 210mm;
  height: 296mm;
  overflow: hidden;
}
.cover-logo { position: absolute; left: 56.7pt; top: 83.5pt; width: 198.2pt; }
.cover-report-type {
  position: absolute;
  left: 50.7pt;
  top: 331pt;
  padding: 6pt;
  font-family: "MiRo DIN", Arial, sans-serif;
  font-weight: 700;
  font-size: 16pt;
  line-height: 16pt;
  color: #fff;
}
.cover-name {
  position: absolute;
  left: 50.7pt;
  top: 390pt;
  font-size: 34pt;
  line-height: 38pt;
  margin: 0;
}
.cover-panel {
  position: absolute;
  left: 50.7pt;
  bottom: 28pt;
  width: 535.7pt;
  height: 100pt;
  border-radius: 50pt;
  background: var(--title-grey);
}
.cover-panel-icon { position: absolute; left: 16pt; top: 15pt; width: 70pt; height: 70pt; }
.cover-panel-text { position: absolute; left: 96pt; top: 26pt; width: 377pt; }
.cover-title {
  font-family: "MiRo DIN", Arial, sans-serif;
  font-weight: 700;
  font-size: 16pt;
  line-height: 18pt;
  padding-bottom: 6pt;
  border-bottom: 0.75pt solid var(--rule);
}
.cover-subtitle {
  font-family: "MiRo DIN", Arial, sans-serif;
  font-size: 16pt;
  line-height: 18pt;
  margin-top: 8pt;
}

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
