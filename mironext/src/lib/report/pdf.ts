// HTML → PDF with headless Chromium. Paged.js (inlined in the HTML) lays the
// document out into pages first; we wait for it, then print.
//
// One Chromium is shared by every request (each report gets its own short-lived
// browser context), at most MAX_CONCURRENT reports render at once, and a
// crashed browser is replaced on the next request.

import type { Browser } from "playwright";

const MAX_CONCURRENT = Number(process.env.PDF_MAX_CONCURRENCY || 2);

interface RendererState {
  browserPromise: Promise<Browser> | null;
  active: number;
  waiting: (() => void)[];
}

// Kept on globalThis so Next.js dev hot-reloads don't leak extra browsers.
const g = globalThis as unknown as { __miroPdfRenderer?: RendererState };
const state: RendererState = (g.__miroPdfRenderer ??= {
  browserPromise: null,
  active: 0,
  waiting: [],
});

async function getBrowser(): Promise<Browser> {
  if (!state.browserPromise) {
    const launching = import("playwright").then(({ chromium }) =>
      chromium.launch({ args: ["--font-render-hinting=none"] }),
    );
    state.browserPromise = launching;
    launching.then(
      (browser) =>
        browser.on("disconnected", () => {
          if (state.browserPromise === launching) state.browserPromise = null;
        }),
      () => {
        if (state.browserPromise === launching) state.browserPromise = null;
      },
    );
  }
  return state.browserPromise;
}

async function acquireSlot(): Promise<void> {
  if (state.active < MAX_CONCURRENT) {
    state.active++;
    return;
  }
  await new Promise<void>((resolve) => state.waiting.push(resolve));
}

function releaseSlot() {
  const next = state.waiting.shift();
  if (next) next();
  else state.active--;
}

export interface PdfResult {
  pdf: Buffer;
  pageCount: number;
}

export async function htmlToPdf(html: string, timeoutMs = 60_000): Promise<PdfResult> {
  await acquireSlot();
  try {
    const browser = await getBrowser();
    const context = await browser.newContext();
    try {
      const page = await context.newPage();
      await page.setContent(html, { waitUntil: "load", timeout: timeoutMs });
      const handle = await page.waitForFunction(
        () => (window as unknown as { __MIRO_PAGED__?: { pages: number } }).__MIRO_PAGED__,
        undefined,
        { timeout: timeoutMs },
      );
      const { pages } = (await handle.jsonValue()) as { pages: number };
      const pdf = await page.pdf({ preferCSSPageSize: true, printBackground: true });
      return { pdf, pageCount: pages };
    } finally {
      await context.close().catch(() => {});
    }
  } finally {
    releaseSlot();
  }
}

/** Close the shared browser (tests, scripts, graceful shutdown). */
export async function closePdfRenderer(): Promise<void> {
  const pending = state.browserPromise;
  state.browserPromise = null;
  const browser = await pending?.catch(() => null);
  await browser?.close();
}
