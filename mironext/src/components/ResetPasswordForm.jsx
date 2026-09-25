"use client";

import { useState } from "react";
import NextLink from "next/link";
import { Alert, Box, Button, Link, Stack, TextField } from "@mui/material";
import AuthCard from "@/components/AuthCard";

export default function ResetPasswordForm({ token, minLength }) {
  const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [done, setDone] = useState(false);

  async function onSubmit(event) {
    event.preventDefault();
    setError("");

    if (password.length < minLength) {
      setError(`Password must be at least ${minLength} characters.`);
      return;
    }
    if (password !== confirm) {
      setError("Passwords don't match.");
      return;
    }

    setLoading(true);
    try {
      const response = await fetch("/api/password-reset/confirm", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ token, password }),
      });
      const data = await response.json().catch(() => ({}));
      if (!response.ok) {
        setError(data.error || "Unable to reset your password.");
        return;
      }
      setDone(true);
    } catch {
      setError("Something went wrong. Please try again.");
    } finally {
      setLoading(false);
    }
  }

  if (done) {
    return (
      <AuthCard title="Password updated">
        <Stack spacing={2} sx={{ mt: 2 }}>
          <Alert severity="success">You can now sign in with your new password.</Alert>
          <Button component={NextLink} href="/login" variant="contained" size="large">
            Sign in
          </Button>
        </Stack>
      </AuthCard>
    );
  }

  return (
    <AuthCard title="Choose a new password" subtitle={`At least ${minLength} characters.`}>
      <Box component="form" onSubmit={onSubmit}>
        <Stack spacing={2}>
          <TextField
            required
            type="password"
            label="New password"
            autoComplete="new-password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
          />
          <TextField
            required
            type="password"
            label="Confirm new password"
            autoComplete="new-password"
            value={confirm}
            onChange={(event) => setConfirm(event.target.value)}
          />
          {error ? (
            <Alert severity="error">
              {error}{" "}
              {/link/.test(error) ? (
                <Link component={NextLink} href="/forgot-password">
                  Request a new link
                </Link>
              ) : null}
            </Alert>
          ) : null}
          <Button type="submit" variant="contained" size="large" disabled={loading}>
            {loading ? "Saving…" : "Set password"}
          </Button>
        </Stack>
      </Box>
    </AuthCard>
  );
}
