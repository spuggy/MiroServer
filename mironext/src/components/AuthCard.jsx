import Image from "next/image";
import { Paper, Stack, Typography } from "@mui/material";

// Logo + bordered card shared by the sign-in and password-reset screens.
export default function AuthCard({ title, subtitle = undefined, children }) {
  return (
    <Stack spacing={3} alignItems="center" sx={{ width: "100%", maxWidth: 400 }}>
      <Image
        src="/miro-logo-small.png"
        alt="MiRo — understanding people"
        width={114}
        height={49}
        priority
      />
      <Paper
        sx={{ width: "100%", p: { xs: 3, sm: 4 }, border: "1px solid", borderColor: "divider" }}
        elevation={0}
      >
        <Typography variant="h5" component="h1" sx={{ fontWeight: 700 }}>
          {title}
        </Typography>
        {subtitle ? (
          <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5, mb: 3 }}>
            {subtitle}
          </Typography>
        ) : null}
        {children}
      </Paper>
    </Stack>
  );
}
