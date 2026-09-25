import { Box, Tooltip, Typography } from "@mui/material";
import { formatPence } from "@/lib/admin/report-math";

const HEIGHT = 160;

/** A round-number ceiling so the gridlines land on clean values. */
function niceCeiling(maxPence) {
  if (maxPence <= 0) return 10000;
  const pounds = maxPence / 100;
  const magnitude = 10 ** Math.floor(Math.log10(pounds));
  const step = [1, 2, 2.5, 5, 10].map((m) => m * magnitude).find((s) => s * 4 >= pounds) ?? pounds;
  return Math.ceil(pounds / step) * step * 100;
}

const compact = (pence) =>
  new Intl.NumberFormat("en-GB", {
    style: "currency",
    currency: "GBP",
    notation: "compact",
    maximumFractionDigits: 1,
  }).format(pence / 100);

/** Monthly revenue columns. One series, so no legend; the table below carries every value. */
export default function RevenueChart({ months }) {
  const ceiling = niceCeiling(Math.max(...months.map((m) => m.revenuePence)));
  const ticks = [1, 0.75, 0.5, 0.25, 0].map((f) => Math.round(ceiling * f));

  return (
    <Box role="img" aria-label="Revenue by month; the table below has the same figures">
      <Box sx={{ display: "flex", gap: 1.5 }}>
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            justifyContent: "space-between",
            height: HEIGHT,
            textAlign: "right",
            minWidth: 44,
          }}
        >
          {ticks.map((t) => (
            <Typography key={t} variant="caption" color="text.secondary" sx={{ lineHeight: 1 }}>
              {compact(t)}
            </Typography>
          ))}
        </Box>
        <Box
          sx={{
            flex: 1,
            position: "relative",
            height: HEIGHT,
            display: "flex",
            alignItems: "flex-end",
            justifyContent: "space-around",
            borderBottom: "1px solid",
            borderColor: "divider",
          }}
        >
          {[0, 25, 50, 75].map((pct) => (
            <Box
              key={pct}
              aria-hidden
              sx={{
                position: "absolute",
                left: 0,
                right: 0,
                top: `${pct}%`,
                borderTop: "1px solid",
                borderColor: "#f0f2f4",
              }}
            />
          ))}
          {months.map((m) => (
            <Tooltip
              key={m.key}
              arrow
              title={`${m.label}: ${formatPence(m.revenuePence)} from ${m.credits} credits · ${m.reports} reports bought`}
            >
              <Box
                sx={{
                  position: "relative",
                  flex: 1,
                  height: "100%",
                  display: "flex",
                  alignItems: "flex-end",
                  justifyContent: "center",
                }}
              >
                <Box
                  sx={{
                    width: "60%",
                    maxWidth: 24,
                    minHeight: m.revenuePence > 0 ? 2 : 0,
                    height: `${(m.revenuePence / ceiling) * 100}%`,
                    bgcolor: "primary.main",
                    borderRadius: "4px 4px 0 0",
                  }}
                />
              </Box>
            </Tooltip>
          ))}
        </Box>
      </Box>
      <Box sx={{ display: "flex", gap: 1.5, mt: 0.75 }}>
        <Box sx={{ minWidth: 44 }} />
        <Box sx={{ flex: 1, display: "flex" }}>
          {months.map((m, i) => (
            <Typography
              key={m.key}
              variant="caption"
              color="text.secondary"
              sx={{
                flex: 1,
                textAlign: "center",
                visibility: i % 2 === (months.length - 1) % 2 ? "visible" : "hidden",
              }}
            >
              {m.label.split(" ")[0]}
            </Typography>
          ))}
        </Box>
      </Box>
    </Box>
  );
}
