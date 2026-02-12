"use client";

import { useState } from "react";
import {
  Alert,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Stack,
  TextField,
} from "@mui/material";
import { useRouter } from "next/navigation";

const EMPTY = { firstName: "", lastName: "", email: "" };

export default function AddCandidateDialog({ projectId }) {
  const router = useRouter();
  const [open, setOpen] = useState(false);
  const [form, setForm] = useState(EMPTY);
  const [error, setError] = useState("");
  const [saving, setSaving] = useState(false);

  function closeDialog() {
    if (saving) {
      return;
    }
    setOpen(false);
    setError("");
    setForm(EMPTY);
  }

  async function submit() {
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
      response = await fetch(`/api/projects/${projectId}/candidates`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
    } catch {
      setError("Network error while saving candidate.");
      setSaving(false);
      return;
    }

    if (!response.ok) {
      const errorPayload = await response.json().catch(() => ({}));
      setError(errorPayload.error || "Failed to add candidate.");
      setSaving(false);
      return;
    }

    setOpen(false);
    setSaving(false);
    setForm(EMPTY);
    router.refresh();
  }

  return (
    <>
      <Button variant="contained" onClick={() => setOpen(true)}>
        Add a New Candidate
      </Button>
      <Dialog open={open} onClose={closeDialog} fullWidth maxWidth="sm">
        <DialogTitle>Add Candidate</DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ mt: 1 }}>
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
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={closeDialog} disabled={saving}>
            Cancel
          </Button>
          <Button onClick={submit} variant="contained" disabled={saving}>
            {saving ? "Saving..." : "Save"}
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
