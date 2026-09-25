import Link from "next/link";
import { notFound } from "next/navigation";
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
import { getCustomerDetail, listTransactions } from "@/lib/admin/queries";
import { withParams } from "@/lib/admin/params";
import { formatDate, formatPence } from "@/lib/admin/report-math";
import { decodeCustomerId, encodeProjectId } from "@/lib/public-ids";
import StatTile from "@/components/admin/StatTile";
import TransactionsTable from "@/components/admin/TransactionsTable";

const RECENT = 15;

export default async function AdminCustomerPage({ params }) {
  const { id: token } = await params;
  const customerId = decodeCustomerId(token);
  if (!customerId) notFound();

  const detail = await getCustomerDetail(customerId);
  if (!detail) notFound();
  const { user, stats, projects, ledgerDifference } = detail;

  const [purchases, reports] = await Promise.all([
    listTransactions({ kind: "purchase", customerId }, 0, RECENT),
    listTransactions({ kind: "report", customerId }, 0, RECENT),
  ]);

  const address = [user.address1, user.address2, user.city, user.county, user.postcode].filter(
    Boolean,
  );
  const name = `${user.firstName} ${user.lastName}`.trim();

  return (
    <Stack spacing={3}>
      <Box>
        <Link href="/admin/customers" style={{ fontSize: 14, fontWeight: 500 }}>
          ← All customers
        </Link>
        <Typography variant="h2" sx={{ mt: 1 }}>
          {name}
        </Typography>
        <Typography color="text.secondary">
          {[user.company, user.email, user.phoneNumber].filter(Boolean).join(" · ")}
        </Typography>
        {address.length ? (
          <Typography color="text.secondary">{address.join(", ")}</Typography>
        ) : null}
        <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
          Username {user.username} · joined {formatDate(user.createdOn)}
        </Typography>
      </Box>

      <Grid container spacing={2}>
        <Grid size={{ xs: 6, md: 3 }}>
          <StatTile
            label="Credit balance"
            value={user.creditBalance ?? "—"}
            tone={(user.creditBalance ?? 0) < 0 ? "error" : undefined}
            hint={
              ledgerDifference
                ? `${ledgerDifference > 0 ? "+" : ""}${ledgerDifference} vs purchases less reports`
                : undefined
            }
          />
        </Grid>
        <Grid size={{ xs: 6, md: 3 }}>
          <StatTile
            label="Total paid"
            value={formatPence(stats.spendPence)}
            hint={`${stats.purchases} ${stats.purchases === 1 ? "purchase" : "purchases"}`}
          />
        </Grid>
        <Grid size={{ xs: 6, md: 3 }}>
          <StatTile
            label="Reports bought"
            value={stats.reportsBought}
            hint={`${stats.projects} projects`}
          />
        </Grid>
        <Grid size={{ xs: 6, md: 3 }}>
          <StatTile
            label="Last purchase"
            value={formatDate(stats.lastPurchase)}
            hint={stats.failedPayments ? `${stats.failedPayments} failed payments` : undefined}
          />
        </Grid>
      </Grid>

      <Section
        title="Credit purchases"
        count={purchases.total}
        href={withParams("/admin/transactions", { customer: token, kind: "purchase" })}
      >
        <TransactionsTable
          rows={purchases.rows}
          showCustomer={false}
          emptyText="No credit purchases."
        />
      </Section>

      <Section
        title="Reports bought"
        count={reports.total}
        href={withParams("/admin/transactions", { customer: token, kind: "report" })}
      >
        <TransactionsTable
          rows={reports.rows}
          showCustomer={false}
          emptyText="No reports bought."
        />
      </Section>

      <Section title="Projects" count={stats.projects}>
        <Card sx={{ overflowX: "auto" }}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell sx={{ pl: 2.5 }}>Project</TableCell>
                <TableCell>Cost code</TableCell>
                <TableCell align="right" sx={{ pr: 2.5 }}>
                  Created
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {projects.map((p) => (
                <TableRow key={encodeProjectId(p.id)} hover>
                  <TableCell sx={{ pl: 2.5, fontWeight: 600 }}>{p.projectTitle}</TableCell>
                  <TableCell sx={{ color: "text.secondary" }}>{p.costcode || "—"}</TableCell>
                  <TableCell align="right" sx={{ pr: 2.5, color: "text.secondary" }}>
                    {formatDate(p.createdOn)}
                  </TableCell>
                </TableRow>
              ))}
              {projects.length === 0 ? (
                <TableRow>
                  <TableCell
                    colSpan={3}
                    sx={{ py: 5, textAlign: "center", color: "text.secondary" }}
                  >
                    No projects.
                  </TableCell>
                </TableRow>
              ) : null}
            </TableBody>
          </Table>
        </Card>
      </Section>
    </Stack>
  );
}

function Section({ title, count, href, children }) {
  return (
    <Stack spacing={1.5}>
      <Stack direction="row" justifyContent="space-between" alignItems="baseline">
        <Typography variant="h6" component="h2">
          {title}{" "}
          <Typography component="span" color="text.secondary">
            ({count})
          </Typography>
        </Typography>
        {href && count > RECENT ? (
          <Button href={href} size="small">
            View all
          </Button>
        ) : null}
      </Stack>
      {children}
    </Stack>
  );
}
