import { prisma } from "@/lib/prisma";

export async function getNextHibernateId(db = prisma) {
  const rows = await db.$queryRaw`SELECT nextval('hibernate_sequence')::bigint AS id`;
  return BigInt(rows[0].id);
}
