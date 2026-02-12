import { redirect } from "next/navigation";
import { Stack, Typography } from "@mui/material";
import { auth } from "@/auth";
import { prisma } from "@/lib/prisma";
import TeamReportBuilder from "@/components/team-reports/TeamReportBuilder";

export default async function TeamReportsPage() {
  const session = await auth();
  if (!session?.user?.id) {
    redirect("/login");
  }

  const userId = BigInt(session.user.id);
  const projects = await prisma.miroProject.findMany({
    where: {
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

  const serializedProjects = projects.map((project) => ({
    id: project.id.toString(),
    projectTitle: project.projectTitle,
    projectDescription: project.projectDescription,
    updatedAt: project.updatedAt?.toISOString() || null,
  }));

  return (
    <Stack spacing={2}>
      <div>
        <Typography variant="h4">Team Reports</Typography>
        <Typography variant="body2" color="text.secondary">
          Build a team report by selecting one or more projects.
        </Typography>
      </div>
      <TeamReportBuilder projects={serializedProjects} />
    </Stack>
  );
}
