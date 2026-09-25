import Link from "next/link";
import {
  Box,
  Card,
  InputAdornment,
  MenuItem,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TextField,
  Typography,
  Button,
} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import { CUSTOMER_FILTERS, CUSTOMER_SORTS, listCustomers } from "@/lib/admin/queries";
import { firstParam, withParams } from "@/lib/admin/params";
import { formatDate, formatPence } from "@/lib/admin/report-math";
import { getPagination } from "@/lib/pagination";
import Pager from "@/components/admin/Pager";

const has = (obj, key) => Object.prototype.hasOwnProperty.call(obj, key);

export default async function AdminCustomersPage({ searchParams }) {
  const params = await searchParams;
  const { page, pageSize, skip, take } = getPagination(params, { pageSize: 25, maxPageSize: 100 });
  const q = (firstParam(params, "q") ?? "").trim().slice(0, 100);
  const filterRaw = firstParam(params, "filter");
  const sortRaw = firstParam(params, "sort");
  const filter = has(CUSTOMER_FILTERS, filterRaw) ? filterRaw : "all";
  const sort = has(CUSTOMER_SORTS, sortRaw) ? sortRaw : "name";

  const { total, rows } = await listCustomers({ q, filter, sort, skip, take });
  const totalPages = Math.max(1, Math.ceil(total / pageSize));
  const hrefFor = (n) =>
    withParams("/admin/customers", {
      q,
      filter: filter === "all" ? "" : filter,
      sort: sort === "name" ? "" : sort,
      page: n,
    });

  return (
    <Stack spacing={3}>
      <Typography color="text.secondary">
        {total.toLocaleString("en-GB")} {total === 1 ? "customer" : "customers"}
        {filter !== "all" ? ` · ${CUSTOMER_FILTERS[filter].toLowerCase()}` : ""}
        {q ? ` matching “${q}”` : ""}
      </Typography>

      <Card>
        <Box
          component="form"
          method="get"
          role="search"
          sx={{
            display: "flex",
            flexWrap: "wrap",
            alignItems: "center",
            gap: 1.5,
            px: 2.5,
            py: 2,
            borderBottom: "1px solid",
            borderColor: "divider",
          }}
        >
          <TextField
            name="q"
            defaultValue={q}
            placeholder="Search name, email or company"
            inputProps={{ "aria-label": "Search customers" }}
            sx={{ width: { xs: "100%", sm: 300 } }}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon fontSize="small" />
                </InputAdornment>
              ),
            }}
          />
          <TextField select name="filter" defaultValue={filter} label="Show" sx={{ minWidth: 210 }}>
            {Object.entries(CUSTOMER_FILTERS).map(([value, label]) => (
              <MenuItem key={value} value={value}>
                {label}
              </MenuItem>
            ))}
          </TextField>
          <TextField select name="sort" defaultValue={sort} label="Sort by" sx={{ minWidth: 200 }}>
            {Object.entries(CUSTOMER_SORTS).map(([value, label]) => (
              <MenuItem key={value} value={value}>
                {label}
              </MenuItem>
            ))}
          </TextField>
          <Button type="submit" variant="contained">
            Apply
          </Button>
          {q || filter !== "all" || sort !== "name" ? (
            <Link href="/admin/customers" style={{ fontSize: 14, fontWeight: 500 }}>
              Clear
            </Link>
          ) : null}
        </Box>

        <Box sx={{ overflowX: "auto" }}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell sx={{ pl: 2.5 }}>Customer</TableCell>
                <TableCell align="right">Credits</TableCell>
                <TableCell align="right">Projects</TableCell>
                <TableCell align="right">Reports bought</TableCell>
                <TableCell align="right">Total paid</TableCell>
                <TableCell align="right" sx={{ pr: 2.5 }}>
                  Last purchase
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {rows.map((c) => (
                <TableRow key={c.token} hover>
                  <TableCell sx={{ pl: 2.5 }}>
                    <Link
                      href={`/admin/customers/${c.token}`}
                      style={{ fontWeight: 600, color: "inherit" }}
                    >
                      {c.name}
                    </Link>
                    <Typography variant="body2" color="text.secondary">
                      {[c.company, c.email].filter(Boolean).join(" · ")}
                    </Typography>
                  </TableCell>
                  <TableCell
                    align="right"
                    sx={{
                      fontWeight: 600,
                      color: (c.creditBalance ?? 0) < 0 ? "error.main" : "text.primary",
                    }}
                  >
                    {c.creditBalance ?? "—"}
                  </TableCell>
                  <TableCell align="right">{c.stats.projects}</TableCell>
                  <TableCell align="right">{c.stats.reportsBought}</TableCell>
                  <TableCell align="right">
                    {c.stats.spendPence > 0 ? formatPence(c.stats.spendPence) : "—"}
                  </TableCell>
                  <TableCell
                    align="right"
                    sx={{ pr: 2.5, color: "text.secondary", whiteSpace: "nowrap" }}
                  >
                    {formatDate(c.stats.lastPurchase)}
                  </TableCell>
                </TableRow>
              ))}
              {rows.length === 0 ? (
                <TableRow>
                  <TableCell
                    colSpan={6}
                    sx={{ py: 6, textAlign: "center", color: "text.secondary" }}
                  >
                    No customers match.
                  </TableCell>
                </TableRow>
              ) : null}
            </TableBody>
          </Table>
        </Box>
      </Card>

      <Pager page={page} totalPages={totalPages} hrefFor={hrefFor} />
    </Stack>
  );
}
