import { NextResponse } from "next/server";
import { auth } from "@/auth";

export function parseId(value: string | undefined): bigint | null {
  if (!value || !/^\d{1,18}$/.test(value)) return null;
  return BigInt(value);
}

/** The signed-in practitioner's id, or null. */
export async function currentPractitionerId(): Promise<bigint | null> {
  const session = (await auth()) as { user?: { id?: string } } | null;
  return parseId(session?.user?.id);
}

export function jsonError(message: string, status: number) {
  return NextResponse.json({ error: message }, { status });
}

/**
 * Public base URL for links in emails. Prefers an explicit APP_URL, then the
 * Railway public domain (e.g. dev.miro-assessment.com), then the request origin.
 */
export function appOrigin(request: Request): string {
  if (process.env.APP_URL) return process.env.APP_URL;
  const railwayDomain = process.env.RAILWAY_PUBLIC_DOMAIN;
  if (railwayDomain) return `https://${railwayDomain.replace(/^https?:\/\//, "")}`;
  return new URL(request.url).origin;
}
