import { Card, CardContent, Typography } from "@mui/material";

export default function HelpPage() {
  return (
    <Card>
      <CardContent>
        <Typography variant="h5" sx={{ mb: 1 }}>
          Help
        </Typography>
        <Typography color="text.secondary">
          Help content can be migrated from legacy web pages.
        </Typography>
      </CardContent>
    </Card>
  );
}
