import Link from "next/link";
import { redirect } from "next/navigation";
import {
  Box,
  Button,
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
import SearchIcon from "@mui/icons-material/Search";
import { auth } from "@/auth";
import { prisma } from "@/lib/prisma";
import { encodeProjectId } from "@/lib/public-ids";
import { getPagination } from "@/lib/pagination";
import ProjectDialog from "@/components/projects/ProjectDialog";

export default async function ProjectsPage({ searchParams }) {
  const session = await auth();
  if (!session?.user?.id) {
    redirect("/login");
  }
  const userId = BigInt(session.user.id);
  const resolvedSearchParams = await searchParams;
  const { page, pageSize, skip, take } = getPagination(resolvedSearchParams, {
    defaultPage: 1,
    pageSize: 10,
    maxPageSize: 25,
  });

  const qRaw = Array.isArray(resolvedSearchParams?.q)
    ? resolvedSearchParams.q[0]
    : resolvedSearchParams?.q;
  const q = qRaw?.trim().slice(0, 100) || "";

  const where = {
    createdById: userId,
    AND: [
      { OR: [{ projectStatus: null }, { projectStatus: { not: 3 } }] },
      ...(q
        ? [
            {
              OR: [
                { projectTitle: { contains: q, mode: "insensitive" } },
                { projectDescription: { contains: q, mode: "insensitive" } },
                { costcode: { contains: q, mode: "insensitive" } },
              ],
            },
          ]
        : []),
    ],
  };

  const [total, projects] = await Promise.all([
    prisma.miroProject.count({ where }),
    prisma.miroProject.findMany({
      where,
      orderBy: [{ updatedAt: "desc" }, { id: "desc" }],
      skip,
      take,
      select: {
        id: true,
        projectTitle: true,
        projectDescription: true,
        updatedAt: true,
      },
    }),
  ]);

  const totalPages = Math.max(1, Math.ceil(total / pageSize));
  const pageHref = (n) =>
    `/projects?${new URLSearchParams({ ...(q ? { q } : {}), page: String(n) })}`;

  return (
    <Stack spacing={3}>
      <Stack
        direction={{ xs: "column", sm: "row" }}
        justifyContent="space-between"
        alignItems={{ xs: "flex-start", sm: "flex-end" }}
        gap={2}
      >
        <Box>
          <Typography variant="h1">My Projects</Typography>
          <Typography sx={{ mt: 0.75, color: "text.secondary" }}>
            {q
              ? `${total} ${total === 1 ? "project matches" : "projects match"} “${q}”`
              : `${total} ${total === 1 ? "project" : "projects"}`}
          </Typography>
        </Box>
        <ProjectDialog practitionerEmail={session.user.email} />
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
            Projects
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
              placeholder="Search projects"
              inputProps={{ "aria-label": "Search projects by title, description or cost code" }}
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
              <Link href="/projects" style={{ fontSize: 14, fontWeight: 500 }}>
                Clear
              </Link>
            ) : null}
          </Box>
        </Stack>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell sx={{ pl: 2.5 }}>Project</TableCell>
              <TableCell sx={{ display: { xs: "none", md: "table-cell" } }}>Description</TableCell>
              <TableCell align="right" sx={{ pr: 2.5 }}>
                Last updated
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {projects.map((project) => (
              <TableRow key={encodeProjectId(project.id)} hover>
                <TableCell sx={{ pl: 2.5 }}>
                  <Link
                    href={`/projects/${encodeProjectId(project.id)}`}
                    style={{ fontWeight: 600, color: "inherit" }}
                  >
                    {project.projectTitle}
                  </Link>
                </TableCell>
                <TableCell
                  sx={{ color: "text.secondary", display: { xs: "none", md: "table-cell" } }}
                >
                  {project.projectDescription}
                </TableCell>
                <TableCell
                  align="right"
                  sx={{ pr: 2.5, color: "text.secondary", whiteSpace: "nowrap" }}
                >
                  {project.updatedAt?.toLocaleDateString("en-GB", {
                    day: "numeric",
                    month: "short",
                    year: "numeric",
                  }) || "—"}
                </TableCell>
              </TableRow>
            ))}
            {projects.length === 0 ? (
              <TableRow>
                <TableCell colSpan={3} sx={{ py: 6, textAlign: "center", color: "text.secondary" }}>
                  {q
                    ? `No projects match “${q}”.`
                    : "No projects yet. Create one to start inviting candidates."}
                </TableCell>
              </TableRow>
            ) : null}
          </TableBody>
        </Table>
      </Card>

      {totalPages > 1 ? (
        <Stack direction="row" justifyContent="space-between" alignItems="center">
          <Button href={pageHref(Math.max(1, page - 1))} variant="outlined" disabled={page <= 1}>
            Previous
          </Button>
          <Typography variant="body2" color="text.secondary">
            Page {page} of {totalPages}
          </Typography>
          <Button
            href={pageHref(Math.min(totalPages, page + 1))}
            variant="outlined"
            disabled={page >= totalPages}
          >
            Next
          </Button>
        </Stack>
      ) : null}
    </Stack>
  );
}
