import { NextResponse } from "next/server";
import { z } from "zod";
import { auth } from "@/auth";
import { prisma } from "@/lib/prisma";
import { getNextHibernateId } from "@/lib/ids";

const PROJECT_SCHEMA = z.object({
  projectTitle: z.string().min(1).max(100),
  projectDescription: z.string().min(1).max(254),
  costcode: z.string().min(1).max(50),
  emailInviteSubject: z.string().min(1).max(50),
  emailInviteText: z.string().min(1).max(100),
});

export async function POST(request) {
  const session = await auth();

  if (!session?.user?.id) {
    return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
  }

  const body = await request.json().catch(() => null);
  const parsed = PROJECT_SCHEMA.safeParse(body);

  if (!parsed.success) {
    return NextResponse.json({ error: "Invalid payload" }, { status: 400 });
  }

  const userId = BigInt(session.user.id);
  const now = new Date();

  const project = await prisma.$transaction(async (tx) => {
    const id = await getNextHibernateId(tx);

    return tx.miroProject.create({
      data: {
        id,
        version: 0,
        projectStatus: 0,
        bccPractitioner: "0",
        checkPoint: 0n,
        createdById: userId,
        createdOn: now,
        lastUpdatedById: userId,
        updatedAt: now,
        ...parsed.data,
      },
      select: {
        id: true,
      },
    });
  });

  return NextResponse.json({ id: project.id.toString() });
}
