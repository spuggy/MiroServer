import { beforeEach, describe, expect, it, vi } from "vitest";

const mocks = vi.hoisted(() => ({
  auth: vi.fn(),
  roleCount: vi.fn(),
  redirect: vi.fn((to: string) => {
    throw new Error(`REDIRECT ${to}`);
  }),
  notFound: vi.fn(() => {
    throw new Error("NOT_FOUND");
  }),
}));

vi.mock("@/auth", () => ({ auth: mocks.auth }));
vi.mock("@/lib/prisma", () => ({ prisma: { userRole: { count: mocks.roleCount } } }));
vi.mock("next/navigation", () => ({ redirect: mocks.redirect, notFound: mocks.notFound }));

import { currentSysAdminId, requireSysAdmin } from "@/lib/admin/access";

beforeEach(() => vi.clearAllMocks());

describe("requireSysAdmin", () => {
  it("accepts the super user with id 0 (0n is falsy, so it must not be treated as signed out)", async () => {
    mocks.auth.mockResolvedValue({ user: { id: "0" } });
    mocks.roleCount.mockResolvedValue(1);
    await expect(requireSysAdmin()).resolves.toBe(0n);
    expect(mocks.redirect).not.toHaveBeenCalled();
  });

  it("sends anonymous users to login", async () => {
    mocks.auth.mockResolvedValue(null);
    await expect(requireSysAdmin()).rejects.toThrow("REDIRECT /login");
  });

  it("hides the area from signed-in users without the role", async () => {
    mocks.auth.mockResolvedValue({ user: { id: "7" } });
    mocks.roleCount.mockResolvedValue(0);
    await expect(requireSysAdmin()).rejects.toThrow("NOT_FOUND");
    expect(mocks.redirect).not.toHaveBeenCalled();
  });
});

describe("currentSysAdminId", () => {
  it("returns 0n for the id 0 super user and null for others", async () => {
    mocks.auth.mockResolvedValue({ user: { id: "0" } });
    mocks.roleCount.mockResolvedValue(1);
    await expect(currentSysAdminId()).resolves.toBe(0n);

    mocks.roleCount.mockResolvedValue(0);
    await expect(currentSysAdminId()).resolves.toBeNull();

    mocks.auth.mockResolvedValue(null);
    await expect(currentSysAdminId()).resolves.toBeNull();
  });
});
