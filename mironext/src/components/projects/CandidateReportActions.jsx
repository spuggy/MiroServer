"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import {
  Alert,
  Button,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Link as MuiLink,
  ListItemText,
  Menu,
  MenuItem,
  Stack,
  Typography,
} from "@mui/material";
import DownloadIcon from "@mui/icons-material/FileDownloadOutlined";
import ArrowDropDownIcon from "@mui/icons-material/ArrowDropDown";

const INVITE_SENT = 10;
const ASSESSMENT_COMPLETE = 20;
const PURCHASE_REPORT = 30;
const DOWNLOAD_REPORT = 40;

// V11 assessments (survey 5) unlock the enhanced and leadership reports.
const SURVEY_ID_MIRO_V11 = 5;
const REPORTS = [
  { type: "v10", label: "MiRo Report", description: "Standard individual report", suffix: "" },
  {
    type: "v11",
    label: "MiRo Enhanced Report",
    description: "Adds Jungian functions and working styles",
    suffix: "_v11",
  },
  {
    type: "leadership",
    label: "MiRo Leadership Report",
    description: "Leadership style and disposition",
    suffix: "_lship",
  },
];

async function readError(response, fallback) {
  const body = await response.json().catch(() => ({}));
  return body.error || fallback;
}

/** Fetches the PDF (generated on demand) and hands it to the browser as a download. */
async function downloadReport(url, fallbackName) {
  const response = await fetch(url);
  if (!response.ok) throw new Error(await readError(response, "Unable to generate the report."));
  const blob = await response.blob();
  const disposition = response.headers.get("Content-Disposition") || "";
  const name = /filename="([^"]+)"/.exec(disposition)?.[1] || fallbackName;
  const href = URL.createObjectURL(blob);
  const a = document.createElement("a");
  a.href = href;
  a.download = name;
  document.body.appendChild(a);
  a.click();
  a.remove();
  setTimeout(() => URL.revokeObjectURL(href), 10_000);
}

