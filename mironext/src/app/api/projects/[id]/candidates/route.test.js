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

vi.mock("@/lib/ids", () => ({
  getNextHibernateId: vi.fn(),
}));

vi.mock("@/lib/password", () => ({
  createLegacyPassword: vi.fn(),
  sha1: vi.fn(),
}));

import { POST } from "./route";
import { auth } from "@/auth";
import { prisma } from "@/lib/prisma";
import { getNextHibernateId } from "@/lib/ids";
import { createLegacyPassword, sha1 } from "@/lib/password";

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
    getNextHibernateId.mockResolvedValue(999n);
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

    const request = new Request("http://localhost/api/projects/2837/candidates", {
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

    const response = await POST(request, { params: Promise.resolve({ id: "2837" }) });

    expect(response.status).toBe(200);
    await expect(response.json()).resolves.toEqual({ id: "999" });
    expect(createSpy).toHaveBeenCalledTimes(1);
    expect(createSpy).toHaveBeenCalledWith(
      expect.objectContaining({
        data: expect.objectContaining({
          responseId: 0n,
          projectId: 2837n,
          email: "ada@example.com",
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

    const request = new Request("http://localhost/api/projects/2837/candidates", {
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

    const response = await POST(request, { params: Promise.resolve({ id: "2837" }) });

    expect(response.status).toBe(409);
    await expect(response.json()).resolves.toEqual({
      error: "A candidate with this name or email already exists for this project.",
    });
    expect(prisma.$transaction).not.toHaveBeenCalled();
    expect(getNextHibernateId).not.toHaveBeenCalled();
    expect(createLegacyPassword).not.toHaveBeenCalled();
    expect(sha1).not.toHaveBeenCalled();
  });
});
