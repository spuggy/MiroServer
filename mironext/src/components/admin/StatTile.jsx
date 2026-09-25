import { Card, Typography } from "@mui/material";

export default function StatTile({ label, value, hint, tone }) {
  return (
    <Card sx={{ p: 2.5, height: "100%" }}>
      <Typography variant="body2" color="text.secondary">
        {label}
      </Typography>
      <Typography
        component="p"
        sx={{
          mt: 0.5,
          fontSize: 28,
          fontWeight: 700,
          letterSpacing: "-0.3px",
          color: tone === "error" ? "error.main" : "text.primary",
        }}
      >
        {value}
      </Typography>
      {hint ? (
        <Typography variant="body2" color="text.secondary" sx={{ mt: 0.25 }}>
          {hint}
        </Typography>
      ) : null}
    </Card>
  );
}
