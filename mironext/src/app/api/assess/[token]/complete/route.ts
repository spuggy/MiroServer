import { NextResponse } from "next/server";
import { AssessmentError } from "@/lib/assessment/survey";
import { completeAssessment, MissingAnswersError } from "@/lib/assessment/service";
import { jsonError } from "@/lib/server/route-helpers";

type Params = { params: Promise<{ token: string }> };

export async function POST(_request: Request, { params }: Params) {
  const { token } = await params;
  try {
    return NextResponse.json(await completeAssessment(token));
  } catch (error) {
    if (error instanceof MissingAnswersError) {
      return NextResponse.json(
        { error: error.message, missingQuestionIds: error.questionIds },
        { status: 409 },
      );
    }
    if (error instanceof AssessmentError) return jsonError(error.message, 409);
    console.error("complete assessment failed", error);
    return jsonError("Unable to submit your assessment.", 500);
  }
}
