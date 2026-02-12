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

const INITIAL_STATE = {
  projectTitle: "",
  projectDescription: "",
  costcode: "STANDARD",
  emailInviteSubject: "You have been invited to Miro",
  emailInviteText: "Please complete your Miro assessment.",
};

export default function NewProjectDialog() {
  const router = useRouter();
  const [open, setOpen] = useState(false);
  const [form, setForm] = useState(INITIAL_STATE);
  const [error, setError] = useState("");
  const [saving, setSaving] = useState(false);

  async function submit() {
    setSaving(true);
    setError("");

    const response = await fetch("/api/projects", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(form),
    });

    if (!response.ok) {
      const payload = await response.json().catch(() => ({}));
      setError(payload.error || "Failed to create project.");
      setSaving(false);
      return;
    }

    setOpen(false);
    setSaving(false);
    setForm(INITIAL_STATE);
    router.refresh();
  }

  return (
    <>
      <Button variant="contained" onClick={() => setOpen(true)}>
        Add New Project
      </Button>
      <Dialog open={open} onClose={() => setOpen(false)} fullWidth maxWidth="sm">
        <DialogTitle>Add New Project</DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ mt: 1 }}>
            <TextField
              label="Project Title"
              value={form.projectTitle}
              onChange={(event) => setForm({ ...form, projectTitle: event.target.value })}
              required
            />
            <TextField
              label="Description"
              value={form.projectDescription}
              onChange={(event) => setForm({ ...form, projectDescription: event.target.value })}
              required
              multiline
              minRows={2}
            />
            <TextField
              label="Cost Code"
              value={form.costcode}
              onChange={(event) => setForm({ ...form, costcode: event.target.value })}
              required
            />
            <TextField
              label="Invite Email Subject"
              value={form.emailInviteSubject}
              onChange={(event) => setForm({ ...form, emailInviteSubject: event.target.value })}
              required
            />
            <TextField
              label="Invite Email Text"
              value={form.emailInviteText}
              onChange={(event) => setForm({ ...form, emailInviteText: event.target.value })}
              required
              multiline
              minRows={2}
            />
            {error ? <Alert severity="error">{error}</Alert> : null}
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpen(false)}>Cancel</Button>
          <Button onClick={submit} variant="contained" disabled={saving}>
            {saving ? "Saving..." : "Save"}
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
