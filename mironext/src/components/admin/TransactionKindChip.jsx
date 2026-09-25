import { Chip } from "@mui/material";
import { KIND_LABELS } from "@/lib/admin/report-math";

const COLOURS = { purchase: "success", report: "info", failed: "error", pending: "default" };

/** Status 2 is paid, but the legacy batch hasn't added the credits to the balance yet. */
export default function TransactionKindChip({ kind, status }) {
  const awaiting = kind === "purchase" && status === 2;
  return (
    <Chip
      size="small"
      variant={kind === "pending" ? "outlined" : "filled"}
      color={COLOURS[kind] ?? "default"}
      label={awaiting ? "Paid, credits pending" : KIND_LABELS[kind]}
    />
  );
}
