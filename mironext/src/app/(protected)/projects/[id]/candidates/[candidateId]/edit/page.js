import Link from "next/link";
import { notFound, redirect } from "next/navigation";
import { Box, Card, CardContent, Stack, Typography } from "@mui/material";
import { auth } from "@/auth";
import { prisma } from "@/lib/prisma";
import EditCandidateForm from "@/components/projects/EditCandidateForm";

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

export default async function EditCandidatePage({ params }) {
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
        <Typography variant="h4">Edit Candidate</Typography>
        <Typography color="text.secondary">{project.projectTitle}</Typography>
      </Box>

      <Stack direction="row" gap={2}>
        <Link href={candidatePath}>Back to Candidate</Link>
        <Link href={projectPath}>Back to Project</Link>
      </Stack>

      <Card>
        <CardContent>
          <EditCandidateForm
            projectId={project.id.toString()}
            candidateId={candidate.id.toString()}
            firstName={candidate.firstName}
            lastName={candidate.lastName}
            email={candidate.email}
          />
        </CardContent>
      </Card>
    </Stack>
  );
}
