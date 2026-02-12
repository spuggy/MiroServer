import { Card, CardContent, Typography } from "@mui/material";

export default function TeamReportsPage() {
  return (
    <Card>
      <CardContent>
        <Typography variant="h5" sx={{ mb: 1 }}>
          Team Reports
        </Typography>
        <Typography color="text.secondary">
          Team reports are not yet migrated in this phase.
        </Typography>
      </CardContent>
    </Card>
  );
}
