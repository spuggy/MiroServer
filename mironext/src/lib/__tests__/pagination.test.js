import { describe, expect, it } from "vitest";
import { getPagination } from "@/lib/pagination";

describe("getPagination", () => {
  it("uses defaults when values are missing", () => {
    expect(getPagination({})).toEqual({ page: 1, pageSize: 10, skip: 0, take: 10 });
  });

  it("coerces invalid values", () => {
    expect(getPagination({ page: "0", pageSize: "1000" })).toEqual({
      page: 1,
      pageSize: 50,
      skip: 0,
      take: 50,
    });
  });

  it("handles valid page and pageSize", () => {
    expect(getPagination({ page: "3", pageSize: "5" })).toEqual({
      page: 3,
      pageSize: 5,
      skip: 10,
      take: 5,
    });
  });
});
