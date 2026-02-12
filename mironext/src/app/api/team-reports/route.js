import { NextResponse } from "next/server";
import { z } from "zod";
import { auth } from "@/auth";
import { prisma } from "@/lib/prisma";
import { buildTeamReport, parseProjectId } from "@/lib/team-reports";

const TEAM_REPORT_SCHEMA = z.object({
  projectIds: z
    .array(z.union([z.string(), z.number(), z.bigint()]))
    .min(1)
    .max(100),
});

function toUniqueProjectIds(values) {
  const seen = new Set();
  const ids = [];

  for (const rawValue of values) {
    const parsed = parseProjectId(rawValue);
    if (!parsed) {
      return null;
    }

    const key = parsed.toString();
    if (!seen.has(key)) {
      seen.add(key);
      ids.push(parsed);
    }
  }

  return ids;
}

export async function POST(request) {
  const session = await auth();

  if (!session?.user?.id) {
    return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
  }

  const body = await request.json().catch(() => null);
  const parsed = TEAM_REPORT_SCHEMA.safeParse(body);

  if (!parsed.success) {
    return NextResponse.json({ error: "Invalid payload" }, { status: 400 });
  }

  const projectIds = toUniqueProjectIds(parsed.data.projectIds);
  if (!projectIds || projectIds.length === 0) {
    return NextResponse.json({ error: "Invalid project ids" }, { status: 400 });
  }

  const userId = BigInt(session.user.id);
  const projects = await prisma.miroProject.findMany({
    where: {
      id: { in: projectIds },
      createdById: userId,
      OR: [{ projectStatus: null }, { projectStatus: { not: 3 } }],
    },
    orderBy: [{ projectTitle: "asc" }, { id: "asc" }],
    select: {
      id: true,
      projectTitle: true,
      projectDescription: true,
      updatedAt: true,
    },
  });

  if (projects.length !== projectIds.length) {
    return NextResponse.json({ error: "One or more projects were not found." }, { status: 404 });
  }

  const candidateCounts = await prisma.appUser.groupBy({
    by: ["projectId"],
    where: {
      projectId: { in: projectIds },
      deleted: { not: true },
    },
    _count: {
      _all: true,
    },
  });

  const candidateCountsByProjectId = Object.create(null);
  for (const row of candidateCounts) {
    if (row.projectId) {
      candidateCountsByProjectId[row.projectId.toString()] = row._count._all;
    }
  }

  const report = buildTeamReport({
    projects,
    candidateCountsByProjectId,
    createdAt: new Date(),
  });

  return NextResponse.json(report);
}
