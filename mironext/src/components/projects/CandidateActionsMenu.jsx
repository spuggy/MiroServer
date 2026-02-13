"use client";

import { useState } from "react";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import { useRouter } from "next/navigation";
import {
  Alert,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  IconButton,
  Menu,
  MenuItem,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import { toCandidateStatusLabel } from "@/lib/status";

function toDisplayDate(value) {
  if (!value) {
    return "-";
  }

  const date = new Date(value);
  if (Number.isNaN(date.getTime())) {
    return "-";
  }

  return date.toLocaleString();
}

export default function CandidateActionsMenu({
  projectId,
  candidateId,
  firstName,
  lastName,
  email,
  status,
  surveyId,
  createdOn,
  updatedAt,
}) {
  const router = useRouter();
  const [anchorEl, setAnchorEl] = useState(null);
  const [viewOpen, setViewOpen] = useState(false);
  const [editOpen, setEditOpen] = useState(false);
  const [deleteOpen, setDeleteOpen] = useState(false);
  const [form, setForm] = useState({ firstName, lastName, email });
  const [saveError, setSaveError] = useState("");
  const [deleteError, setDeleteError] = useState("");
  const [saving, setSaving] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const open = Boolean(anchorEl);

  const candidateName = `${firstName} ${lastName}`;

  function closeMenu() {
    setAnchorEl(null);
  }

  function closeViewDialog() {
    setViewOpen(false);
  }

  function openViewDialog() {
    closeMenu();
    setViewOpen(true);
  }

  function closeEditDialog() {
    if (saving) {
      return;
    }
    setEditOpen(false);
    setSaveError("");
    setForm({ firstName, lastName, email });
  }

  function openEditDialog() {
    closeMenu();
    setSaveError("");
    setForm({ firstName, lastName, email });
    setEditOpen(true);
  }

  function closeDeleteDialog() {
    if (deleting) {
      return;
    }
    setDeleteOpen(false);
    setDeleteError("");
  }

  function openDeleteDialog() {
    closeMenu();
    setDeleteError("");
    setDeleteOpen(true);
  }

  async function submitEdit() {
    const payload = {
      firstName: form.firstName.trim(),
      lastName: form.lastName.trim(),
      email: form.email.trim(),
    };

    if (!payload.firstName || !payload.lastName || !payload.email) {
      setSaveError("First name, last name and email are required.");
      return;
    }

    setSaving(true);
    setSaveError("");

    let response;
    try {
      response = await fetch(`/api/projects/${projectId}/candidates/${candidateId}`, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
    } catch {
      setSaveError("Network error while updating candidate.");
      setSaving(false);
      return;
    }

    if (!response.ok) {
      const payloadError = await response.json().catch(() => ({}));
      setSaveError(payloadError.error || "Failed to update candidate.");
      setSaving(false);
      return;
    }

    setSaving(false);
    setEditOpen(false);
    router.refresh();
  }

  async function submitDelete() {
    setDeleting(true);
    setDeleteError("");

    let response;
    try {
      response = await fetch(`/api/projects/${projectId}/candidates/${candidateId}`, {
        method: "DELETE",
      });
    } catch {
      setDeleteError("Network error while deleting candidate.");
      setDeleting(false);
      return;
    }

    if (!response.ok) {
      const errorPayload = await response.json().catch(() => ({}));
      setDeleteError(errorPayload.error || "Failed to delete candidate.");
      setDeleting(false);
      return;
    }

    setDeleteOpen(false);
    setDeleting(false);
    router.refresh();
  }

  return (
    <>
      <IconButton
        size="small"
        aria-label="Open candidate actions"
        onClick={(event) => setAnchorEl(event.currentTarget)}
      >
        <MoreVertIcon fontSize="small" />
      </IconButton>
      <Menu anchorEl={anchorEl} open={open} onClose={closeMenu}>
        <MenuItem onClick={openViewDialog}>View details</MenuItem>
        <MenuItem onClick={openEditDialog}>Edit candidate</MenuItem>
        <MenuItem onClick={openDeleteDialog}>Delete candidate</MenuItem>
      </Menu>

      <Dialog open={viewOpen} onClose={closeViewDialog} fullWidth maxWidth="sm">
        <DialogTitle>Candidate Details</DialogTitle>
        <DialogContent>
          <Stack spacing={1.5} sx={{ mt: 1 }}>
            <Typography>
              <strong>Name:</strong> {candidateName}
            </Typography>
            <Typography>
              <strong>Email:</strong> {email}
            </Typography>
            <Typography>
              <strong>Status:</strong> {toCandidateStatusLabel(status)}
            </Typography>
            <Typography>
              <strong>Survey:</strong> {surveyId || "-"}
            </Typography>
            <Typography>
              <strong>Created:</strong> {toDisplayDate(createdOn)}
            </Typography>
            <Typography>
              <strong>Last Updated:</strong> {toDisplayDate(updatedAt)}
            </Typography>
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={closeViewDialog}>Close</Button>
        </DialogActions>
      </Dialog>

      <Dialog open={editOpen} onClose={closeEditDialog} fullWidth maxWidth="sm">
        <DialogTitle>Edit Candidate</DialogTitle>
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
            {saveError ? <Alert severity="error">{saveError}</Alert> : null}
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={closeEditDialog} disabled={saving}>
            Cancel
          </Button>
          <Button onClick={submitEdit} variant="contained" disabled={saving}>
            {saving ? "Saving..." : "Save"}
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={deleteOpen} onClose={closeDeleteDialog} fullWidth maxWidth="xs">
        <DialogTitle>Delete Candidate</DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ mt: 1 }}>
            <Typography color="text.secondary">
              This will remove {candidateName} from this project.
            </Typography>
            {deleteError ? <Alert severity="error">{deleteError}</Alert> : null}
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={closeDeleteDialog} disabled={deleting}>
            Cancel
          </Button>
          <Button color="error" variant="contained" onClick={submitDelete} disabled={deleting}>
            {deleting ? "Deleting..." : "Delete Candidate"}
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
