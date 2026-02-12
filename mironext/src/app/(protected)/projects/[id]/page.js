import { notFound, redirect } from "next/navigation";
import Link from "next/link";
import {
  Box,
  Button,
  Card,
  CardContent,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TextField,
  Typography,
} from "@mui/material";
import { auth } from "@/auth";
import { prisma } from "@/lib/prisma";
import { toCandidateStatusLabel } from "@/lib/status";
import AddCandidateDialog from "@/components/projects/AddCandidateDialog";

export default async function ProjectPage({ params, searchParams }) {
  const session = await auth();
  if (!session?.user?.id) {
    redirect("/login");
  }
  const userId = BigInt(session.user.id);
  const routeParams = await params;

  let projectId;
  try {
    projectId = BigInt(routeParams.id);
  } catch {
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
      projectDescription: true,
      emailInviteSubject: true,
      emailInviteText: true,
    },
  });

  if (!project) {
    notFound();
  }

  const qRaw = Array.isArray(searchParams?.q) ? searchParams.q[0] : searchParams?.q;
  const q = qRaw?.trim() || "";

  const candidates = await prisma.appUser.findMany({
    where: {
      projectId,
      deleted: { not: true },
      ...(q
        ? {
            OR: [
              { firstName: { contains: q, mode: "insensitive" } },
              { lastName: { contains: q, mode: "insensitive" } },
            ],
          }
        : {}),
    },
    orderBy: {
      id: "desc",
    },
    select: {
      id: true,
      firstName: true,
      lastName: true,
      email: true,
      status: true,
      response: {
        select: {
          surveyId: true,
        },
      },
    },
  });

  return (
    <Stack spacing={2}>
      <Stack direction="row" justifyContent="space-between" alignItems="center" gap={2}>
        <Box>
          <Typography variant="h4">{project.projectTitle}</Typography>
          <Typography variant="body2" color="text.secondary">
            {project.projectDescription}
          </Typography>
        </Box>
        <AddCandidateDialog projectId={project.id.toString()} />
      </Stack>

      <Stack direction="row" justifyContent="space-between" alignItems="center" gap={2}>
        <Link href="/projects">Back to Projects</Link>
        <Box component="form" method="get" sx={{ display: "flex", gap: 1 }}>
          <TextField name="q" defaultValue={q} size="small" label="Filter by name" />
          <Button type="submit" variant="outlined">
            Filter
          </Button>
        </Box>
      </Stack>

      <Card>
        <Box sx={{ px: 2, py: 1, borderBottom: "1px solid #e5e5e5", backgroundColor: "#fafafa" }}>
          <Typography variant="h6" sx={{ color: "#6b6b6b" }}>
            Candidates
          </Typography>
        </Box>
        <CardContent>
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>Name</TableCell>
                <TableCell>Email</TableCell>
                <TableCell>Status</TableCell>
                <TableCell>Survey</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {candidates.map((candidate) => (
                <TableRow key={candidate.id.toString()} hover>
                  <TableCell>{`${candidate.firstName} ${candidate.lastName}`}</TableCell>
                  <TableCell>{candidate.email}</TableCell>
                  <TableCell>{toCandidateStatusLabel(candidate.status)}</TableCell>
                  <TableCell>{candidate.response?.surveyId?.toString() || "-"}</TableCell>
                </TableRow>
              ))}
              {candidates.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={4}>No candidates found.</TableCell>
                </TableRow>
              ) : null}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </Stack>
  );
}
