"use client";

import { useState } from "react";
import { signIn } from "next-auth/react";
import { useRouter } from "next/navigation";
import Image from "next/image";
import NextLink from "next/link";
import { Alert, Box, Button, Link, Paper, Stack, TextField, Typography } from "@mui/material";

export default function LoginForm() {
  const router = useRouter();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function onSubmit(event) {
    event.preventDefault();
    setLoading(true);
    setError("");

    const result = await signIn("credentials", {
      username,
      password,
      redirect: false,
    });

    setLoading(false);

    if (result?.error) {
      setError("Login failed. Check your username and password.");
      return;
    }

    router.push("/");
    router.refresh();
  }

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
          Sign in
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5, mb: 3 }}>
          Practitioner account
        </Typography>
        <Box component="form" onSubmit={onSubmit}>
          <Stack spacing={2}>
            <TextField
              required
              label="Username"
              autoComplete="username"
              value={username}
              onChange={(event) => setUsername(event.target.value)}
            />
            <TextField
              required
              type="password"
              label="Password"
              autoComplete="current-password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
            />
            {error ? <Alert severity="error">{error}</Alert> : null}
            <Button type="submit" variant="contained" size="large" disabled={loading}>
              {loading ? "Signing in…" : "Sign in"}
            </Button>
            <Typography variant="body2" textAlign="center">
              <Link component={NextLink} href="/forgot-password">
                Forgot password?
              </Link>
            </Typography>
          </Stack>
        </Box>
      </Paper>
    </Stack>
  );
}
