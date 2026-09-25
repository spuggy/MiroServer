// Read-only queries behind the sysadmin report screens. mr.mirotransactions has
// no Prisma relations, so user/project details are looked up in bulk.

import type { Prisma, PrismaClient } from "@prisma/client";
import { prisma as untypedPrisma } from "@/lib/prisma";
import { encodeCustomerId } from "@/lib/public-ids";
import {
  classifyTransaction,
  monthlyBuckets,
  PAID_STATUSES,
  rangeStart,
  statusFilterForKind,
  toPence,
  type MonthBucket,
  type RangeKey,
  type TransactionKind,
  TRANSACTION_STATUS,
} from "./report-math";
import { SYSADMIN_ROLE } from "./access";

// prisma.js is plain JS, so give this module the real client types.
const prisma = untypedPrisma as PrismaClient;

const NOT_DELETED_TX = { deleted: false } as const;
const ACTIVE_PROJECT: Prisma.MiroProjectWhereInput = {
  OR: [{ projectStatus: null }, { projectStatus: { not: 3 } }],
};

// ---- customers --------------------------------------------------------------

/** Practitioners: the legacy "admin" role, or anyone who has created a project; never sysadmins. */
export function customerWhere(q?: string): Prisma.AppUserWhereInput {
  const and: Prisma.AppUserWhereInput[] = [
    {
      OR: [
        { roles: { some: { role: { name: { equals: "admin", mode: "insensitive" } } } } },
        { createdProjects: { some: {} } },
      ],
    },
  ];
  if (q) {
    and.push({
      OR: [
        { firstName: { contains: q, mode: "insensitive" } },
        { lastName: { contains: q, mode: "insensitive" } },
        { email: { contains: q, mode: "insensitive" } },
        { username: { contains: q, mode: "insensitive" } },
        { company: { contains: q, mode: "insensitive" } },
      ],
    });
  }
  return {
    deleted: { not: true },
    roles: { none: { role: { name: { equals: SYSADMIN_ROLE, mode: "insensitive" } } } },
    AND: and,
  };
}

export interface PersonRef {
  token: string;
  name: string;
  company: string | null;
  email: string;
}

async function loadPeople(ids: bigint[]): Promise<Map<string, PersonRef>> {
  const unique = [...new Set(ids.map((id) => id.toString()))].map((id) => BigInt(id));
  const map = new Map<string, PersonRef>();
  if (unique.length === 0) return map;
  const users = await prisma.appUser.findMany({
    where: { id: { in: unique } },
    select: { id: true, firstName: true, lastName: true, company: true, email: true },
  });
  for (const u of users) {
    map.set(u.id.toString(), {
      token: encodeCustomerId(u.id),
      name: `${u.firstName} ${u.lastName}`.trim(),
      company: u.company || null,
      email: u.email,
    });
  }
  return map;
}

export interface CustomerStats {
  creditsPurchased: number; // paid and applied to the balance (status 4)
  creditsAwaitingApply: number; // paid but not yet applied (status 2)
  spendPence: number;
  purchases: number;
  reportsBought: number;
  failedPayments: number;
  lastPurchase: Date | null;
  lastActivity: Date | null;
  projects: number;
}

const EMPTY_STATS: CustomerStats = {
  creditsPurchased: 0,
  creditsAwaitingApply: 0,
  spendPence: 0,
  purchases: 0,
  reportsBought: 0,
  failedPayments: 0,
  lastPurchase: null,
  lastActivity: null,
  projects: 0,
};

function laterOf(a: Date | null, b: Date | null): Date | null {
  if (!a) return b;
  if (!b) return a;
  return a > b ? a : b;
}

export async function customerStats(ids: bigint[]): Promise<Map<string, CustomerStats>> {
  const stats = new Map<string, CustomerStats>();
  if (ids.length === 0) return stats;
  const [groups, projects] = await Promise.all([
    prisma.miroTransaction.groupBy({
      by: ["userId", "status"],
      where: { userId: { in: ids }, ...NOT_DELETED_TX },
      _sum: { credits: true, transValue: true },
      _count: { _all: true },
      _max: { createdOn: true },
    }),
    prisma.miroProject.groupBy({
      by: ["createdById"],
      where: { createdById: { in: ids }, ...ACTIVE_PROJECT },
      _count: { _all: true },
    }),
  ]);

  const get = (id: bigint) => {
    const key = id.toString();
    let s = stats.get(key);
    if (!s) {
      s = { ...EMPTY_STATS };
      stats.set(key, s);
    }
    return s;
  };
  for (const id of ids) get(id);

  for (const g of groups) {
    const s = get(g.userId);
    const kind = classifyTransaction(g.status);
    const count = g._count._all;
    const last = g._max.createdOn ?? null;
    if (kind === "purchase") {
      s.purchases += count;
      s.spendPence += toPence(g._sum.transValue);
      if (g.status === TRANSACTION_STATUS.OK_PROCESSED) s.creditsPurchased += g._sum.credits ?? 0;
      else s.creditsAwaitingApply += g._sum.credits ?? 0;
      s.lastPurchase = laterOf(s.lastPurchase, last);
    } else if (kind === "report") {
      s.reportsBought += count;
    } else if (kind === "failed") {
      s.failedPayments += count;
    }
    if (kind !== "pending") s.lastActivity = laterOf(s.lastActivity, last);
  }
  for (const p of projects) {
    if (p.createdById != null) get(p.createdById).projects = p._count._all;
  }
  return stats;
}

