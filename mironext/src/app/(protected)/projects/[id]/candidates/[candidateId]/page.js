import Link from "next/link";
import { notFound, redirect } from "next/navigation";
import { Box, Card, CardContent, Stack, Typography } from "@mui/material";
import { auth } from "@/auth";
import { prisma } from "@/lib/prisma";
import { toCandidateStatusLabel } from "@/lib/status";

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

export default async function CandidateDetailPage({ params }) {
  const session = await auth();
  if (!session?.user?.id) {
    redirect("/login");
  }

  const routeParams = await params;
  const userId = parseBigIntParam(session.user.id);
  const projectId = parseBigIntParam(routeParams.id);
  const candidateId = parseBigIntParam(routeParams.candidateId);

  if (!userId || !projectId || !candidateId) {
    notFound();
  }

  const project = await prisma.miroProject.findFirst({
    where: {
      id: projectId,
      createdById: userId,
      OR: [{ projectStatus: null }, { projectStatus: { not: 3 } }],
    },
    select: {
      id: true,
      projectTitle: true,
    },
  });

  if (!project) {
    notFound();
  }

  const candidate = await prisma.appUser.findFirst({
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
      createdOn: true,
      updatedAt: true,
      response: {
        select: {
          surveyId: true,
        },
      },
    },
  });

  if (!candidate) {
    notFound();
  }

  const projectPath = `/projects/${project.id.toString()}`;
  const candidatePath = `${projectPath}/candidates/${candidate.id.toString()}`;

  return (
    <Stack spacing={2}>
      <Box>
        <Typography variant="h4">{`${candidate.firstName} ${candidate.lastName}`}</Typography>
        <Typography color="text.secondary">{project.projectTitle}</Typography>
      </Box>

      <Stack direction="row" gap={2}>
        <Link href={projectPath}>Back to Project</Link>
        <Link href={`${candidatePath}/edit`}>Edit candidate</Link>
        <Link href={`${candidatePath}/delete`}>Delete candidate</Link>
      </Stack>

      <Card>
        <CardContent>
          <Stack spacing={1.5}>
            <Typography>
              <strong>Email:</strong> {candidate.email}
            </Typography>
            <Typography>
              <strong>Status:</strong> {toCandidateStatusLabel(candidate.status)}
            </Typography>
            <Typography>
              <strong>Survey:</strong> {candidate.response?.surveyId?.toString() || "-"}
            </Typography>
            <Typography>
              <strong>Created:</strong> {candidate.createdOn?.toLocaleString() || "-"}
            </Typography>
            <Typography>
              <strong>Last Updated:</strong> {candidate.updatedAt?.toLocaleString() || "-"}
            </Typography>
          </Stack>
        </CardContent>
      </Card>
    </Stack>
  );
}
