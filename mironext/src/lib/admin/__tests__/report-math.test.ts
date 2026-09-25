import { describe, expect, it } from "vitest";
import {
  classifyTransaction,
  csvField,
  endOfDayExclusive,
  formatMoney,
  monthlyBuckets,
  parseDateParam,
  rangeStart,
  statusFilterForKind,
  toCsv,
  toPence,
} from "@/lib/admin/report-math";

describe("classifyTransaction", () => {
  it("maps legacy statuses to kinds", () => {
    expect(classifyTransaction(2)).toBe("purchase");
    expect(classifyTransaction(4)).toBe("purchase");
    expect(classifyTransaction(3)).toBe("failed");
    expect(classifyTransaction(5)).toBe("failed");
    expect(classifyTransaction(6)).toBe("report");
    expect(classifyTransaction(0)).toBe("pending");
  });

  it("builds a status filter that matches the classification", () => {
    expect(statusFilterForKind("purchase")).toEqual({ in: [2, 4] });
    expect(statusFilterForKind("report")).toEqual({ in: [6] });
    expect(statusFilterForKind("failed")).toEqual({ in: [3, 5] });
    expect(statusFilterForKind("pending")).toEqual({ notIn: [2, 4, 3, 5, 6] });
  });
});

describe("periods", () => {
  const now = new Date(2026, 8, 24, 15, 30);

  it("computes range starts", () => {
    expect(rangeStart("all", now)).toBeNull();
    expect(rangeStart("30d", now)).toEqual(new Date(2026, 7, 25));
    expect(rangeStart("12m", now)).toEqual(new Date(2025, 9, 1));
  });

  it("parses only real yyyy-mm-dd dates", () => {
    expect(parseDateParam("2026-02-28")).toEqual(new Date(2026, 1, 28));
    expect(parseDateParam("2026-02-31")).toBeNull();
    expect(parseDateParam("28/02/2026")).toBeNull();
    expect(parseDateParam(undefined)).toBeNull();
  });

  it("makes an inclusive end date exclusive", () => {
    expect(endOfDayExclusive(new Date(2026, 0, 31))).toEqual(new Date(2026, 1, 1));
  });
});

describe("monthlyBuckets", () => {
  const now = new Date(2026, 8, 24);
  const row = (status: number, createdOn: Date, credits = 1, transValue: number | null = 0) => ({
    status,
    credits,
    transValue,
    createdOn,
  });

  it("returns one bucket per month, oldest first, including empty months", () => {
    const buckets = monthlyBuckets([], 3, now);
    expect(buckets.map((b) => b.key)).toEqual(["2026-07", "2026-08", "2026-09"]);
    expect(buckets.every((b) => b.revenuePence === 0)).toBe(true);
  });

  it("rolls up purchases, reports and failures per month", () => {
    const buckets = monthlyBuckets(
      [
        row(4, new Date(2026, 8, 2), 10, 100.1),
        row(2, new Date(2026, 8, 3), 5, 50.2),
        row(6, new Date(2026, 8, 4)),
        row(6, new Date(2026, 7, 4)),
        row(3, new Date(2026, 8, 5), 3, 30),
        row(0, new Date(2026, 8, 6)),
        row(4, new Date(2024, 0, 1), 99, 999), // outside the window
      ],
      2,
      now,
    );
    expect(buckets[1]).toMatchObject({
      key: "2026-09",
      purchases: 2,
      credits: 15,
      revenuePence: 15030,
      reports: 1,
      failed: 1,
    });
    expect(buckets[0]).toMatchObject({ key: "2026-08", reports: 1, purchases: 0 });
  });
});

describe("money", () => {
  it("adds in whole pence", () => {
    expect(toPence(0.1 + 0.2)).toBe(30);
    expect(toPence(null)).toBe(0);
    expect(formatMoney(1234.5)).toBe("£1,234.50");
  });
});

describe("csv", () => {
  it("quotes fields that need it", () => {
    expect(csvField('a "b", c')).toBe('"a ""b"", c"');
    expect(csvField(null)).toBe("");
  });

  it("defuses formulas in text but leaves numbers alone", () => {
    expect(csvField("=HYPERLINK(1)")).toBe("'=HYPERLINK(1)");
    expect(csvField(-3)).toBe("-3");
  });

  it("joins rows with CRLF", () => {
    expect(toCsv(["a", "b"], [[1, "x,y"]])).toBe('a,b\r\n1,"x,y"\r\n');
  });
});
