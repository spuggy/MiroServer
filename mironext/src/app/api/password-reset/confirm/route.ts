import { NextResponse } from "next/server";
import { z } from "zod";
import { MIN_PASSWORD_LENGTH, resetPassword } from "@/lib/password-reset";
import { jsonError } from "@/lib/server/route-helpers";

const CONFIRM_SCHEMA = z.object({
  token: z.string().min(1).max(100),
  password: z.string().min(MIN_PASSWORD_LENGTH).max(255),
});

export async function POST(request: Request) {
  const body = await request.json().catch(() => null);
  const parsed = CONFIRM_SCHEMA.safeParse(body);
  if (!parsed.success) {
    return jsonError(`Password must be at least ${MIN_PASSWORD_LENGTH} characters.`, 400);
  }

  const state = await resetPassword(parsed.data.token, parsed.data.password);
  if (state === "expired") return jsonError("This reset link has expired.", 410);
  if (state === "invalid") return jsonError("This reset link isn't valid.", 400);
  return NextResponse.json({ ok: true });
}
