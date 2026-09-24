import { buildCandidateReport, ReportAccessError } from "@/lib/candidates/reports";
import { currentPractitionerId, jsonError, parseId } from "@/lib/server/route-helpers";

// Chromium needs the Node.js runtime.
export const runtime = "nodejs";
export const dynamic = "force-dynamic";

type Params = { params: Promise<{ id: string; candidateId: string }> };

export async function GET(_request: Request, { params }: Params) {
  const practitionerId = await currentPractitionerId();
  if (!practitionerId) return jsonError("Unauthorized", 401);
  const { id, candidateId } = await params;
  const projectId = parseId(id);
  const candidate = parseId(candidateId);
  if (!projectId || !candidate) return jsonError("Invalid id", 400);

  try {
    const { pdf, fileName } = await buildCandidateReport({
      projectId,
      candidateId: candidate,
      practitionerId,
    });
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
