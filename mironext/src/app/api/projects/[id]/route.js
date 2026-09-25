import { NextResponse } from "next/server";
import { auth } from "@/auth";
import { prisma } from "@/lib/prisma";
import { decodeProjectId, encodeProjectId } from "@/lib/public-ids";
import { PROJECT_SCHEMA, toProjectData } from "@/lib/projects/schema";

export async function PUT(request, { params }) {
  const session = await auth();

  if (!session?.user?.id) {
    return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
  }

  const { id } = await params;
  const projectId = decodeProjectId(id);

  if (!projectId) {
    return NextResponse.json({ error: "Project not found" }, { status: 404 });
  }

  const body = await request.json().catch(() => null);
  const parsed = PROJECT_SCHEMA.safeParse(body);

  if (!parsed.success) {
    return NextResponse.json({ error: "Invalid payload" }, { status: 400 });
  }

  const userId = BigInt(session.user.id);
  const result = await prisma.miroProject.updateMany({
    where: {
      id: projectId,
      createdById: userId,
      OR: [{ projectStatus: null }, { projectStatus: { not: 3 } }],
    },
    data: {
      ...toProjectData(parsed.data),
      lastUpdatedById: userId,
      updatedAt: new Date(),
      version: { increment: 1 },
    },
  });

  if (result.count === 0) {
    return NextResponse.json({ error: "Project not found" }, { status: 404 });
  }

  return NextResponse.json({ id: encodeProjectId(projectId) });
}
