"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { Alert, Button, Stack, Typography } from "@mui/material";

export default function DeleteCandidateForm({ projectId, candidateId, candidateName }) {
  const router = useRouter();
  const [deleting, setDeleting] = useState(false);
  const [error, setError] = useState("");

  async function removeCandidate() {
    setDeleting(true);
    setError("");

    let response;
    try {
      response = await fetch(`/api/projects/${projectId}/candidates/${candidateId}`, {
        method: "DELETE",
      });
    } catch {
      setError("Network error while deleting candidate.");
      setDeleting(false);
      return;
    }

    if (!response.ok) {
      const payload = await response.json().catch(() => ({}));
      setError(payload.error || "Failed to delete candidate.");
      setDeleting(false);
      return;
    }

    router.push(`/projects/${projectId}`);
    router.refresh();
  }

  return (
    <Stack spacing={2}>
      <Typography color="text.secondary">
        This will remove {candidateName} from this project.
      </Typography>
      {error ? <Alert severity="error">{error}</Alert> : null}
      <Stack direction="row" spacing={1}>
        <Button color="error" variant="contained" disabled={deleting} onClick={removeCandidate}>
          {deleting ? "Deleting..." : "Delete Candidate"}
        </Button>
        <Button
          variant="outlined"
          disabled={deleting}
          onClick={() => router.push(`/projects/${projectId}/candidates/${candidateId}`)}
        >
          Cancel
        </Button>
      </Stack>
    </Stack>
  );
}
