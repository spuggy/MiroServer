// Pure helpers for the sysadmin report screens: classifying legacy
// mr.mirotransactions rows, date periods, monthly roll-ups, formatting, CSV.

import { TRANSACTION_OK_BUY_REPORT } from "@/lib/candidates/status";

/** mr.mirotransactions.status values (legacy MiroTransaction constants). */
export const TRANSACTION_STATUS = {
  OK: 2,
  FAILURE: 3,
  OK_PROCESSED: 4,
  FAILURE_PROCESSED: 5,
  OK_BUY_REPORT: TRANSACTION_OK_BUY_REPORT,
} as const;

export const PAID_STATUSES = [TRANSACTION_STATUS.OK, TRANSACTION_STATUS.OK_PROCESSED];
export const FAILED_STATUSES = [TRANSACTION_STATUS.FAILURE, TRANSACTION_STATUS.FAILURE_PROCESSED];

export type TransactionKind = "purchase" | "report" | "failed" | "pending";

/** Legacy status 2 means paid but the batch hasn't added the credits yet. */
export function classifyTransaction(status: number): TransactionKind {
  if (PAID_STATUSES.includes(status as 2 | 4)) return "purchase";
  if (status === TRANSACTION_STATUS.OK_BUY_REPORT) return "report";
  if (FAILED_STATUSES.includes(status as 3 | 5)) return "failed";
  return "pending";
}

export const KIND_LABELS: Record<TransactionKind, string> = {
  purchase: "Credit purchase",
  report: "Report bought",
  failed: "Failed payment",
  pending: "Not completed",
};

export function statusFilterForKind(kind: TransactionKind): { in: number[] } | { notIn: number[] } {
  if (kind === "purchase") return { in: PAID_STATUSES };
  if (kind === "report") return { in: [TRANSACTION_STATUS.OK_BUY_REPORT] };
  if (kind === "failed") return { in: FAILED_STATUSES };
  return {
    notIn: [...PAID_STATUSES, ...FAILED_STATUSES, TRANSACTION_STATUS.OK_BUY_REPORT],
  };
}

export function isTransactionKind(value: unknown): value is TransactionKind {
  return typeof value === "string" && Object.prototype.hasOwnProperty.call(KIND_LABELS, value);
}

// ---- periods ----------------------------------------------------------------

export const RANGES = {
  "30d": "Last 30 days",
  "90d": "Last 90 days",
  "12m": "Last 12 months",
  all: "All time",
} as const;
export type RangeKey = keyof typeof RANGES;
export const DEFAULT_RANGE: RangeKey = "12m";

export function isRangeKey(value: unknown): value is RangeKey {
  return typeof value === "string" && Object.prototype.hasOwnProperty.call(RANGES, value);
}

/** Start of the period (inclusive), or null for all time. */
export function rangeStart(range: RangeKey, now: Date = new Date()): Date | null {
  if (range === "all") return null;
  const start = new Date(now);
  if (range === "30d") start.setDate(start.getDate() - 30);
  else if (range === "90d") start.setDate(start.getDate() - 90);
  else {
    start.setDate(1);
    start.setMonth(start.getMonth() - 11);
  }
  start.setHours(0, 0, 0, 0);
  return start;
}

/** Parses a yyyy-mm-dd query value; anything else is ignored. */
export function parseDateParam(value: string | undefined | null): Date | null {
  if (!value || !/^\d{4}-\d{2}-\d{2}$/.test(value)) return null;
  const date = new Date(`${value}T00:00:00.000`);
  if (Number.isNaN(date.getTime())) return null;
  // Reject rollovers such as 2025-02-31.
  const [y, m, d] = value.split("-").map(Number);
  if (date.getFullYear() !== y || date.getMonth() !== m - 1 || date.getDate() !== d) return null;
  return date;
}

/** Exclusive end for an inclusive yyyy-mm-dd "to" date. */
export function endOfDayExclusive(date: Date): Date {
  const end = new Date(date);
  end.setDate(end.getDate() + 1);
  return end;
}

// ---- roll-ups ---------------------------------------------------------------

export interface TransactionRow {
  status: number;
  credits: number;
  transValue: number | null;
  createdOn: Date;
}

export interface MonthBucket {
  key: string; // yyyy-mm
  label: string; // e.g. "Mar 2026"
  purchases: number;
  credits: number;
  revenuePence: number;
  reports: number;
  failed: number;
}

export function monthKey(date: Date): string {
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, "0")}`;
}

/** Money is a Float in the legacy table; add it up in whole pence to avoid drift. */
export function toPence(value: number | null | undefined): number {
  return Math.round((value ?? 0) * 100);
}

/** One bucket per month for the `months` months up to and including `now`, oldest first. */
export function monthlyBuckets(
  rows: TransactionRow[],
  months: number,
  now: Date = new Date(),
): MonthBucket[] {
  const buckets: MonthBucket[] = [];
  const byKey = new Map<string, MonthBucket>();
  for (let i = months - 1; i >= 0; i -= 1) {
    const d = new Date(now.getFullYear(), now.getMonth() - i, 1);
    const bucket: MonthBucket = {
      key: monthKey(d),
      label: d.toLocaleDateString("en-GB", { month: "short", year: "numeric" }),
      purchases: 0,
      credits: 0,
      revenuePence: 0,
      reports: 0,
      failed: 0,
    };
    buckets.push(bucket);
    byKey.set(bucket.key, bucket);
  }

  for (const row of rows) {
    const bucket = byKey.get(monthKey(row.createdOn));
    if (!bucket) continue;
    const kind = classifyTransaction(row.status);
    if (kind === "purchase") {
      bucket.purchases += 1;
      bucket.credits += row.credits;
      bucket.revenuePence += toPence(row.transValue);
    } else if (kind === "report") {
      bucket.reports += 1;
    } else if (kind === "failed") {
      bucket.failed += 1;
    }
  }
  return buckets;
}

// ---- formatting -------------------------------------------------------------

/** Currency is GBP (legacy action-servlet.xml `currency`). */
export function formatPence(pence: number): string {
  return new Intl.NumberFormat("en-GB", { style: "currency", currency: "GBP" }).format(pence / 100);
}

export function formatMoney(value: number | null | undefined): string {
  return formatPence(toPence(value));
}

export function formatDate(date: Date | null | undefined): string {
  if (!date) return "—";
  return date.toLocaleDateString("en-GB", { day: "numeric", month: "short", year: "numeric" });
}

export function formatDateTime(date: Date | null | undefined): string {
  if (!date) return "—";
  return `${formatDate(date)} ${date.toLocaleTimeString("en-GB", { hour: "2-digit", minute: "2-digit" })}`;
}

// ---- CSV --------------------------------------------------------------------

/** Quotes a field per RFC 4180, and defuses spreadsheet formula injection. */
export function csvField(value: unknown): string {
  let text = value == null ? "" : String(value);
  // Names and payment details are user-supplied; stop Excel evaluating them.
  if (typeof value === "string" && /^[=+\-@\t\r]/.test(text)) text = `'${text}`;
  return /[",\r\n]/.test(text) ? `"${text.replace(/"/g, '""')}"` : text;
}

export function toCsv(header: string[], rows: unknown[][]): string {
  return [header, ...rows].map((row) => row.map(csvField).join(",")).join("\r\n") + "\r\n";
}
