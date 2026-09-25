import { buildCandidateReport, ReportAccessError } from "@/lib/candidates/reports";
import { isReportType } from "@/lib/report";
import { currentPractitionerId, jsonError } from "@/lib/server/route-helpers";
import { decodeCandidateId, decodeProjectId } from "@/lib/public-ids";

// Chromium needs the Node.js runtime.
export const runtime = "nodejs";
export const dynamic = "force-dynamic";

type Params = { params: Promise<{ id: string; candidateId: string }> };

/** GET ?type=v10|v11|leadership (default v10). */
export async function GET(request: Request, { params }: Params) {
  const practitionerId = await currentPractitionerId();
  if (!practitionerId) return jsonError("Unauthorized", 401);
  const { id, candidateId } = await params;
  const projectId = decodeProjectId(id);
  const candidate = decodeCandidateId(candidateId);
  if (!projectId || !candidate) return jsonError("Invalid id", 400);
  const type = new URL(request.url).searchParams.get("type") ?? "v10";
  if (!isReportType(type)) return jsonError("Unknown report type", 400);

  try {
    const { pdf, fileName } = await buildCandidateReport(
      { projectId, candidateId: candidate, practitionerId },
      type,
    );
    return new Response(new Uint8Array(pdf), {
      headers: {
        "Content-Type": "application/pdf",
        "Content-Disposition": `attachment; filename="${fileName.replace(/[^\w.-]/g, "_")}"`,
        "Cache-Control": "private, no-store",
      },
    });
  } catch (error) {
    if (error instanceof ReportAccessError) return jsonError(error.message, error.httpStatus);
    console.error("report generation failed", error);
    return jsonError("Unable to generate the report.", 500);
  }
}