export type CustomerFilter = "all" | "low" | "negative";
export type CustomerSort = "name" | "balance-asc" | "balance-desc" | "newest";

export const CUSTOMER_FILTERS: Record<CustomerFilter, string> = {
  all: "All customers",
  low: "Zero or negative balance",
  negative: "Negative balance",
};
export const CUSTOMER_SORTS: Record<CustomerSort, string> = {
  name: "Name",
  "balance-asc": "Balance (low to high)",
  "balance-desc": "Balance (high to low)",
  newest: "Newest first",
};

export interface CustomerRow extends PersonRef {
  username: string;
  creditBalance: number | null;
  createdOn: Date | null;
  stats: CustomerStats;
}

export async function listCustomers(opts: {
  q: string;
  filter: CustomerFilter;
  sort: CustomerSort;
  skip: number;
  take: number;
}): Promise<{ total: number; rows: CustomerRow[] }> {
  const base = customerWhere(opts.q);
  const where: Prisma.AppUserWhereInput =
    opts.filter === "low"
      ? { AND: [base, { creditBalance: { lte: 0 } }] }
      : opts.filter === "negative"
        ? { AND: [base, { creditBalance: { lt: 0 } }] }
        : base;

  const orderBy: Prisma.AppUserOrderByWithRelationInput[] =
    opts.sort === "balance-asc"
      ? [{ creditBalance: { sort: "asc", nulls: "last" } }, { lastName: "asc" }]
      : opts.sort === "balance-desc"
        ? [{ creditBalance: { sort: "desc", nulls: "last" } }, { lastName: "asc" }]
        : opts.sort === "newest"
          ? [{ createdOn: { sort: "desc", nulls: "last" } }, { id: "desc" }]
          : [{ lastName: "asc" }, { firstName: "asc" }, { id: "asc" }];

  const [total, users] = await Promise.all([
    prisma.appUser.count({ where }),
    prisma.appUser.findMany({
      where,
      orderBy,
      skip: opts.skip,
      take: opts.take,
      select: {
        id: true,
        username: true,
        firstName: true,
        lastName: true,
        email: true,
        company: true,
        creditBalance: true,
        createdOn: true,
      },
    }),
  ]);
  const stats = await customerStats(users.map((u) => u.id));
  return {
    total,
    rows: users.map((u) => ({
      token: encodeCustomerId(u.id),
      name: `${u.firstName} ${u.lastName}`.trim(),
      company: u.company || null,
      email: u.email,
      username: u.username,
      creditBalance: u.creditBalance,
      createdOn: u.createdOn,
      stats: stats.get(u.id.toString()) ?? { ...EMPTY_STATS },
    })),
  };
}

export async function getCustomerDetail(id: bigint) {
  const user = await prisma.appUser.findFirst({
    where: { id, deleted: { not: true } },
    select: {
      id: true,
      username: true,
      firstName: true,
      lastName: true,
      email: true,
      company: true,
      phoneNumber: true,
      address1: true,
      address2: true,
      city: true,
      county: true,
      postcode: true,
      creditBalance: true,
      createdOn: true,
      enabled: true,
    },
  });
  if (!user) return null;

  const [stats, projects] = await Promise.all([
    customerStats([id]),
    prisma.miroProject.findMany({
      where: { createdById: id, ...ACTIVE_PROJECT },
      orderBy: [{ updatedAt: "desc" }, { id: "desc" }],
      take: 10,
      select: { id: true, projectTitle: true, costcode: true, createdOn: true },
    }),
  ]);
  const s = stats.get(id.toString()) ?? { ...EMPTY_STATS };

  // Credits should equal applied purchases minus reports bought. Legacy balances
  // may also include manual adjustments, so this is a hint, not a fault.
  const expectedBalance = s.creditsPurchased - s.reportsBought;
  const ledgerDifference = user.creditBalance == null ? null : user.creditBalance - expectedBalance;

  return { user, stats: s, projects, ledgerDifference };
}

