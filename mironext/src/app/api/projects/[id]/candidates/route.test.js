import { beforeEach, describe, expect, it, vi } from "vitest";

vi.mock("@/auth", () => ({
  auth: vi.fn(),
}));

vi.mock("@/lib/prisma", () => ({
  prisma: {
    miroProject: {
      findFirst: vi.fn(),
    },
    appUser: {
      findFirst: vi.fn(),
    },
    $transaction: vi.fn(),
  },
}));

vi.mock("@/lib/server/ids", () => ({
  allocateLegacyId: vi.fn(),
}));

vi.mock("@/lib/assessment/service", () => ({
  sendAssessmentInvite: vi.fn(),
}));

vi.mock("@/lib/password", () => ({
  createLegacyPassword: vi.fn(),
  sha1: vi.fn(),
}));

import { POST } from "./route";
import { auth } from "@/auth";
import { prisma } from "@/lib/prisma";
import { allocateLegacyId } from "@/lib/server/ids";
import { createLegacyPassword, sha1 } from "@/lib/password";
import { encodeCandidateId, encodeProjectId } from "@/lib/public-ids";

const PROJECT = encodeProjectId(2837n);

describe("POST /api/projects/[id]/candidates", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("creates candidate rows with responseId set to 0n", async () => {
    auth.mockResolvedValue({
      user: {
        id: "7166",
        accountId: "1",
      },
    });

    prisma.miroProject.findFirst.mockResolvedValue({ id: 2837n });
    prisma.appUser.findFirst.mockResolvedValue(null);
    allocateLegacyId.mockResolvedValue(999n);
    createLegacyPassword.mockReturnValue("temp-pass");
    sha1.mockReturnValue("sha1-temp-pass");

    const createSpy = vi.fn().mockResolvedValue({ id: 999n });
    prisma.$transaction.mockImplementation(async (callback) =>
      callback({
        appUser: {
          create: createSpy,
        },
      }),
    );

    const request = new Request(`http://localhost/api/projects/${PROJECT}/candidates`, {
      method: "POST",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({
        firstName: "Ada",
        lastName: "Lovelace",
        email: "ADA@example.com",
      }),
    });

    const response = await POST(request, { params: Promise.resolve({ id: PROJECT }) });

    expect(response.status).toBe(200);
    await expect(response.json()).resolves.toEqual({ id: encodeCandidateId(999n) });
    expect(createSpy).toHaveBeenCalledTimes(1);
    expect(createSpy).toHaveBeenCalledWith(
      expect.objectContaining({
        data: expect.objectContaining({
          responseId: 0n,
          projectId: 2837n,
          email: "ada@example.com",
          passwordHint: null,
        }),
      }),
    );
  });

  it("returns 409 when a duplicate candidate exists in the project", async () => {
    auth.mockResolvedValue({
      user: {
        id: "7166",
        accountId: "1",
      },
    });

    prisma.miroProject.findFirst.mockResolvedValue({ id: 2837n });
    prisma.appUser.findFirst.mockResolvedValue({ id: 123n });

    const request = new Request(`http://localhost/api/projects/${PROJECT}/candidates`, {
      method: "POST",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({
        firstName: "Ada",
        lastName: "Lovelace",
        email: "ada@example.com",
      }),
    });

    const response = await POST(request, { params: Promise.resolve({ id: PROJECT }) });

    expect(response.status).toBe(409);
    await expect(response.json()).resolves.toEqual({
      error: "A candidate with this name or email already exists for this project.",
    });
    expect(prisma.$transaction).not.toHaveBeenCalled();
    expect(allocateLegacyId).not.toHaveBeenCalled();
    expect(createLegacyPassword).not.toHaveBeenCalled();
    expect(sha1).not.toHaveBeenCalled();
  });

  it("rejects numeric project ids", async () => {
    auth.mockResolvedValue({ user: { id: "7166", accountId: "1" } });

    const request = new Request("http://localhost/api/projects/2837/candidates", {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify({ firstName: "Ada", lastName: "Lovelace", email: "ada@example.com" }),
    });

    const response = await POST(request, { params: Promise.resolve({ id: "2837" }) });

    expect(response.status).toBe(400);
    expect(prisma.miroProject.findFirst).not.toHaveBeenCalled();
  });
});
