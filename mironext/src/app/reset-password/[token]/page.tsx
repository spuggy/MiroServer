import type { Metadata } from "next";
import NextLink from "next/link";
import { Box, Button, Stack, Typography } from "@mui/material";
import AuthCard from "@/components/AuthCard";
import ResetPasswordForm from "@/components/ResetPasswordForm";
import { checkResetToken, MIN_PASSWORD_LENGTH } from "@/lib/password-reset";

export const dynamic = "force-dynamic";

export const metadata: Metadata = {
  title: "Reset password",
  robots: { index: false, follow: false },
  // Keep the token out of Referer headers sent to other sites.
  referrer: "no-referrer",
};

type Params = { params: Promise<{ token: string }> };

export default async function ResetPasswordPage({ params }: Params) {
  const { token } = await params;
  const state = await checkResetToken(token);

  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        px: 2,
        bgcolor: "background.default",
      }}
    >
      {state === "valid" ? (
        <ResetPasswordForm token={token} minLength={MIN_PASSWORD_LENGTH} />
      ) : (
        <AuthCard title={state === "expired" ? "This link has expired" : "This link isn't valid"}>
          <Stack spacing={2} sx={{ mt: 2 }}>
            <Typography>
              Reset links expire after an hour and can only be used once. Request a new one to
              continue.
            </Typography>
            <Button component={NextLink} href="/forgot-password" variant="contained">
              Request a new link
            </Button>
          </Stack>
        </AuthCard>
      )}
    </Box>
  );
}
