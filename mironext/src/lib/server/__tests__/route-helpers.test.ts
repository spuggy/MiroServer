import { afterEach, describe, expect, it, vi } from "vitest";

vi.mock("@/auth", () => ({ auth: vi.fn() }));

import { appOrigin } from "../route-helpers";

const request = new Request("http://localhost:8080/api/password-reset");

describe("appOrigin", () => {
  afterEach(() => vi.unstubAllEnvs());

  it("prefers APP_URL", () => {
    vi.stubEnv("APP_URL", "https://app.example.com");
    vi.stubEnv("RAILWAY_PUBLIC_DOMAIN", "dev.miro-assessment.com");
    expect(appOrigin(request)).toBe("https://app.example.com");
  });

  it("falls back to the Railway public domain", () => {
    vi.stubEnv("APP_URL", "");
    vi.stubEnv("RAILWAY_PUBLIC_DOMAIN", "dev.miro-assessment.com");
    expect(appOrigin(request)).toBe("https://dev.miro-assessment.com");
  });

  it("falls back to the request origin", () => {
    vi.stubEnv("APP_URL", "");
    vi.stubEnv("RAILWAY_PUBLIC_DOMAIN", "");
    expect(appOrigin(request)).toBe("http://localhost:8080");
  });
});
