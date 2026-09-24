import { NextResponse } from "next/server";
import { z } from "zod";
import { AssessmentError } from "@/lib/assessment/survey";
import { saveAnswer } from "@/lib/assessment/service";
import { jsonError } from "@/lib/server/route-helpers";

const BODY = z.object({
  questionId: z.string().regex(/^\d+$/),
  answer: z.union([
    z.object({ most: z.string(), least: z.string() }),
    z.object({ choice: z.string() }),
  ]),
});

type Params = { params: Promise<{ token: string }> };

export async function POST(request: Request, { params }: Params) {
  const { token } = await params;
  const parsed = BODY.safeParse(await request.json().catch(() => null));
  if (!parsed.success) return jsonError("Invalid answer", 400);
  try {
    const { answered } = await saveAnswer(token, parsed.data.questionId, parsed.data.answer);
    return NextResponse.json({ saved: true, answered });
  } catch (error) {
    if (error instanceof AssessmentError) return jsonError(error.message, 409);
    console.error("save answer failed", error);
    return jsonError("Unable to save your answer.", 500);
  }
}
