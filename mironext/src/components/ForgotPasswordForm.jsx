"use client";

import { useState } from "react";
import NextLink from "next/link";
import { Alert, Box, Button, Link, Stack, TextField, Typography } from "@mui/material";
import AuthCard from "@/components/AuthCard";

export default function ForgotPasswordForm() {
  const [identifier, setIdentifier] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [sent, setSent] = useState(false);
  const [devResetUrl, setDevResetUrl] = useState("");

  async function onSubmit(event) {
    event.preventDefault();
    setLoading(true);
    setError("");

    try {
      const response = await fetch("/api/password-reset", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ identifier }),
      });
      if (!response.ok) throw new Error();
      const data = await response.json();
      setDevResetUrl(data.devResetUrl ?? "");
      setSent(true);
    } catch {
      setError("Something went wrong. Please try again.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <AuthCard
      title="Reset password"
      subtitle="Enter your username or email and we'll send you a reset link."
    >
      {sent ? (
        <Stack spacing={2}>
          <Alert severity="success">
            If an account matches, a reset link is on its way. It expires in 60 minutes.
          </Alert>
          {devResetUrl ? (
            <Alert severity="info">
              Email isn't configured. Dev link:{" "}
              <Link component={NextLink} href={new URL(devResetUrl).pathname}>
                open reset page
              </Link>
            </Alert>
          ) : null}
          <Link component={NextLink} href="/login" variant="body2">
            Back to sign in
          </Link>
        </Stack>
      ) : (
        <Box component="form" onSubmit={onSubmit}>
          <Stack spacing={2}>
            <TextField
              required
              label="Username or email"
              autoComplete="username"
              value={identifier}
              onChange={(event) => setIdentifier(event.target.value)}
            />
            {error ? <Alert severity="error">{error}</Alert> : null}
            <Button type="submit" variant="contained" size="large" disabled={loading}>
              {loading ? "Sending…" : "Send reset link"}
            </Button>
            <Typography variant="body2" textAlign="center">
              <Link component={NextLink} href="/login">
                Back to sign in
              </Link>
            </Typography>
          </Stack>
        </Box>
      )}
    </AuthCard>
  );
}
