import { Box } from "@mui/material";
import { toCandidateStatusLabel } from "@/lib/status";

// Tinted pill + dot; text colours are dark enough for 4.5:1 on their tint.
const STYLES = {
  0: { bg: "#eef0f3", fg: "#3f4756", dot: "#8a93a3" },
  10: { bg: "#e6f2f8", fg: "#0f5876", dot: "#2a8fbb" },
  20: { bg: "#e6f2f8", fg: "#0f5876", dot: "#2a8fbb" },
  30: { bg: "#fff4d6", fg: "#7a5300", dot: "#d99a00" },
  40: { bg: "#e7f6e9", fg: "#1d6b2a", dot: "#2f9a3e" },
};

export default function CandidateStatusChip({ status }) {
  const style = STYLES[status ?? 0] ?? STYLES[0];
  return (
    <Box
      component="span"
      sx={{
        display: "inline-flex",
        alignItems: "center",
        gap: 0.75,
        px: 1.25,
        py: 0.5,
        borderRadius: 999,
        bgcolor: style.bg,
        color: style.fg,
        fontSize: 13,
        fontWeight: 600,
        whiteSpace: "nowrap",
      }}
    >
      <Box component="span" sx={{ width: 7, height: 7, borderRadius: "50%", bgcolor: style.dot }} />
      {toCandidateStatusLabel(status)}
    </Box>
  );
}
