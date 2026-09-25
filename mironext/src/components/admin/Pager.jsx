import { Button, Stack, Typography } from "@mui/material";

/** Previous / next links for server-rendered pages; `hrefFor(n)` builds the page URL. */
export default function Pager({ page, totalPages, hrefFor }) {
  if (totalPages <= 1) return null;
  return (
    <Stack direction="row" justifyContent="space-between" alignItems="center">
      <Button href={hrefFor(Math.max(1, page - 1))} variant="outlined" disabled={page <= 1}>
        Previous
      </Button>
      <Typography variant="body2" color="text.secondary">
        Page {page} of {totalPages}
      </Typography>
      <Button
        href={hrefFor(Math.min(totalPages, page + 1))}
        variant="outlined"
        disabled={page >= totalPages}
      >
        Next
      </Button>
    </Stack>
  );
}
