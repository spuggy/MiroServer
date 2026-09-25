// Practitioner password resets: an emailed, single-use link that lets the user
// choose a new password. Tokens reuse the invite-token scheme (only a SHA-256
// hash is stored).

import type { Prisma } from "@prisma/client";
import { prisma } from "@/lib/prisma";
import { escapeHtml, sendEmail, type EmailResult } from "@/lib/email";
import { hashPassword } from "@/lib/password";
import { isEnabledFlag } from "@/lib/auth-utils";
import { createInviteToken, hashInviteToken, looksLikeInviteToken } from "@/lib/assessment/tokens";

export const RESET_TTL_MINUTES = 60;
export const MIN_PASSWORD_LENGTH = 8;

export interface RequestResetResult {
  email: EmailResult | null;
  /** Only returned when email isn't configured (local dev), so the link can be opened. */
  devResetUrl?: string;
}

/**
 * Emails a reset link to the practitioner matching `identifier` (username or
 * email). Callers must respond identically whether or not a user was found.
 */
export async function requestPasswordReset(opts: {
  identifier: string;
  origin: string;
}): Promise<RequestResetResult> {
  const identifier = opts.identifier.trim().toLowerCase();
  if (!identifier) return { email: null };

  const user = await prisma.appUser.findFirst({
    where: {
      OR: [{ username: identifier }, { email: { equals: identifier, mode: "insensitive" } }],
      deleted: { not: true },
      // Candidates are app_user rows attached to a project; they don't sign in.
      AND: [{ OR: [{ projectId: null }, { projectId: 0n }] }],
    },
    select: { id: true, firstName: true, email: true, enabled: true },
  });
  if (!user || !isEnabledFlag(user.enabled) || !user.email) return { email: null };

  const { token, tokenHash } = createInviteToken();
  const now = new Date();
  const expiresAt = new Date(now.getTime() + RESET_TTL_MINUTES * 60 * 1000);

  await prisma.$transaction(async (tx: Prisma.TransactionClient) => {
    // Only the newest link works.
    await tx.passwordReset.updateMany({
      where: { userId: user.id, usedAt: null },
      data: { usedAt: now },
    });
    await tx.passwordReset.create({ data: { tokenHash, userId: user.id, expiresAt } });
  });

  const url = `${opts.origin.replace(/\/$/, "")}/reset-password/${token}`;
  const email = await sendEmail({
    to: user.email,
    subject: "Reset your MiRo password",
    text: [
      `Hi ${user.firstName},`,
      "",
      "We received a request to reset your MiRo password.",
      "",
      `Choose a new password: ${url}`,
      "",
      `This link expires in ${RESET_TTL_MINUTES} minutes and can only be used once.`,
      "If you didn't ask for this, you can ignore this email.",
    ].join("\n"),
    html: resetHtml({ firstName: user.firstName, url }),
  });

  return { email, devResetUrl: email.delivered ? undefined : url };
}

export type ResetTokenState = "valid" | "invalid" | "expired";

async function findReset(token: string) {
  if (!looksLikeInviteToken(token)) return null;
  return prisma.passwordReset.findUnique({
    where: { tokenHash: hashInviteToken(token) },
    select: { id: true, userId: true, expiresAt: true, usedAt: true },
  });
}

export async function checkResetToken(token: string): Promise<ResetTokenState> {
  const reset = await findReset(token);
  if (!reset || reset.usedAt) return "invalid";
  if (reset.expiresAt <= new Date()) return "expired";
  return "valid";
}

/** Sets the new password and consumes the token. Returns the resulting token state. */
export async function resetPassword(token: string, password: string): Promise<ResetTokenState> {
  const reset = await findReset(token);
  if (!reset || reset.usedAt) return "invalid";
  const now = new Date();
  if (reset.expiresAt <= now) return "expired";

  const passwordHash = await hashPassword(password);
  return prisma.$transaction(async (tx: Prisma.TransactionClient) => {
    // Claim the token first so two concurrent submits can't both succeed.
    const claimed = await tx.passwordReset.updateMany({
      where: { id: reset.id, usedAt: null },
      data: { usedAt: now },
    });
    if (claimed.count !== 1) return "invalid";

    // Salted scrypt. The legacy Java server can't verify this format, so a
    // reset password only works for signing in to this app.
    await tx.appUser.update({
      where: { id: reset.userId },
      data: { password: passwordHash, credentialsExpired: "N", updatedAt: now },
    });
    await tx.passwordReset.updateMany({
      where: { userId: reset.userId, usedAt: null },
      data: { usedAt: now },
    });
    return "valid";
  });
}

function resetHtml(p: { firstName: string; url: string }): string {
  return `<!doctype html><html><body style="margin:0;background:#f4f4f4;font-family:Arial,Helvetica,sans-serif;color:#292929">
<table role="presentation" width="100%" cellpadding="0" cellspacing="0"><tr><td align="center" style="padding:32px 12px">
<table role="presentation" width="100%" style="max-width:560px;background:#fff;border-radius:8px" cellpadding="0" cellspacing="0">
<tr><td style="height:6px;background:linear-gradient(90deg,#d53f35 0 25%,#fbc726 25% 50%,#43add5 50% 75%,#42b449 75%);border-radius:8px 8px 0 0"></td></tr>
<tr><td style="padding:28px 32px 8px;font-size:15px;line-height:1.55">
<p style="margin:0 0 14px">Hi ${escapeHtml(p.firstName)},</p>
<p style="margin:0 0 22px">We received a request to reset your MiRo password.</p>
<p style="margin:0 0 22px"><a href="${escapeHtml(p.url)}" style="display:inline-block;background:#288eb5;color:#fff;text-decoration:none;padding:12px 22px;border-radius:6px;font-weight:bold">Choose a new password</a></p>
<p style="margin:0 0 6px;font-size:13px;color:#6b6b6b">This link expires in ${RESET_TTL_MINUTES} minutes and can only be used once.</p>
<p style="margin:0 0 24px;font-size:13px;color:#6b6b6b">If you didn't ask for this, you can ignore this email — your password won't change.</p>
</td></tr></table></td></tr></table></body></html>`;
}