// ---- transactions -----------------------------------------------------------

export interface TransactionFilters {
  kind?: TransactionKind;
  from?: Date | null;
  /** Exclusive upper bound. */
  to?: Date | null;
  customerId?: bigint | null;
  q?: string;
}

export async function buildTransactionWhere(
  filters: TransactionFilters,
): Promise<Prisma.MiroTransactionWhereInput> {
  const and: Prisma.MiroTransactionWhereInput[] = [NOT_DELETED_TX];
  if (filters.kind) and.push({ status: statusFilterForKind(filters.kind) });
  if (filters.from) and.push({ createdOn: { gte: filters.from } });
  if (filters.to) and.push({ createdOn: { lt: filters.to } });
  if (filters.customerId) and.push({ userId: filters.customerId });
  if (filters.q) {
    const people = await prisma.appUser.findMany({
      where: customerWhere(filters.q),
      select: { id: true },
      take: 200,
    });
    and.push({
      OR: [
        { userId: { in: people.map((p) => p.id) } },
        { paymentTransId: { contains: filters.q, mode: "insensitive" } },
        { paymentStatusDetail: { contains: filters.q, mode: "insensitive" } },
      ],
    });
  }
  return { AND: and };
}

export interface TransactionView {
  id: string;
  createdOn: Date;
  kind: TransactionKind;
  status: number;
  credits: number;
  value: number | null;
  /** Sage Pay reference for purchases; candidate name for reports. */
  reference: string;
  /** Payment status text for purchases; project (cost code) for reports. */
  detail: string;
  customer: PersonRef | null;
}

export async function fetchTransactionViews(
  where: Prisma.MiroTransactionWhereInput,
  skip: number,
  take: number,
): Promise<TransactionView[]> {
  const rows = await prisma.miroTransaction.findMany({
    where,
    orderBy: [{ createdOn: "desc" }, { id: "desc" }],
    skip,
    take,
    select: {
      id: true,
      userId: true,
      status: true,
      credits: true,
      transValue: true,
      createdOn: true,
      paymentStatus: true,
      paymentStatusDetail: true,
      paymentTransId: true,
    },
  });

  const reportRows = rows.filter((r) => classifyTransaction(r.status) === "report");
  const candidateIds = reportRows
    .map((r) =>
      r.paymentTransId && /^\d{1,18}$/.test(r.paymentTransId) ? BigInt(r.paymentTransId) : null,
    )
    .filter((id): id is bigint => id !== null);

  const [people, candidates] = await Promise.all([
    loadPeople(rows.map((r) => r.userId)),
    candidateIds.length
      ? prisma.appUser.findMany({
          where: { id: { in: candidateIds } },
          select: { id: true, projectId: true },
        })
      : Promise.resolve([]),
  ]);
  const projectIds = candidates.map((c) => c.projectId).filter((id): id is bigint => id != null);
  const projects = projectIds.length
    ? await prisma.miroProject.findMany({
        where: { id: { in: projectIds } },
        select: { id: true, projectTitle: true, costcode: true },
      })
    : [];
  const projectById = new Map(projects.map((p) => [p.id.toString(), p]));
  const projectByCandidate = new Map(
    candidates.map((c) => [
      c.id.toString(),
      c.projectId ? projectById.get(c.projectId.toString()) : undefined,
    ]),
  );

  return rows.map((r) => {
    const kind = classifyTransaction(r.status);
    let reference = r.paymentTransId ?? "";
    let detail = r.paymentStatusDetail ?? r.paymentStatus ?? "";
    if (kind === "report") {
      reference = r.paymentStatusDetail ?? "";
      const project = r.paymentTransId ? projectByCandidate.get(r.paymentTransId) : undefined;
      detail = project
        ? `${project.projectTitle}${project.costcode ? ` (${project.costcode})` : ""}`
        : "";
    }
    return {
      id: r.id.toString(),
      createdOn: r.createdOn,
      kind,
      status: r.status,
      credits: r.credits,
      value: r.transValue,
      reference,
      detail,
      customer: people.get(r.userId.toString()) ?? null,
    };
  });
}

export async function listTransactions(filters: TransactionFilters, skip: number, take: number) {
  const where = await buildTransactionWhere(filters);
  const [total, paid, rows] = await Promise.all([
    prisma.miroTransaction.count({ where }),
    prisma.miroTransaction.aggregate({
      where: { AND: [where, { status: { in: PAID_STATUSES } }] },
      _sum: { transValue: true, credits: true },
      _count: { _all: true },
    }),
    fetchTransactionViews(where, skip, take),
  ]);
  return {
    total,
    rows,
    paidPence: toPence(paid._sum.transValue),
    paidCredits: paid._sum.credits ?? 0,
    paidCount: paid._count._all,
  };
}

