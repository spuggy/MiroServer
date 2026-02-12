"use client";

import { useMemo, useState } from "react";
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Checkbox,
  CircularProgress,
  FormControlLabel,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";
import { toTeamReportCsv } from "@/lib/team-reports";

function formatDate(value) {
  if (!value) {
    return "-";
  }

  const date = new Date(value);
  if (Number.isNaN(date.getTime())) {
    return "-";
  }

  return date.toLocaleDateString();
}

function createFileName(createdAt) {
  const date = new Date(createdAt);
  if (Number.isNaN(date.getTime())) {
    return "team-report.csv";
  }

  const isoDay = date.toISOString().slice(0, 10);
  return `team-report-${isoDay}.csv`;
}

function saveCsvFile(csvContents, fileName) {
  const blob = new Blob([csvContents], { type: "text/csv;charset=utf-8;" });
  const url = URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  link.download = fileName;
  document.body.appendChild(link);
  link.click();
  link.remove();
  URL.revokeObjectURL(url);
}

export default function TeamReportBuilder({ projects }) {
  const [selectedProjectIds, setSelectedProjectIds] = useState([]);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [report, setReport] = useState(null);
  const selectedIds = useMemo(() => new Set(selectedProjectIds), [selectedProjectIds]);
  const reportRows = Array.isArray(report?.rows) ? report.rows : [];
  const hasProjects = projects.length > 0;
  const allProjectsSelected = hasProjects && selectedProjectIds.length === projects.length;

  function toggleProject(projectId) {
    setSelectedProjectIds((previous) => {
      if (previous.includes(projectId)) {
        return previous.filter((id) => id !== projectId);
      }
      return [...previous, projectId];
    });
  }

  function toggleAllProjects() {
    setSelectedProjectIds((previous) =>
      previous.length === projects.length ? [] : projects.map((p) => p.id),
    );
  }

  async function createReport() {
    setIsSubmitting(true);
    setError("");

    try {
      const response = await fetch("/api/team-reports", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ projectIds: selectedProjectIds }),
      });

      const responseData = await response.json().catch(() => null);
      if (!response.ok) {
        throw new Error(responseData?.error || "Unable to create team report.");
      }

      setReport(responseData);
    } catch (requestError) {
      setReport(null);
      setError(requestError.message || "Unable to create team report.");
    } finally {
      setIsSubmitting(false);
    }
  }

  function downloadReport() {
    if (!report) {
      return;
    }

    const csv = toTeamReportCsv(report);
    saveCsvFile(csv, createFileName(report.createdAt));
  }

  function deleteReport() {
    setReport(null);
    setError("");
  }

  return (
    <Stack spacing={2}>
      <Card>
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6">Select Projects</Typography>
            {!hasProjects ? (
              <Typography color="text.secondary">
                You do not have any active projects available for team reports.
              </Typography>
            ) : (
              <Box>
                <FormControlLabel
                  control={<Checkbox checked={allProjectsSelected} onChange={toggleAllProjects} />}
                  label="Select all projects"
                />
                <Table size="small">
                  <TableHead>
                    <TableRow>
                      <TableCell width={56}>Include</TableCell>
                      <TableCell>Project</TableCell>
                      <TableCell>Description</TableCell>
                      <TableCell align="right">Last Updated</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {projects.map((project) => (
                      <TableRow key={project.id} hover>
                        <TableCell padding="checkbox">
                          <Checkbox
                            checked={selectedIds.has(project.id)}
                            onChange={() => toggleProject(project.id)}
                            inputProps={{
                              "aria-label": `Select project ${project.projectTitle}`,
                            }}
                          />
                        </TableCell>
                        <TableCell>{project.projectTitle}</TableCell>
                        <TableCell>{project.projectDescription}</TableCell>
                        <TableCell align="right">{formatDate(project.updatedAt)}</TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </Box>
            )}
            <Stack direction={{ xs: "column", sm: "row" }} spacing={1}>
              <Button
                type="button"
                variant="contained"
                disabled={!hasProjects || selectedProjectIds.length === 0 || isSubmitting}
                onClick={createReport}
              >
                {isSubmitting ? (
                  <>
                    <CircularProgress
                      size={16}
                      color="inherit"
                      sx={{ display: "inline-flex", mr: 1 }}
                    />
                    Creating...
                  </>
                ) : (
                  "Create Team Report"
                )}
              </Button>
              <Button type="button" variant="outlined" disabled={!report} onClick={downloadReport}>
                Download Team Report
              </Button>
              <Button
                type="button"
                variant="outlined"
                color="error"
                disabled={!report}
                onClick={deleteReport}
              >
                Delete Team Report
              </Button>
            </Stack>
            {error ? <Alert severity="error">{error}</Alert> : null}
          </Stack>
        </CardContent>
      </Card>

      {report ? (
        <Card>
          <CardContent>
            <Stack spacing={1.5}>
              <Typography variant="h6">Generated Team Report</Typography>
              <Typography variant="body2" color="text.secondary">
                Created: {formatDate(report.createdAt)}. Projects: {report.projectCount}.
                Candidates: {report.totalCandidates}.
              </Typography>
              <Table size="small">
                <TableHead>
                  <TableRow>
                    <TableCell>Project</TableCell>
                    <TableCell>Description</TableCell>
                    <TableCell align="right">Candidates</TableCell>
                    <TableCell align="right">Last Updated</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {reportRows.map((row) => (
                    <TableRow key={row.projectId}>
                      <TableCell>{row.projectTitle}</TableCell>
                      <TableCell>{row.projectDescription}</TableCell>
                      <TableCell align="right">{row.candidateCount}</TableCell>
                      <TableCell align="right">{formatDate(row.lastUpdated)}</TableCell>
                    </TableRow>
                  ))}
                  {reportRows.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={4}>No projects in report.</TableCell>
                    </TableRow>
                  ) : null}
                </TableBody>
              </Table>
            </Stack>
          </CardContent>
        </Card>
      ) : null}
    </Stack>
  );
}
