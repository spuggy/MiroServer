import { NextResponse } from "next/server";
import { AssessmentError } from "@/lib/assessment/survey";
import { sendAssessmentInvite } from "@/lib/assessment/service";
import { appOrigin, currentPractitionerId, jsonError, parseId } from "@/lib/server/route-helpers";

type Params = { params: Promise<{ id: string; candidateId: string }> };

export async function POST(request: Request, { params }: Params) {
  const practitionerId = await currentPractitionerId();
  if (!practitionerId) return jsonError("Unauthorized", 401);
  const { id, candidateId } = await params;
  const projectId = parseId(id);
  const candidate = parseId(candidateId);
  if (!projectId || !candidate) return jsonError("Invalid id", 400);

  try {
    const result = await sendAssessmentInvite({
      projectId,
      candidateId: candidate,
      practitionerId,
      origin: appOrigin(request),
    });
    return NextResponse.json({
      sent: result.email.delivered,
      transport: result.email.transport,
      devInviteUrl: result.devInviteUrl,
    });
  } catch (error) {
    if (error instanceof AssessmentError) return jsonError(error.message, 409);
    console.error("invite failed", error);
    return jsonError("Unable to send the invite.", 500);
  }
}
