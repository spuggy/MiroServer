import { NextResponse } from "next/server";
import { z } from "zod";
import { AssessmentError } from "@/lib/assessment/survey";
import { confirmCandidate, CONSENT_REQUIRED } from "@/lib/assessment/service";
import { jsonError } from "@/lib/server/route-helpers";

const BODY = z.object({
  firstName: z.string().trim().min(1).max(50),
  lastName: z.string().trim().min(1).max(50),
  gdpr: z.boolean(),
});

type Params = { params: Promise<{ token: string }> };

export async function POST(request: Request, { params }: Params) {
  const { token } = await params;
  const parsed = BODY.safeParse(await request.json().catch(() => null));
  if (!parsed.success) return jsonError("Please enter your first and last name.", 400);
  if (!parsed.data.gdpr) return jsonError(CONSENT_REQUIRED, 400);
  try {
    await confirmCandidate(token, {
      firstName: parsed.data.firstName,
      lastName: parsed.data.lastName,
    });
    return NextResponse.json({ confirmed: true });
  } catch (error) {
    if (error instanceof AssessmentError) return jsonError(error.message, 409);
    console.error("confirm candidate failed", error);
    return jsonError("Unable to save your details.", 500);
  }
}