const EXPORT_BATCH = 1000;
export const EXPORT_MAX_ROWS = 20000;

export async function exportTransactions(filters: TransactionFilters): Promise<TransactionView[]> {
  const where = await buildTransactionWhere(filters);
  const all: TransactionView[] = [];
  for (let skip = 0; skip < EXPORT_MAX_ROWS; skip += EXPORT_BATCH) {
    const batch = await fetchTransactionViews(where, skip, EXPORT_BATCH);
    all.push(...batch);
    if (batch.length < EXPORT_BATCH) break;
  }
  return all;
}

// ---- overview ---------------------------------------------------------------

export interface TopCustomer extends PersonRef {
  revenuePence: number;
  credits: number;
  reports: number;
}

export async function getOverview(range: RangeKey, now: Date = new Date()) {
  const since = rangeStart(range, now);
  const chartStart = rangeStart("12m", now)!;
  const fetchFrom = since && since < chartStart ? since : chartStart;
  const fetchAll = since === null;

  const [rows, customerCount, balanceSum, lowBalance, negativeBalance, unapplied, failed] =
    await Promise.all([
      prisma.miroTransaction.findMany({
        where: { ...NOT_DELETED_TX, ...(fetchAll ? {} : { createdOn: { gte: fetchFrom } }) },
        select: { userId: true, status: true, credits: true, transValue: true, createdOn: true },
      }),
      prisma.appUser.count({ where: customerWhere() }),
      prisma.appUser.aggregate({ where: customerWhere(), _sum: { creditBalance: true } }),
      prisma.appUser.count({ where: { AND: [customerWhere(), { creditBalance: { lte: 0 } }] } }),
      prisma.appUser.count({ where: { AND: [customerWhere(), { creditBalance: { lt: 0 } }] } }),
      prisma.miroTransaction.findMany({
        where: { ...NOT_DELETED_TX, status: TRANSACTION_STATUS.OK },
        orderBy: { createdOn: "asc" },
        take: 5,
        select: { id: true },
      }),
      prisma.miroTransaction.findMany({
        where: { ...NOT_DELETED_TX, status: statusFilterForKind("failed") },
        orderBy: { createdOn: "desc" },
        take: 5,
      }),
    ]);

  const inPeriod = since ? rows.filter((r) => r.createdOn >= since) : rows;
  const totals = { revenuePence: 0, credits: 0, purchases: 0, reports: 0, failed: 0 };
  const perCustomer = new Map<
    string,
    { revenuePence: number; credits: number; reports: number; userId: bigint }
  >();
  for (const r of inPeriod) {
    const kind = classifyTransaction(r.status);
    if (kind === "pending") continue;
    const entry = perCustomer.get(r.userId.toString()) ?? {
      revenuePence: 0,
      credits: 0,
      reports: 0,
      userId: r.userId,
    };
    if (kind === "purchase") {
      totals.purchases += 1;
      totals.credits += r.credits;
      totals.revenuePence += toPence(r.transValue);
      entry.revenuePence += toPence(r.transValue);
      entry.credits += r.credits;
    } else if (kind === "report") {
      totals.reports += 1;
      entry.reports += 1;
    } else {
      totals.failed += 1;
    }
    perCustomer.set(r.userId.toString(), entry);
  }

  const topEntries = [...perCustomer.values()]
    .filter((e) => e.revenuePence > 0 || e.reports > 0)
    // Ranked by usage: customers billed on account have no payments to rank on.
    .sort((a, b) => b.reports - a.reports || b.revenuePence - a.revenuePence)
    .slice(0, 8);

  const people = await loadPeople([
    ...topEntries.map((e) => e.userId),
    ...failed.map((f) => f.userId),
  ]);

  const topCustomers: TopCustomer[] = topEntries.flatMap((e) => {
    const person = people.get(e.userId.toString());
    return person
      ? [{ ...person, revenuePence: e.revenuePence, credits: e.credits, reports: e.reports }]
      : [];
  });

  const months: MonthBucket[] = monthlyBuckets(rows, 12, now);

  return {
    since,
    totals,
    months,
    topCustomers,
    customers: {
      total: customerCount,
      creditsOutstanding: balanceSum._sum.creditBalance ?? 0,
      lowBalance,
      negativeBalance,
    },
    attention: {
      unappliedCount: unapplied.length, // capped at 5; the UI shows "5+"
      failed: failed.map((f) => ({
        id: f.id.toString(),
        createdOn: f.createdOn,
        value: f.transValue,
        detail: f.paymentStatusDetail ?? f.paymentStatus ?? "",
        customer: people.get(f.userId.toString()) ?? null,
      })),
    },
  };
}
