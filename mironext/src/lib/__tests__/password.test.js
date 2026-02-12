import { describe, expect, it } from "vitest";
import { sha1, verifyLegacyPassword } from "@/lib/password";

describe("verifyLegacyPassword", () => {
  it("accepts plain text equality", () => {
    expect(verifyLegacyPassword("secret", "secret")).toBe(true);
  });

  it("accepts sha1 encoded values", () => {
    expect(verifyLegacyPassword("secret", sha1("secret"))).toBe(true);
  });

  it("rejects non-matching values", () => {
    expect(verifyLegacyPassword("secret", sha1("another"))).toBe(false);
  });
});
