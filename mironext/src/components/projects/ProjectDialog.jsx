"use client";

import AddIcon from "@mui/icons-material/Add";
import EditIcon from "@mui/icons-material/Edit";
import { useState } from "react";
import {
  Alert,
  Button,
  Checkbox,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControlLabel,
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
  bccPractitioner: false,
};

/**
 * Create a project, or edit one when `projectId` and `initialValues` are given.
 */
export default function ProjectDialog({ projectId, initialValues, practitionerEmail }) {
  const router = useRouter();
  const isEdit = Boolean(projectId);
  const startState = isEdit ? { ...INITIAL_STATE, ...initialValues } : INITIAL_STATE;
  const [open, setOpen] = useState(false);
  const [form, setForm] = useState(startState);
  const [error, setError] = useState("");
  const [saving, setSaving] = useState(false);

  function openDialog() {
    setForm(startState);
    setError("");
    setOpen(true);
  }

  function field(name) {
    return {
      value: form[name],
      onChange: (event) => setForm({ ...form, [name]: event.target.value }),
    };
  }

  async function submit() {
    setSaving(true);
    setError("");

    const response = await fetch(isEdit ? `/api/projects/${projectId}` : "/api/projects", {
      method: isEdit ? "PUT" : "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(form),
    });

    if (!response.ok) {
      const payload = await response.json().catch(() => ({}));
      setError(payload.error || `Failed to ${isEdit ? "update" : "create"} project.`);
      setSaving(false);
      return;
    }

    setOpen(false);
    setSaving(false);
    if (!isEdit) {
      setForm(INITIAL_STATE);
    }
    router.refresh();
  }

  return (
    <>
      {isEdit ? (
        <Button variant="outlined" size="large" startIcon={<EditIcon />} onClick={openDialog}>
          Edit project
        </Button>
      ) : (
        <Button variant="contained" size="large" startIcon={<AddIcon />} onClick={openDialog}>
          New project
        </Button>
      )}
      <Dialog open={open} onClose={() => setOpen(false)} fullWidth maxWidth="sm">
        <DialogTitle>{isEdit ? "Edit Project" : "Add New Project"}</DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ mt: 1 }}>
            <TextField
              label="Project Title"
              {...field("projectTitle")}
              required
              inputProps={{ maxLength: 100 }}
            />
            <TextField
              label="Cost Code"
              {...field("costcode")}
              required
              inputProps={{ maxLength: 50 }}
            />
            <TextField
              label="Description"
              {...field("projectDescription")}
              required
              multiline
              minRows={2}
              inputProps={{ maxLength: 254 }}
            />
            <TextField
              label="Invite Email Subject"
              {...field("emailInviteSubject")}
              required
              inputProps={{ maxLength: 50 }}
            />
            <FormControlLabel
              control={
                <Checkbox
                  checked={form.bccPractitioner}
                  onChange={(event) => setForm({ ...form, bccPractitioner: event.target.checked })}
                />
              }
              label={`Copy email invites to ${practitionerEmail || "me"}`}
            />
            <TextField
              label="Invite Email Text"
              {...field("emailInviteText")}
              required
              multiline
              minRows={4}
              inputProps={{ maxLength: 100 }}
              helperText={`${form.emailInviteText.length}/100`}
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
