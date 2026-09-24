import type { Prisma } from "@prisma/client";

// Legacy tables have plain BIGINT ids that Hibernate assigned. The shared
// `hibernate_sequence` in the restored data (last value 13) is far behind the
// real ids (app_user ~32k, responses ~48k), so `nextval` would collide.
// Allocate max(id)+1 under a per-table advisory lock instead; call this inside
// the same transaction as the insert.

const TABLES = {
  "public.app_user": 1001,
  "mr.responses": 1002,
  "mr.mirotransactions": 1003,
  "mr.miroprojects": 1004,
} as const;

export type LegacyTable = keyof typeof TABLES;

export async function allocateLegacyId(
  tx: Prisma.TransactionClient,
  table: LegacyTable,
): Promise<bigint> {
  await tx.$executeRawUnsafe(`SELECT pg_advisory_xact_lock(${TABLES[table]})`);
  const rows = await tx.$queryRawUnsafe<{ id: bigint }[]>(
    `SELECT COALESCE(MAX(id), 0) + 1 AS id FROM ${table}`,
  );
  return BigInt(rows[0].id);
}
