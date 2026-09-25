import { NextResponse } from "next/server";
import { auth } from "@/auth";
import { prisma } from "@/lib/prisma";
import { allocateLegacyId } from "@/lib/server/ids";
import { encodeProjectId } from "@/lib/public-ids";
import { PROJECT_SCHEMA, toProjectData } from "@/lib/projects/schema";

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
    const id = await allocateLegacyId(tx, "mr.miroprojects");

    return tx.miroProject.create({
      data: {
        id,
        version: 0,
        projectStatus: 0,
        checkPoint: 0n,
        createdById: userId,
        createdOn: now,
        lastUpdatedById: userId,
        updatedAt: now,
        ...toProjectData(parsed.data),
      },
      select: {
        id: true,
      },
    });
  });

  return NextResponse.json({ id: encodeProjectId(project.id) });
}
