import Link from "next/link";
import { Card, Table, TableBody, TableCell, TableHead, TableRow, Typography } from "@mui/material";
import { formatDateTime, formatMoney } from "@/lib/admin/report-math";
import TransactionKindChip from "./TransactionKindChip";

export default function TransactionsTable({ rows, showCustomer = true, emptyText }) {
  return (
    <Card sx={{ overflowX: "auto" }}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell sx={{ pl: 2.5 }}>Date</TableCell>
            {showCustomer ? <TableCell>Customer</TableCell> : null}
            <TableCell>Type</TableCell>
            <TableCell>Reference</TableCell>
            <TableCell>Details</TableCell>
            <TableCell align="right">Credits</TableCell>
            <TableCell align="right" sx={{ pr: 2.5 }}>
              Paid
            </TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map((row) => (
            <TableRow key={row.id} hover>
              <TableCell sx={{ pl: 2.5, whiteSpace: "nowrap", color: "text.secondary" }}>
                {formatDateTime(row.createdOn)}
              </TableCell>
              {showCustomer ? (
                <TableCell>
                  {row.customer ? (
                    <Link
                      href={`/admin/customers/${row.customer.token}`}
                      style={{ fontWeight: 600, color: "inherit" }}
                    >
                      {row.customer.name}
                    </Link>
                  ) : (
                    "—"
                  )}
                  {row.customer?.company ? (
                    <Typography variant="body2" color="text.secondary">
                      {row.customer.company}
                    </Typography>
                  ) : null}
                </TableCell>
              ) : null}
              <TableCell>
                <TransactionKindChip kind={row.kind} status={row.status} />
              </TableCell>
              <TableCell sx={{ wordBreak: "break-word" }}>{row.reference || "—"}</TableCell>
              <TableCell sx={{ color: "text.secondary" }}>{row.detail || "—"}</TableCell>
              <TableCell align="right">{row.credits}</TableCell>
              <TableCell align="right" sx={{ pr: 2.5, whiteSpace: "nowrap" }}>
                {row.kind === "purchase" ? formatMoney(row.value) : "—"}
              </TableCell>
            </TableRow>
          ))}
          {rows.length === 0 ? (
            <TableRow>
              <TableCell
                colSpan={showCustomer ? 7 : 6}
                sx={{ py: 6, textAlign: "center", color: "text.secondary" }}
              >
                {emptyText ?? "No transactions."}
              </TableCell>
            </TableRow>
          ) : null}
        </TableBody>
      </Table>
    </Card>
  );
}
