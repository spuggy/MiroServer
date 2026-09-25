import { notFound, redirect } from "next/navigation";
import { auth } from "@/auth";
import { prisma } from "@/lib/prisma";
import { parseId } from "@/lib/server/route-helpers";

/** public.role.name for the super user (legacy Constants.SYSADMIN_ROLE). */
export const SYSADMIN_ROLE = "sysadmin";

export async function isSysAdmin(userId: bigint): Promise<boolean> {
  const count = await prisma.userRole.count({
    where: { userId, role: { name: { equals: SYSADMIN_ROLE, mode: "insensitive" } } },
  });
  return count > 0;
}

// NB: ids are bigints and 0n is falsy (the legacy "tomcat" super user is id 0),
// so always compare against null, never test truthiness.

/** For pages: sends anonymous users to login and hides the area (404) from everyone else. */
export async function requireSysAdmin(): Promise<bigint> {
  const session = (await auth()) as { user?: { id?: string } } | null;
  const userId = parseId(session?.user?.id);
  if (userId === null) redirect("/login");
  if (!(await isSysAdmin(userId))) notFound();
  return userId;
}

/** For API routes: the sysadmin's id, or null if the caller isn't one. */
export async function currentSysAdminId(): Promise<bigint | null> {
  const session = (await auth()) as { user?: { id?: string } } | null;
  const userId = parseId(session?.user?.id);
  if (userId === null || !(await isSysAdmin(userId))) return null;
  return userId;
}
