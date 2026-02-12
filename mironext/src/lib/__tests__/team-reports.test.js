import { describe, expect, it } from "vitest";
import { buildTeamReport, parseProjectId, toTeamReportCsv } from "@/lib/team-reports";

describe("parseProjectId", () => {
  it("parses valid values", () => {
    expect(parseProjectId("42")).toBe(42n);
    expect(parseProjectId(7)).toBe(7n);
    expect(parseProjectId(9n)).toBe(9n);
  });

  it("rejects invalid values", () => {
    expect(parseProjectId("")).toBeNull();
    expect(parseProjectId("abc")).toBeNull();
    expect(parseProjectId("0")).toBeNull();
    expect(parseProjectId(-2)).toBeNull();
    expect(parseProjectId(null)).toBeNull();
  });
});

describe("buildTeamReport", () => {
  it("builds rows and totals", () => {
    const report = buildTeamReport({
      createdAt: new Date("2026-02-12T10:00:00.000Z"),
      projects: [
        {
          id: 101n,
          projectTitle: "Alpha",
          projectDescription: "First project",
          updatedAt: new Date("2026-01-01T00:00:00.000Z"),
        },
        {
          id: 102n,
          projectTitle: "Beta",
          projectDescription: "Second project",
          updatedAt: null,
        },
      ],
      candidateCountsByProjectId: {
        101: 3,
      },
    });

    expect(report.projectCount).toBe(2);
    expect(report.totalCandidates).toBe(3);
    expect(report.rows).toEqual([
      {
        projectId: "101",
        projectTitle: "Alpha",
        projectDescription: "First project",
        lastUpdated: "2026-01-01T00:00:00.000Z",
        candidateCount: 3,
      },
      {
        projectId: "102",
        projectTitle: "Beta",
        projectDescription: "Second project",
        lastUpdated: null,
        candidateCount: 0,
      },
    ]);
  });
});

describe("toTeamReportCsv", () => {
  it("creates CSV with escaped values", () => {
    const csv = toTeamReportCsv({
      createdAt: "2026-02-12T10:00:00.000Z",
      projectCount: 1,
      totalCandidates: 2,
      rows: [
        {
          projectId: "201",
          projectTitle: 'Alpha, "Beta"',
          projectDescription: "Line 1\nLine 2",
          lastUpdated: "2026-02-01T00:00:00.000Z",
          candidateCount: 2,
        },
      ],
    });

    expect(csv).toContain(
      "Project ID,Project Title,Project Description,Last Updated,Candidate Count",
    );
    expect(csv).toContain('"Alpha, ""Beta"""');
    expect(csv).toContain('"Line 1\nLine 2"');
  });
});
