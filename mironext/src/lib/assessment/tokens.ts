import crypto from "node:crypto";

// Invite links carry a random token; the database only stores its SHA-256
// hash, so a copy of the database cannot be turned into working links.

export function createInviteToken(): { token: string; tokenHash: string } {
  const token = crypto.randomBytes(32).toString("base64url");
  return { token, tokenHash: hashInviteToken(token) };
}

export function hashInviteToken(token: string): string {
  return crypto.createHash("sha256").update(token).digest("hex");
}

/** Cheap shape check before touching the database. */
export function looksLikeInviteToken(token: string): boolean {
  return /^[A-Za-z0-9_-]{43}$/.test(token);
}
