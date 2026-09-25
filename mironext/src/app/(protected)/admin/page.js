import Link from "next/link";
import {
  Box,
  Button,
  Card,
  Grid,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";
import { getOverview } from "@/lib/admin/queries";
import { firstParam, withParams } from "@/lib/admin/params";
import {
  DEFAULT_RANGE,
  formatDate,
  formatMoney,
  formatPence,
  isRangeKey,
  RANGES,
} from "@/lib/admin/report-math";
import StatTile from "@/components/admin/StatTile";
import RevenueChart from "@/components/admin/RevenueChart";

export default async function AdminOverviewPage({ searchParams }) {
  const params = await searchParams;
  const rangeRaw = firstParam(params, "range");
  const range = isRangeKey(rangeRaw) ? rangeRaw : DEFAULT_RANGE;
  const data = await getOverview(range);
  const { totals, customers, attention } = data;
  const periodLabel = RANGES[range].toLowerCase();
  const sinceParam = data.since ? { from: data.since.toISOString().slice(0, 10) } : {};

  return (
    <Stack spacing={3}>
      <Stack direction="row" gap={1} flexWrap="wrap" role="group" aria-label="Period">
        {Object.entries(RANGES).map(([key, label]) => (
          <Button
            key={key}
            href={withParams("/admin", { range: key })}
            variant={key === range ? "contained" : "outlined"}
            size="small"
            aria-current={key === range ? "true" : undefined}
          >
            {label}
          </Button>
        ))}
      </Stack>

      <Grid container spacing={2}>
        <Grid size={{ xs: 6, md: 3 }}>
          <StatTile
            label="Revenue"
            value={formatPence(totals.revenuePence)}
            hint={`${totals.purchases} ${totals.purchases === 1 ? "purchase" : "purchases"}`}
          />
        </Grid>
        <Grid size={{ xs: 6, md: 3 }}>
          <StatTile
            label="Credits sold"
            value={totals.credits.toLocaleString("en-GB")}
            hint={periodLabel}
          />
        </Grid>
        <Grid size={{ xs: 6, md: 3 }}>
          <StatTile
            label="Reports bought"
            value={totals.reports.toLocaleString("en-GB")}
            hint={periodLabel}
          />
        </Grid>
        <Grid size={{ xs: 6, md: 3 }}>
          <StatTile
            label="Failed payments"
            value={totals.failed.toLocaleString("en-GB")}
            hint={periodLabel}
            tone={totals.failed > 0 ? "error" : undefined}
          />
        </Grid>
      </Grid>

      <Card sx={{ p: 2.5 }}>
        <Typography variant="h6" component="h2">
          Revenue by month
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
          Paid credit purchases, last 12 months
        </Typography>
        <RevenueChart months={data.months} />
      </Card>

      <Card sx={{ overflowX: "auto" }}>
        <Typography variant="h6" component="h2" sx={{ px: 2.5, py: 2 }}>
          Month by month
        </Typography>
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell sx={{ pl: 2.5 }}>Month</TableCell>
              <TableCell align="right">Purchases</TableCell>
              <TableCell align="right">Credits sold</TableCell>
              <TableCell align="right">Reports bought</TableCell>
              <TableCell align="right">Failed</TableCell>
              <TableCell align="right" sx={{ pr: 2.5 }}>
                Revenue
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {[...data.months].reverse().map((m) => (
              <TableRow key={m.key} hover>
                <TableCell sx={{ pl: 2.5 }}>{m.label}</TableCell>
                <TableCell align="right">{m.purchases}</TableCell>
                <TableCell align="right">{m.credits}</TableCell>
                <TableCell align="right">{m.reports}</TableCell>
                <TableCell align="right">{m.failed}</TableCell>
                <TableCell align="right" sx={{ pr: 2.5, fontWeight: 600 }}>
                  {formatPence(m.revenuePence)}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Card>

      <Grid container spacing={2}>
        <Grid size={{ xs: 12, md: 6 }}>
          <Card sx={{ height: "100%" }}>
            <Typography variant="h6" component="h2" sx={{ px: 2.5, pt: 2, pb: 1 }}>
              Top customers
            </Typography>
            <Typography variant="body2" color="text.secondary" sx={{ px: 2.5, pb: 1 }}>
              By reports bought, {periodLabel}
            </Typography>
            <Table size="small">
              <TableBody>
                {data.topCustomers.map((c) => (
                  <TableRow key={c.token} hover>
                    <TableCell sx={{ pl: 2.5 }}>
                      <Link
                        href={`/admin/customers/${c.token}`}
                        style={{ fontWeight: 600, color: "inherit" }}
                      >
                        {c.name}
                      </Link>
                      {c.company ? (
                        <Typography variant="body2" color="text.secondary">
                          {c.company}
                        </Typography>
                      ) : null}
                    </TableCell>
                    <TableCell align="right" sx={{ fontWeight: 600 }}>
                      {c.reports} {c.reports === 1 ? "report" : "reports"}
                    </TableCell>
                    <TableCell align="right" sx={{ pr: 2.5, color: "text.secondary" }}>
                      {c.revenuePence > 0 ? formatPence(c.revenuePence) : "—"}
                    </TableCell>
                  </TableRow>
                ))}
                {data.topCustomers.length === 0 ? (
                  <TableRow>
                    <TableCell sx={{ py: 4, textAlign: "center", color: "text.secondary" }}>
                      No activity in this period.
                    </TableCell>
                  </TableRow>
                ) : null}
              </TableBody>
            </Table>
          </Card>
        </Grid>

        <Grid size={{ xs: 12, md: 6 }}>
          <Card sx={{ height: "100%" }}>
            <Typography variant="h6" component="h2" sx={{ px: 2.5, pt: 2, pb: 1 }}>
              Customers
            </Typography>
            <Box sx={{ px: 2.5, pb: 2 }}>
              <Stack spacing={1}>
                <Typography>
                  <strong>{customers.total.toLocaleString("en-GB")}</strong> customers hold{" "}
                  <strong>{customers.creditsOutstanding.toLocaleString("en-GB")}</strong> credits
                </Typography>
                <Typography>
                  <Link href="/admin/customers?filter=low">
                    {customers.lowBalance} with zero or negative balance
                  </Link>
                  {customers.negativeBalance > 0 ? (
                    <>
                      {" "}
                      (
                      <Link href="/admin/customers?filter=negative">
                        {customers.negativeBalance} negative
                      </Link>
                      )
                    </>
                  ) : null}
                </Typography>
              </Stack>
            </Box>
          </Card>
        </Grid>
      </Grid>

      <Card sx={{ overflowX: "auto" }}>
        <Stack
          direction="row"
          justifyContent="space-between"
          alignItems="center"
          sx={{ px: 2.5, py: 2 }}
        >
          <Typography variant="h6" component="h2">
            Needs attention
          </Typography>
          <Link href={withParams("/admin/transactions", { kind: "failed", ...sinceParam })}>
            All failed payments
          </Link>
        </Stack>
        <Table size="small">
          <TableBody>
            {attention.unappliedCount > 0 ? (
              <TableRow>
                <TableCell sx={{ pl: 2.5 }}>
                  {attention.unappliedCount >= 5 ? "5 or more" : attention.unappliedCount} paid{" "}
                  {attention.unappliedCount === 1 ? "purchase has" : "purchases have"} not had{" "}
                  credits applied to the customer’s balance yet.
                </TableCell>
              </TableRow>
            ) : null}
            {attention.failed.map((f) => (
              <TableRow key={f.id}>
                <TableCell sx={{ pl: 2.5 }}>
                  Failed payment
                  {f.customer ? (
                    <>
                      {" "}
                      from{" "}
                      <Link href={`/admin/customers/${f.customer.token}`}>{f.customer.name}</Link>
                    </>
                  ) : null}
                  {f.value ? ` (${formatMoney(f.value)})` : ""} on {formatDate(f.createdOn)}
                  {f.detail ? (
                    <Typography component="span" color="text.secondary">
                      {" "}
                      — {f.detail}
                    </Typography>
                  ) : null}
                </TableCell>
              </TableRow>
            ))}
            {attention.unappliedCount === 0 && attention.failed.length === 0 ? (
              <TableRow>
                <TableCell sx={{ py: 4, textAlign: "center", color: "text.secondary" }}>
                  Nothing needs attention.
                </TableCell>
              </TableRow>
            ) : null}
          </TableBody>
        </Table>
      </Card>
    </Stack>
  );
}
