import { describe, expect, it } from "vitest";
import { hashPassword, isScryptHash, sha1, verifyLegacyPassword, verifyPassword } from "@/lib/password";

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

describe("hashPassword / verifyPassword", () => {
  it("produces a salted scrypt hash that verifies", async () => {
    const hash = await hashPassword("correct horse");
    expect(isScryptHash(hash)).toBe(true);
    expect(hash.length).toBeLessThanOrEqual(255);
    expect(await hashPassword("correct horse")).not.toBe(hash);
    expect(await verifyPassword("correct horse", hash)).toBe(true);
    expect(await verifyPassword("wrong horse", hash)).toBe(false);
  });

  it("does not accept the stored scrypt string as a plain-text password", async () => {
    const hash = await hashPassword("secret");
    expect(await verifyPassword(hash, hash)).toBe(false);
  });

  it("still verifies legacy sha1 and plain-text rows", async () => {
    expect(await verifyPassword("secret", sha1("secret"))).toBe(true);
    expect(await verifyPassword("secret", "secret")).toBe(true);
    expect(await verifyPassword("secret", sha1("another"))).toBe(false);
  });

  it("rejects malformed scrypt rows", async () => {
    expect(await verifyPassword("secret", "scrypt$broken")).toBe(false);
    expect(await verifyPassword("secret", "scrypt$16384$8$1$c2FsdA$")).toBe(false);
  });
});
