import { notFound, redirect } from "next/navigation";
import Link from "next/link";
import {
  Box,
  Card,
  InputAdornment,
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
import { isEnabledFlagValue } from "@/lib/candidates/status";
import { decodeProjectId, encodeCandidateId, encodeProjectId } from "@/lib/public-ids";
import ProjectDialog from "@/components/projects/ProjectDialog";
import AddCandidateDialog from "@/components/projects/AddCandidateDialog";
import CandidateActionsMenu from "@/components/projects/CandidateActionsMenu";
import CandidateReportActions from "@/components/projects/CandidateReportActions";
import CandidateStatusChip from "@/components/projects/CandidateStatusChip";
import SearchIcon from "@mui/icons-material/Search";

export default async function ProjectPage({ params, searchParams }) {
  const session = await auth();
  if (!session?.user?.id) {
    redirect("/login");
  }
  const userId = BigInt(session.user.id);
  const routeParams = await params;
  const queryParams = await searchParams;

  const projectId = decodeProjectId(routeParams.id);
  if (!projectId) {
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
      costcode: true,
      bccPractitioner: true,
      createdOn: true,
    },
  });

  if (!project) {
    notFound();
  }

  const qRaw = Array.isArray(queryParams?.q) ? queryParams.q[0] : queryParams?.q;
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
      createdOn: true,
      updatedAt: true,
      response: {
        select: {
          surveyId: true,
        },
      },
    },
  });

  const publicProjectId = encodeProjectId(project.id);
  const candidateCount = candidates.length;
  const created = project.createdOn
    ? project.createdOn.toLocaleDateString("en-GB", {
        day: "numeric",
        month: "long",
        year: "numeric",
      })
    : null;

  return (
    <Stack spacing={3}>
      <Stack spacing={0.75}>
        <Link href="/projects" style={{ fontSize: 14, fontWeight: 500 }}>
          ← All projects
        </Link>
        <Stack
          direction={{ xs: "column", sm: "row" }}
          justifyContent="space-between"
          alignItems={{ xs: "flex-start", sm: "flex-end" }}
          gap={2}
        >
          <Box>
            <Typography variant="h1">{project.projectTitle}</Typography>
            <Typography sx={{ mt: 0.75, color: "text.secondary" }}>
              {[
                project.projectDescription,
                q
                  ? `${candidateCount} matching`
                  : `${candidateCount} ${candidateCount === 1 ? "candidate" : "candidates"}`,
                created ? `created ${created}` : null,
              ]
                .filter(Boolean)
                .join(" · ")}
            </Typography>
          </Box>
          <Stack direction="row" gap={1.5}>
            <ProjectDialog
              projectId={publicProjectId}
              practitionerEmail={session.user.email}
              initialValues={{
                projectTitle: project.projectTitle,
                projectDescription: project.projectDescription,
                costcode: project.costcode,
                emailInviteSubject: project.emailInviteSubject,
                emailInviteText: project.emailInviteText,
                bccPractitioner: isEnabledFlagValue(project.bccPractitioner),
              }}
            />
            <AddCandidateDialog projectId={publicProjectId} />
          </Stack>
        </Stack>
      </Stack>

      <Card>
        <Stack
          direction="row"
          justifyContent="space-between"
          alignItems="center"
          gap={2}
          sx={{ px: 2.5, py: 2, borderBottom: "1px solid", borderColor: "divider" }}
        >
          <Typography variant="h6" component="h2">
            Candidates
          </Typography>
          <Box
            component="form"
            method="get"
            role="search"
            sx={{ display: "flex", alignItems: "center", gap: 1 }}
          >
            <TextField
              name="q"
              defaultValue={q}
              placeholder="Filter by name"
              inputProps={{ "aria-label": "Filter candidates by name" }}
              sx={{ width: { xs: 180, sm: 300 } }}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <SearchIcon fontSize="small" />
                  </InputAdornment>
                ),
              }}
            />
            {q ? (
              <Link href={`/projects/${publicProjectId}`} style={{ fontSize: 14, fontWeight: 500 }}>
                Clear
              </Link>
            ) : null}
          </Box>
        </Stack>

        <Table>
          <TableHead>
            <TableRow>
              <TableCell sx={{ pl: 2.5 }}>Name</TableCell>
              <TableCell sx={{ display: { xs: "none", md: "table-cell" } }}>Email</TableCell>
              <TableCell>Status</TableCell>
              <TableCell align="right">Action</TableCell>
              <TableCell sx={{ width: 56 }} />
            </TableRow>
          </TableHead>
          <TableBody>
            {candidates.map((candidate) => (
              <TableRow key={encodeCandidateId(candidate.id)} hover>
                <TableCell sx={{ pl: 2.5, fontWeight: 600 }}>
                  {`${candidate.firstName} ${candidate.lastName}`}
                </TableCell>
                <TableCell
                  sx={{ color: "text.secondary", display: { xs: "none", md: "table-cell" } }}
                >
                  {candidate.email}
                </TableCell>
                <TableCell>
                  <CandidateStatusChip status={candidate.status} />
                </TableCell>
                <TableCell align="right">
                  <CandidateReportActions
                    projectId={publicProjectId}
                    candidateId={encodeCandidateId(candidate.id)}
                    candidateName={`${candidate.firstName} ${candidate.lastName}`}
                    status={candidate.status}
                    surveyId={candidate.response?.surveyId?.toString() || null}
                  />
                </TableCell>
                <TableCell align="right" sx={{ pr: 1.5 }}>
                  <CandidateActionsMenu
                    projectId={publicProjectId}
                    candidateId={encodeCandidateId(candidate.id)}
                    firstName={candidate.firstName}
                    lastName={candidate.lastName}
                    email={candidate.email}
                    status={candidate.status}
                    surveyId={candidate.response?.surveyId?.toString() || null}
                    createdOn={candidate.createdOn?.toISOString() || null}
                    updatedAt={candidate.updatedAt?.toISOString() || null}
                  />
                </TableCell>
              </TableRow>
            ))}
            {candidates.length === 0 ? (
              <TableRow>
                <TableCell colSpan={5} sx={{ py: 6, textAlign: "center", color: "text.secondary" }}>
                  {q
                    ? `No candidates match “${q}”.`
                    : "No candidates yet. Add one to send them their assessment link."}
                </TableCell>
              </TableRow>
            ) : null}
          </TableBody>
        </Table>
      </Card>
    </Stack>
  );
}
