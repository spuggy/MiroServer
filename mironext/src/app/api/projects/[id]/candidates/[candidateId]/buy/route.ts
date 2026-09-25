import { NextResponse } from "next/server";
import { buyReport, ReportAccessError } from "@/lib/candidates/reports";
import { currentPractitionerId, jsonError } from "@/lib/server/route-helpers";
import { decodeCandidateId, decodeProjectId } from "@/lib/public-ids";

type Params = { params: Promise<{ id: string; candidateId: string }> };

export async function POST(_request: Request, { params }: Params) {
  const practitionerId = await currentPractitionerId();
  if (!practitionerId) return jsonError("Unauthorized", 401);
  const { id, candidateId } = await params;
  const projectId = decodeProjectId(id);
  const candidate = decodeCandidateId(candidateId);
  if (!projectId || !candidate) return jsonError("Invalid id", 400);

  try {
    const { creditBalance } = await buyReport({
      projectId,
      candidateId: candidate,
      practitionerId,
    });
    return NextResponse.json({
      creditBalance,
      downloadUrl: `/api/projects/${id}/candidates/${candidateId}/report`,
    });
  } catch (error) {
    if (error instanceof ReportAccessError) return jsonError(error.message, error.httpStatus);
    console.error("buy report failed", error);
    return jsonError("Unable to buy the report.", 500);
  }
}
