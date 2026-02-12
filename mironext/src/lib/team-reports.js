function parseCsvCell(value) {
  const rawValue = value == null ? "" : String(value);
  if (!/[",\n]/.test(rawValue)) {
    return rawValue;
  }
  return `"${rawValue.replaceAll('"', '""')}"`;
}

function toPositiveInteger(value) {
  const parsed = Number(value);
  if (!Number.isFinite(parsed) || parsed <= 0) {
    return 0;
  }
  return Math.trunc(parsed);
}

function getCandidateCount(candidateCountsByProjectId, projectId) {
  if (candidateCountsByProjectId instanceof Map) {
    return toPositiveInteger(candidateCountsByProjectId.get(projectId));
  }

  return toPositiveInteger(candidateCountsByProjectId?.[projectId]);
}

function toIsoDateString(value) {
  if (!value) {
    return null;
  }

  const date = value instanceof Date ? value : new Date(value);
  if (Number.isNaN(date.getTime())) {
    return null;
  }

  return date.toISOString();
}

export function parseProjectId(value) {
  if (typeof value === "bigint") {
    return value > 0n ? value : null;
  }

  if (typeof value === "number" && Number.isInteger(value) && value > 0) {
    return BigInt(value);
  }

  if (typeof value !== "string") {
    return null;
  }

  const trimmed = value.trim();
  if (!/^\d+$/.test(trimmed)) {
    return null;
  }

  try {
    const parsed = BigInt(trimmed);
    return parsed > 0n ? parsed : null;
  } catch {
    return null;
  }
}

export function buildTeamReport({ projects, candidateCountsByProjectId, createdAt = new Date() }) {
  const reportCreatedAt = createdAt instanceof Date ? createdAt : new Date(createdAt);
  const safeCreatedAt = Number.isNaN(reportCreatedAt.getTime()) ? new Date() : reportCreatedAt;
  const rows = (projects || []).map((project) => {
    const projectId = project.id.toString();

    return {
      projectId,
      projectTitle: project.projectTitle || "",
      projectDescription: project.projectDescription || "",
      lastUpdated: toIsoDateString(project.updatedAt),
      candidateCount: getCandidateCount(candidateCountsByProjectId, projectId),
    };
  });

  return {
    createdAt: safeCreatedAt.toISOString(),
    projectCount: rows.length,
    totalCandidates: rows.reduce((total, row) => total + row.candidateCount, 0),
    rows,
  };
}

export function toTeamReportCsv(report) {
  const rows = Array.isArray(report?.rows) ? report.rows : [];
  const computedTotalCandidates = rows.reduce(
    (total, row) => total + toPositiveInteger(row?.candidateCount),
    0,
  );
  const projectCount = toPositiveInteger(report?.projectCount) || rows.length;
  const totalCandidates = toPositiveInteger(report?.totalCandidates) || computedTotalCandidates;
  const createdAt =
    typeof report?.createdAt === "string" ? report.createdAt : new Date().toISOString();

  const csvRows = [
    ["Created At", createdAt],
    ["Projects Included", String(projectCount)],
    ["Total Candidates", String(totalCandidates)],
    [],
    ["Project ID", "Project Title", "Project Description", "Last Updated", "Candidate Count"],
    ...rows.map((row) => [
      row?.projectId || "",
      row?.projectTitle || "",
      row?.projectDescription || "",
      row?.lastUpdated || "",
      String(toPositiveInteger(row?.candidateCount)),
    ]),
  ];

  return csvRows.map((row) => row.map(parseCsvCell).join(",")).join("\n");
}
