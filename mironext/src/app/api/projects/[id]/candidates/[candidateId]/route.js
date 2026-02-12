import { NextResponse } from "next/server";
import { z } from "zod";
import { Prisma } from "@prisma/client";
import { auth } from "@/auth";
import { prisma } from "@/lib/prisma";

const UPDATE_CANDIDATE_SCHEMA = z.object({
  firstName: z.string().min(1).max(50),
  lastName: z.string().min(1).max(50),
  email: z.string().email().max(255),
});

function parseBigIntParam(value) {
  if (typeof value !== "string" || value.trim().length === 0) {
    return null;
  }

  try {
    return BigInt(value);
  } catch {
    return null;
  }
}

async function findProjectForUser(projectId, userId) {
  return prisma.miroProject.findFirst({
    where: {
      id: projectId,
      createdById: userId,
      OR: [{ projectStatus: null }, { projectStatus: { not: 3 } }],
    },
    select: {
      id: true,
    },
  });
}

async function findCandidate(projectId, candidateId) {
  return prisma.appUser.findFirst({
    where: {
      id: candidateId,
      projectId,
      deleted: { not: true },
    },
    select: {
      id: true,
      firstName: true,
      lastName: true,
      email: true,
      status: true,
      updatedAt: true,
      createdOn: true,
      response: {
        select: {
          surveyId: true,
        },
      },
    },
  });
}

async function getRouteContext(params) {
  const session = await auth();
  const routeParams = await params;

  if (!session?.user?.id) {
    return { error: NextResponse.json({ error: "Unauthorized" }, { status: 401 }) };
  }

  const userId = parseBigIntParam(session.user.id);
  const projectId = parseBigIntParam(routeParams.id);
  const candidateId = parseBigIntParam(routeParams.candidateId);

  if (!userId || !projectId || !candidateId) {
    return { error: NextResponse.json({ error: "Invalid route params" }, { status: 400 }) };
  }

  const project = await findProjectForUser(projectId, userId);
  if (!project) {
    return { error: NextResponse.json({ error: "Project not found" }, { status: 404 }) };
  }

  return { userId, projectId, candidateId };
}

export async function GET(_request, { params }) {
  const context = await getRouteContext(params);
  if (context.error) {
    return context.error;
  }

  const candidate = await findCandidate(context.projectId, context.candidateId);
  if (!candidate) {
    return NextResponse.json({ error: "Candidate not found" }, { status: 404 });
  }

  return NextResponse.json({
    id: candidate.id.toString(),
    firstName: candidate.firstName,
    lastName: candidate.lastName,
    email: candidate.email,
    status: candidate.status,
    surveyId: candidate.response?.surveyId?.toString() || null,
    createdOn: candidate.createdOn?.toISOString() || null,
    updatedAt: candidate.updatedAt?.toISOString() || null,
  });
}

export async function PATCH(request, { params }) {
  const context = await getRouteContext(params);
  if (context.error) {
    return context.error;
  }

  const body = await request.json().catch(() => null);
  const parsed = UPDATE_CANDIDATE_SCHEMA.safeParse(body);

  if (!parsed.success) {
    return NextResponse.json({ error: "Invalid payload" }, { status: 400 });
  }

  const firstName = parsed.data.firstName.trim();
  const lastName = parsed.data.lastName.trim();
  const email = parsed.data.email.trim().toLowerCase();

  if (!firstName || !lastName || !email) {
    return NextResponse.json(
      { error: "First name, last name and email are required." },
      { status: 400 },
    );
  }

  const candidate = await findCandidate(context.projectId, context.candidateId);
  if (!candidate) {
    return NextResponse.json({ error: "Candidate not found" }, { status: 404 });
  }

  const duplicateInProject = await prisma.appUser.findFirst({
    where: {
      projectId: context.projectId,
      deleted: { not: true },
      NOT: { id: context.candidateId },
      OR: [
        { email: { equals: email, mode: "insensitive" } },
        {
          AND: [
            { firstName: { equals: firstName, mode: "insensitive" } },
            { lastName: { equals: lastName, mode: "insensitive" } },
          ],
        },
      ],
    },
    select: { id: true },
  });

  if (duplicateInProject) {
    return NextResponse.json(
      { error: "A candidate with this name or email already exists for this project." },
      { status: 409 },
    );
  }

  try {
    await prisma.appUser.update({
      where: { id: context.candidateId },
      data: {
        firstName,
        lastName,
        email,
        updatedAt: new Date(),
        lastUpdatedById: context.userId,
      },
      select: { id: true },
    });
  } catch (error) {
    if (error instanceof Prisma.PrismaClientKnownRequestError && error.code === "P2002") {
      return NextResponse.json(
        { error: "A candidate with this email already exists." },
        { status: 409 },
      );
    }
    return NextResponse.json({ error: "Unable to update candidate." }, { status: 500 });
  }

  return NextResponse.json({ ok: true });
}

export async function DELETE(_request, { params }) {
  const context = await getRouteContext(params);
  if (context.error) {
    return context.error;
  }

  const candidate = await findCandidate(context.projectId, context.candidateId);
  if (!candidate) {
    return NextResponse.json({ error: "Candidate not found" }, { status: 404 });
  }

  const now = new Date();
  const suffix = `${now.getTime()}`;
  const tombstoneUsername = `deleted_${context.candidateId.toString()}_${suffix}`.slice(0, 50);
  const tombstoneEmail = `deleted+${context.candidateId.toString()}+${suffix}@deleted.local`.slice(
    0,
    255,
  );

  await prisma.appUser.update({
    where: { id: context.candidateId },
    data: {
      deleted: true,
      enabled: "0",
      username: tombstoneUsername,
      email: tombstoneEmail,
      updatedAt: now,
      lastUpdatedById: context.userId,
    },
    select: { id: true },
  });

  return NextResponse.json({ ok: true });
}
