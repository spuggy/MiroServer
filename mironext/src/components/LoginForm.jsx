"use client";

import { useState } from "react";
import { signIn } from "next-auth/react";
import { useRouter } from "next/navigation";
import { Alert, Box, Button, Paper, Stack, TextField, Typography } from "@mui/material";

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

    router.push("/projects");
    router.refresh();
  }

  return (
    <Paper elevation={0} sx={{ width: "100%", maxWidth: 330, border: "1px solid #e5e5e5", p: 2 }}>
      <Box sx={{ pb: 1 }}>
        <Typography variant="h5" component="h1" sx={{ color: "#6b6b6b" }}>
          Sign In
        </Typography>
      </Box>
      <Box>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
          Use your existing `app_user` credentials.
        </Typography>
        <Box component="form" onSubmit={onSubmit}>
          <Stack spacing={2}>
            <TextField
              required
              label="Username"
              value={username}
              onChange={(event) => setUsername(event.target.value)}
            />
            <TextField
              required
              type="password"
              label="Password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
            />
            {error ? <Alert severity="error">{error}</Alert> : null}
            <Button type="submit" variant="contained" disabled={loading}>
              {loading ? "Signing in..." : "Sign In"}
            </Button>
          </Stack>
        </Box>
      </Box>
    </Paper>
  );
}
