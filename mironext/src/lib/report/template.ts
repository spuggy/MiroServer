// Port of MiroReportFileGenerator: builds the per-candidate report document by
// copying sections out of the master XHTML template (mirosource11.xhtml).
//
// Output mirrors the legacy out/<name>.xhtml: <html><body><div id="0">…</div>…
// with variables written into elements whose id matches, and image srcs set
// to asset names (or "chart:pie" for the generated chart).

import { readFileSync } from "node:fs";
import { DOMParser, XMLSerializer, type Document, type Element } from "@xmldom/xmldom";
import type { ReportPage } from "./miro-report";

const ELEMENT_NODE = 1;

const templateCache = new Map<string, Document>();

export function loadTemplate(path: string): Document {
  let doc = templateCache.get(path);
  if (!doc) {
    doc = new DOMParser().parseFromString(readFileSync(path, "utf8"), "text/xml");
    templateCache.set(path, doc);
  }
  return doc;
}

export interface AssembleOptions {
  pages: ReportPage[];
  variables: Record<string, string>;
  /** element id → image reference (asset file name or "chart:pie"). */
  images: Record<string, string>;
}

export interface AssembledReport {
  document: Document;
  /** Section ids that were requested but not found in the template. */
  missingSections: string[];
}

export function assembleReport(template: Document, options: AssembleOptions): AssembledReport {
  const dest = new DOMParser().parseFromString("<html><body/></html>", "text/xml");
  const body = dest.getElementsByTagName("body")[0];
  const missingSections: string[] = [];

  options.pages.forEach((page, pageIndex) => {
    const div = dest.createElement("div");
    div.setAttribute("id", String(pageIndex));
    let hasContent = false;

    for (const sectionId of page) {
      const section = template.getElementById(sectionId);
      if (!section) {
        missingSections.push(sectionId);
        continue;
      }
      hasContent = true;
      for (let i = 0; i < section.childNodes.length; i++) {
        const node = dest.importNode(section.childNodes[i], true);
        applyVariables(node as Element, options);
        div.appendChild(node);
      }
    }
    // Java only appends a page div when at least one section was found.
    if (hasContent) body.appendChild(div);
  });

  return { document: dest, missingSections };
}

function applyVariables(node: Element, options: AssembleOptions) {
  if (!node || node.nodeType !== ELEMENT_NODE) return;
  const id = node.getAttribute("id");
  if (id) {
    const img = options.images[id];
    if (img !== undefined) node.setAttribute("src", img);
    const value = options.variables[id];
    if (value !== undefined) node.textContent = value;
  }
  for (let i = 0; i < node.childNodes.length; i++) {
    applyVariables(node.childNodes[i] as Element, options);
  }
}

export function serializeReport(doc: Document): string {
  return new XMLSerializer().serializeToString(doc);
}
