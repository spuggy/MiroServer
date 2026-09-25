import { NextResponse } from "next/server";
import { currentSysAdminId } from "@/lib/admin/access";
import { parseTransactionParams } from "@/lib/admin/params";
import { EXPORT_MAX_ROWS, exportTransactions } from "@/lib/admin/queries";
import { classifyTransaction, KIND_LABELS, toCsv } from "@/lib/admin/report-math";

export const dynamic = "force-dynamic";

export async function GET(request: Request) {
  // 404 rather than 403, so the admin area isn't discoverable.
  if (!(await currentSysAdminId())) return new NextResponse("Not found", { status: 404 });

  const { filters } = parseTransactionParams(
    Object.fromEntries(new URL(request.url).searchParams.entries()),
  );
  const rows = await exportTransactions(filters);

  const csv = toCsv(
    [
      "Date",
      "Customer",
      "Company",
      "Email",
      "Type",
      "Reference",
      "Details",
      "Credits",
      "Paid (GBP)",
    ],
    rows.map((r) => [
      r.createdOn.toISOString(),
      r.customer?.name,
      r.customer?.company,
      r.customer?.email,
      r.status === 2 && r.kind === "purchase"
        ? "Paid, credits pending"
        : KIND_LABELS[classifyTransaction(r.status)],
      r.reference,
      r.detail,
      r.credits,
      r.kind === "purchase" ? (r.value ?? 0).toFixed(2) : "",
    ]),
  );

  const headers: Record<string, string> = {
    "Content-Type": "text/csv; charset=utf-8",
    "Content-Disposition": `attachment; filename="transactions-${new Date().toISOString().slice(0, 10)}.csv"`,
    "Cache-Control": "no-store",
  };
  // Tell the admin if the export was cut short.
  if (rows.length >= EXPORT_MAX_ROWS) headers["X-Export-Truncated"] = "true";
  return new NextResponse(csv, { headers });
}