export default function CandidateReportActions({
  projectId,
  candidateId,
  candidateName,
  status,
  surveyId,
}) {
  const router = useRouter();
  const [busy, setBusy] = useState(null); // "invite" | "buy" | "download"
  const [error, setError] = useState("");
  const [confirmBuy, setConfirmBuy] = useState(false);
  const [inviteResult, setInviteResult] = useState(null);
  const [menuAnchor, setMenuAnchor] = useState(null);

  const base = `/api/projects/${projectId}/candidates/${candidateId}`;
  const current = status ?? 0;
  const hasV11Reports = Number(surveyId) >= SURVEY_ID_MIRO_V11;
  const fileName = (report) => `${candidateName.replace(/\W+/g, "_")}${report.suffix}.pdf`;

  async function sendInvite() {
    setBusy("invite");
    setError("");
    try {
      const response = await fetch(`${base}/invite`, { method: "POST" });
      if (!response.ok) throw new Error(await readError(response, "Unable to send the invite."));
      setInviteResult(await response.json());
      router.refresh();
    } catch (e) {
      setError(e.message);
    } finally {
      setBusy(null);
    }
  }

  async function buy() {
    setConfirmBuy(false);
    setBusy("buy");
    setError("");
    try {
      const response = await fetch(`${base}/buy`, { method: "POST" });
      if (!response.ok) throw new Error(await readError(response, "Unable to buy the report."));
      const { downloadUrl } = await response.json();
      setBusy("download");
      await downloadReport(downloadUrl, fileName(REPORTS[0]));
      router.refresh();
    } catch (e) {
      setError(e.message);
      router.refresh();
    } finally {
      setBusy(null);
    }
  }

  async function download(report) {
    setMenuAnchor(null);
    setBusy("download");
    setError("");
    try {
      await downloadReport(`${base}/report?type=${report.type}`, fileName(report));
    } catch (e) {
      setError(e.message);
    } finally {
      setBusy(null);
    }
  }

  let action = null;
  if (current < ASSESSMENT_COMPLETE) {
    action = (
      <Button size="small" variant="outlined" onClick={sendInvite} disabled={Boolean(busy)}>
        {busy === "invite" ? "Sending…" : current >= INVITE_SENT ? "Resend invite" : "Send invite"}
      </Button>
    );
  } else if (current === PURCHASE_REPORT) {
    action = (
      <Button
        size="small"
        variant="contained"
        onClick={() => setConfirmBuy(true)}
        disabled={Boolean(busy)}
      >
        Buy report
      </Button>
    );
  } else if (current === DOWNLOAD_REPORT && !hasV11Reports) {
    action = (
      <Button
        size="small"
        variant="outlined"
        color="primary"
        onClick={() => download(REPORTS[0])}
        disabled={Boolean(busy)}
        startIcon={<DownloadIcon fontSize="small" />}
      >
        Download PDF
      </Button>
    );
  } else if (current === DOWNLOAD_REPORT) {
    action = (
      <>
        <Button
          size="small"
          variant="outlined"
          color="primary"
          onClick={(e) => setMenuAnchor(e.currentTarget)}
          disabled={Boolean(busy)}
          startIcon={<DownloadIcon fontSize="small" />}
          endIcon={<ArrowDropDownIcon fontSize="small" />}
          aria-haspopup="menu"
          aria-expanded={Boolean(menuAnchor)}
        >
          Download PDF
        </Button>
        <Menu
          anchorEl={menuAnchor}
          open={Boolean(menuAnchor)}
          onClose={() => setMenuAnchor(null)}
          anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
          transformOrigin={{ vertical: "top", horizontal: "right" }}
        >
          {REPORTS.map((report) => (
            <MenuItem key={report.type} onClick={() => download(report)}>
              <ListItemText primary={report.label} secondary={report.description} />
            </MenuItem>
          ))}
        </Menu>
      </>
    );
  }

  return (
    <>
      <Stack direction="row" spacing={1} alignItems="center" justifyContent="flex-end">
        {busy === "buy" || busy === "download" ? (
          <Stack direction="row" spacing={1} alignItems="center">
            <CircularProgress size={16} />
            <Typography variant="body2" color="text.secondary">
              {busy === "buy" ? "Buying…" : "Preparing PDF…"}
            </Typography>
          </Stack>
        ) : (
          action
        )}
      </Stack>
      {error ? (
        <Alert severity="error" sx={{ mt: 1, textAlign: "left" }} onClose={() => setError("")}>
          {error}
        </Alert>
      ) : null}

      <Dialog open={confirmBuy} onClose={() => setConfirmBuy(false)} maxWidth="xs" fullWidth>
        <DialogTitle>Buy report</DialogTitle>
        <DialogContent>
          <Typography>
            Buy the MiRo report for <strong>{candidateName}</strong>? This uses 1 credit. The PDF
            will download straight away.
            {hasV11Reports
              ? " The enhanced and leadership reports are included and can be downloaded afterwards."
              : ""}
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setConfirmBuy(false)}>Cancel</Button>
          <Button variant="contained" onClick={buy}>
            Buy and download
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog
        open={Boolean(inviteResult)}
        onClose={() => setInviteResult(null)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>Invite {inviteResult?.sent ? "sent" : "created"}</DialogTitle>
        <DialogContent>
          {inviteResult?.sent ? (
            <Typography>
              {candidateName} has been emailed a personal link to their assessment.
            </Typography>
          ) : (
            <Stack spacing={1.5}>
              <Alert severity="info">
                Email isn&apos;t configured (no RESEND_API_KEY), so nothing was sent. The email was
                written to the server log. Use this link to test:
              </Alert>
              <MuiLink
                href={inviteResult?.devInviteUrl}
                target="_blank"
                sx={{ wordBreak: "break-all" }}
              >
                {inviteResult?.devInviteUrl}
              </MuiLink>
            </Stack>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setInviteResult(null)}>Close</Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
