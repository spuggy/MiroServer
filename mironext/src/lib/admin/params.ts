import { decodeCustomerId } from "@/lib/public-ids";
import {
  endOfDayExclusive,
  isTransactionKind,
  parseDateParam,
  type TransactionKind,
} from "./report-math";
import type { TransactionFilters } from "./queries";

export type SearchParams = Record<string, string | string[] | undefined>;

export function firstParam(params: SearchParams | undefined, key: string): string | undefined {
  const value = params?.[key];
  return Array.isArray(value) ? value[0] : value;
}

/** Builds a href from a path and params, dropping empty values. */
export function withParams(
  path: string,
  params: Record<string, string | number | undefined | null>,
) {
  const query = new URLSearchParams();
  for (const [key, value] of Object.entries(params)) {
    if (value !== undefined && value !== null && value !== "") query.set(key, String(value));
  }
  const qs = query.toString();
  return qs ? `${path}?${qs}` : path;
}

export interface ParsedTransactionParams {
  filters: TransactionFilters;
  /** The cleaned raw values, for echoing back into forms and links. */
  values: { kind: TransactionKind | ""; from: string; to: string; q: string; customer: string };
}

/** Shared by the Transactions page and its CSV export so both filter identically. */
export function parseTransactionParams(params: SearchParams | undefined): ParsedTransactionParams {
  const kindRaw = firstParam(params, "kind");
  const kind = isTransactionKind(kindRaw) ? kindRaw : undefined;
  const fromRaw = firstParam(params, "from") ?? "";
  const toRaw = firstParam(params, "to") ?? "";
  const from = parseDateParam(fromRaw);
  const toDate = parseDateParam(toRaw);
  const q = (firstParam(params, "q") ?? "").trim().slice(0, 100);
  const customer = firstParam(params, "customer") ?? "";
  const customerId = customer ? decodeCustomerId(customer) : null;

  return {
    filters: {
      kind,
      from,
      to: toDate ? endOfDayExclusive(toDate) : null,
      customerId,
      q: q || undefined,
    },
    values: {
      kind: kind ?? "",
      from: from ? fromRaw : "",
      to: toDate ? toRaw : "",
      q,
      customer: customerId ? customer : "",
    },
  };
}
