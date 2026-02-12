import { Card, CardContent, Typography } from "@mui/material";

export default function MorePage() {
  return (
    <Card>
      <CardContent>
        <Typography variant="h5" sx={{ mb: 1 }}>
          More
        </Typography>
        <Typography color="text.secondary">
          Additional migrated sections can be exposed from this menu.
        </Typography>
      </CardContent>
    </Card>
  );
}
