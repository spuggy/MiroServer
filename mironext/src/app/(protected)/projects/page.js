import Link from "next/link";
import {
  Box,
  Card,
  CardContent,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";
import { auth } from "@/auth";
import { prisma } from "@/lib/prisma";
import { getPagination } from "@/lib/pagination";
import NewProjectDialog from "@/components/projects/NewProjectDialog";

export default async function ProjectsPage({ searchParams }) {
  const session = await auth();
  const userId = BigInt(session.user.id);
  const resolvedSearchParams = await searchParams;
  const { page, pageSize, skip, take } = getPagination(resolvedSearchParams, {
    defaultPage: 1,
    pageSize: 10,
    maxPageSize: 25,
  });

  const where = {
    createdById: userId,
    OR: [{ projectStatus: null }, { projectStatus: { not: 3 } }],
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

  return (
    <Stack spacing={2}>
      <Stack direction="row" justifyContent="space-between" alignItems="center">
        <Typography variant="h4">My Projects</Typography>
        <NewProjectDialog />
      </Stack>

      <Card>
        <Box sx={{ px: 2, py: 1, borderBottom: "1px solid #e5e5e5", backgroundColor: "#fafafa" }}>
          <Typography variant="h6" sx={{ color: "#6b6b6b" }}>
            Projects
          </Typography>
        </Box>
        <CardContent>
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>Project</TableCell>
                <TableCell>Description</TableCell>
                <TableCell align="right">Last Updated</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {projects.map((project) => (
                <TableRow key={project.id.toString()} hover>
                  <TableCell>
                    <Link href={`/projects/${project.id.toString()}`}>{project.projectTitle}</Link>
                  </TableCell>
                  <TableCell>{project.projectDescription}</TableCell>
                  <TableCell align="right">
                    {project.updatedAt?.toLocaleDateString() || "-"}
                  </TableCell>
                </TableRow>
              ))}
              {projects.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={3}>No projects found.</TableCell>
                </TableRow>
              ) : null}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      <Box sx={{ display: "flex", justifyContent: "space-between" }}>
        {page <= 1 ? (
          <span style={{ color: "#999" }}>Previous</span>
        ) : (
          <Link href={`/projects?page=${Math.max(1, page - 1)}`}>Previous</Link>
        )}
        <Typography variant="body2" sx={{ alignSelf: "center" }}>
          Page {page} of {totalPages}
        </Typography>
        {page >= totalPages ? (
          <span style={{ color: "#999" }}>Next</span>
        ) : (
          <Link href={`/projects?page=${Math.min(totalPages, page + 1)}`}>Next</Link>
        )}
      </Box>
    </Stack>
  );
}
