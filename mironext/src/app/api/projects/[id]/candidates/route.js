import { NextResponse } from "next/server";
import { z } from "zod";
import { auth } from "@/auth";
import { prisma } from "@/lib/prisma";
import { createLegacyPassword, sha1 } from "@/lib/password";
import { allocateLegacyId } from "@/lib/server/ids";
import { sendAssessmentInvite } from "@/lib/assessment/service";
import { appOrigin } from "@/lib/server/route-helpers";
import { decodeProjectId, encodeCandidateId } from "@/lib/public-ids";
import { Prisma } from "@prisma/client";

const CANDIDATE_SCHEMA = z.object({
  firstName: z.string().min(1).max(50),
  lastName: z.string().min(1).max(50),
  email: z.string().email().max(255),
  sendInvite: z.boolean().optional(),
});

function createUsername(projectId, firstName, lastName) {
  const safe = `${projectId}${firstName}${lastName}`
    .toLowerCase()
    .replace(/[^a-z0-9]/g, "")
    .slice(0, 28);
  return `${safe}${Date.now().toString(36).slice(-6)}`;
}

export async function POST(request, { params }) {
  const session = await auth();
  const routeParams = await params;

  if (!session?.user?.id || !session?.user?.accountId) {
    return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
  }

  const projectId = decodeProjectId(routeParams.id);
  if (!projectId) {
    return NextResponse.json({ error: "Invalid project id" }, { status: 400 });
  }

  const body = await request.json().catch(() => null);
  const parsed = CANDIDATE_SCHEMA.safeParse(body);

  if (!parsed.success) {
    return NextResponse.json({ error: "Invalid payload" }, { status: 400 });
  }

  const userId = BigInt(session.user.id);
  const accountId = BigInt(session.user.accountId);
  const firstName = parsed.data.firstName.trim();
  const lastName = parsed.data.lastName.trim();
  const email = parsed.data.email.trim().toLowerCase();

  const project = await prisma.miroProject.findFirst({
    where: {
      id: projectId,
      createdById: userId,
    },
    select: { id: true },
  });

  if (!project) {
    return NextResponse.json({ error: "Project not found" }, { status: 404 });
  }

  const duplicateInProject = await prisma.appUser.findFirst({
    where: {
      projectId,
      deleted: { not: true },
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

  const now = new Date();
  // Candidates sign in with their emailed assessment link, so the password is
  // random and never shown (the legacy app stored it in password_hint).
  const unusablePassword = createLegacyPassword() + createLegacyPassword();
  const username = createUsername(projectId.toString(), firstName, lastName);

  try {
    const candidate = await prisma.$transaction(async (tx) => {
      const id = await allocateLegacyId(tx, "public.app_user");

      return tx.appUser.create({
        data: {
          id,
          version: 0,
          userType: "DEFAULT",
          checkPoint: 0n,
          updatedAt: now,
          createdById: userId,
          status: 0,
          creditBalance: 0,
          projectId,
          // Legacy schema requires response_id NOT NULL; 0 means no response yet.
          responseId: 0n,
          deleted: false,
          employeeRef: "N/A",
          pinNumber: "N/A",
          accountId,
          username,
          password: sha1(unusablePassword),
          passwordHint: null,
          firstName,
          lastName,
          email,
          enabled: "1",
          gdpr: "0",
          accountExpired: "0",
          accountLocked: "0",
          credentialsExpired: "0",
          createdOn: now,
          oldreport: false,
        },
        select: {
          id: true,
        },
      });
    });

    if (!parsed.data.sendInvite) {
      return NextResponse.json({ id: encodeCandidateId(candidate.id) });
    }
    try {
      const invite = await sendAssessmentInvite({
        projectId,
        candidateId: candidate.id,
        practitionerId: userId,
        origin: appOrigin(request),
      });
      return NextResponse.json({
        id: encodeCandidateId(candidate.id),
        inviteSent: invite.email.delivered,
        devInviteUrl: invite.devInviteUrl,
      });
    } catch (inviteError) {
      console.error("invite after create failed", inviteError);
      return NextResponse.json({
        id: encodeCandidateId(candidate.id),
        inviteError: "Candidate saved, but the invite could not be sent.",
      });
    }
  } catch (error) {
    if (error instanceof Prisma.PrismaClientKnownRequestError && error.code === "P2002") {
      return NextResponse.json(
        { error: "A candidate with this email already exists." },
        { status: 409 },
      );
    }
    return NextResponse.json({ error: "Unable to save candidate." }, { status: 500 });
  }
}
