"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { Alert, Button, Stack, TextField } from "@mui/material";

export default function EditCandidateForm({ projectId, candidateId, firstName, lastName, email }) {
  const router = useRouter();
  const [form, setForm] = useState({ firstName, lastName, email });
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  const candidatePath = `/projects/${projectId}/candidates/${candidateId}`;

  async function submit(event) {
    event.preventDefault();

    const payload = {
      firstName: form.firstName.trim(),
      lastName: form.lastName.trim(),
      email: form.email.trim(),
    };

    if (!payload.firstName || !payload.lastName || !payload.email) {
      setError("First name, last name and email are required.");
      return;
    }

    setSaving(true);
    setError("");

    let response;
    try {
      response = await fetch(`/api/projects/${projectId}/candidates/${candidateId}`, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
    } catch {
      setError("Network error while updating candidate.");
      setSaving(false);
      return;
    }

    if (!response.ok) {
      const payloadError = await response.json().catch(() => ({}));
      setError(payloadError.error || "Failed to update candidate.");
      setSaving(false);
      return;
    }

    router.push(candidatePath);
    router.refresh();
  }

  return (
    <Stack component="form" spacing={2} onSubmit={submit}>
      <TextField
        required
        label="First Name"
        value={form.firstName}
        onChange={(event) => setForm({ ...form, firstName: event.target.value })}
      />
      <TextField
        required
        label="Last Name"
        value={form.lastName}
        onChange={(event) => setForm({ ...form, lastName: event.target.value })}
      />
      <TextField
        required
        label="Email"
        type="email"
        value={form.email}
        onChange={(event) => setForm({ ...form, email: event.target.value })}
      />
      {error ? <Alert severity="error">{error}</Alert> : null}
      <Stack direction="row" spacing={1}>
        <Button type="submit" variant="contained" disabled={saving}>
          {saving ? "Saving..." : "Save Changes"}
        </Button>
        <Button
          type="button"
          variant="outlined"
          disabled={saving}
          onClick={() => router.push(candidatePath)}
        >
          Cancel
        </Button>
      </Stack>
    </Stack>
  );
}
