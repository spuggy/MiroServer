import Link from "next/link";
import { Box, Button, Card, MenuItem, Stack, TextField, Typography } from "@mui/material";
import { listTransactions, getCustomerDetail } from "@/lib/admin/queries";
import { parseTransactionParams, withParams } from "@/lib/admin/params";
import { formatPence, KIND_LABELS } from "@/lib/admin/report-math";
import { decodeCustomerId } from "@/lib/public-ids";
import { getPagination } from "@/lib/pagination";
import Pager from "@/components/admin/Pager";
import TransactionsTable from "@/components/admin/TransactionsTable";

export default async function AdminTransactionsPage({ searchParams }) {
  const params = await searchParams;
  const { page, pageSize, skip, take } = getPagination(params, { pageSize: 25, maxPageSize: 100 });
  const { filters, values } = parseTransactionParams(params);
  const { total, rows, paidPence, paidCredits, paidCount } = await listTransactions(
    filters,
    skip,
    take,
  );
  const customer = values.customer
    ? (await getCustomerDetail(decodeCustomerId(values.customer)))?.user
    : null;

  const totalPages = Math.max(1, Math.ceil(total / pageSize));
  const filterParams = {
    kind: values.kind,
    from: values.from,
    to: values.to,
    q: values.q,
    customer: values.customer,
  };
  const hrefFor = (n) => withParams("/admin/transactions", { ...filterParams, page: n });
  const exportHref = withParams("/api/admin/transactions/export", filterParams);
  const filtered = Object.values(filterParams).some(Boolean);

  return (
    <Stack spacing={3}>
      <Box>
        <Typography color="text.secondary">
          {total.toLocaleString("en-GB")} {total === 1 ? "transaction" : "transactions"}
          {customer ? ` for ${customer.firstName} ${customer.lastName}` : ""}
          {paidCount > 0
            ? ` · ${formatPence(paidPence)} paid for ${paidCredits.toLocaleString("en-GB")} credits`
            : ""}
        </Typography>
      </Box>

      <Card>
        <Box
          component="form"
          method="get"
          role="search"
          sx={{ display: "flex", flexWrap: "wrap", alignItems: "center", gap: 1.5, px: 2.5, py: 2 }}
        >
          {values.customer ? <input type="hidden" name="customer" value={values.customer} /> : null}
          <TextField
            select
            name="kind"
            defaultValue={values.kind}
            label="Type"
            sx={{ minWidth: 180 }}
          >
            <MenuItem value="">All types</MenuItem>
            {Object.entries(KIND_LABELS).map(([value, label]) => (
              <MenuItem key={value} value={value}>
                {label}
              </MenuItem>
            ))}
          </TextField>
          <TextField
            name="from"
            type="date"
            label="From"
            defaultValue={values.from}
            InputLabelProps={{ shrink: true }}
          />
          <TextField
            name="to"
            type="date"
            label="To"
            defaultValue={values.to}
            InputLabelProps={{ shrink: true }}
          />
          <TextField
            name="q"
            defaultValue={values.q}
            label="Search"
            placeholder="Customer, reference or candidate"
            sx={{ width: { xs: "100%", sm: 260 } }}
          />
          <Button type="submit" variant="contained">
            Apply
          </Button>
          {filtered ? (
            <Link href="/admin/transactions" style={{ fontSize: 14, fontWeight: 500 }}>
              Clear
            </Link>
          ) : null}
          <Box sx={{ flex: 1 }} />
          <Button href={exportHref} variant="outlined" download>
            Export CSV
          </Button>
        </Box>
      </Card>

      <TransactionsTable
        rows={rows}
        emptyText={filtered ? "No transactions match these filters." : "No transactions yet."}
      />
      <Pager page={page} totalPages={totalPages} hrefFor={hrefFor} />
    </Stack>
  );
}
