import { describe, expect, it } from "vitest";
import {
  decodeCandidateId,
  decodeCustomerId,
  decodeProjectId,
  encodeCandidateId,
  encodeCustomerId,
  encodeProjectId,
} from "@/lib/public-ids";

describe("public ids", () => {
  it("round-trips ids as opaque 22-character tokens", () => {
    for (const id of [1n, 1732n, 2837n, 9007199254740993n]) {
      const token = encodeProjectId(id);
      expect(token).toMatch(/^[A-Za-z0-9_-]{22}$/);
      expect(token).not.toContain(id.toString());
      expect(decodeProjectId(token)).toBe(id);
    }
    expect(decodeCandidateId(encodeCandidateId("88"))).toBe(88n);
  });

  it("gives unrelated tokens for consecutive ids", () => {
    const a = encodeProjectId(1732n);
    const b = encodeProjectId(1733n);
    expect(a.slice(0, 8)).not.toBe(b.slice(0, 8));
  });

  it("does not accept a project token as a candidate id", () => {
    expect(decodeCandidateId(encodeProjectId(1732n))).toBeNull();
    expect(decodeProjectId(encodeCandidateId(1732n))).toBeNull();
    expect(decodeCustomerId(encodeCustomerId(7n))).toBe(7n);
    expect(decodeCustomerId(encodeProjectId(7n))).toBeNull();
    expect(decodeProjectId(encodeCustomerId(7n))).toBeNull();
  });

  it("rejects raw numbers, garbage and tampered tokens", () => {
    expect(decodeProjectId("1732")).toBeNull();
    expect(decodeProjectId("")).toBeNull();
    expect(decodeProjectId(undefined)).toBeNull();
    expect(decodeProjectId("not-a-real-token-at-all")).toBeNull();
    const token = encodeProjectId(1732n);
    const tampered = (token[0] === "A" ? "B" : "A") + token.slice(1);
    expect(decodeProjectId(tampered)).toBeNull();
  });
});
