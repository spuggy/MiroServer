import { NextResponse } from "next/server";
import { z } from "zod";
import { requestPasswordReset } from "@/lib/password-reset";
import { appOrigin, jsonError } from "@/lib/server/route-helpers";

const REQUEST_SCHEMA = z.object({ identifier: z.string().min(1).max(255) });

// Always answers the same way whether or not the account exists, so the
// endpoint can't be used to discover usernames or emails.
export async function POST(request: Request) {
  const body = await request.json().catch(() => null);
  const parsed = REQUEST_SCHEMA.safeParse(body);
  if (!parsed.success) return jsonError("Invalid payload", 400);

  let devResetUrl: string | undefined;
  try {
    const result = await requestPasswordReset({
      identifier: parsed.data.identifier,
      origin: appOrigin(request),
    });
    devResetUrl = result.devResetUrl;
  } catch (error) {
    console.error("password reset request failed", error);
  }

  return NextResponse.json({ ok: true, devResetUrl });
}
