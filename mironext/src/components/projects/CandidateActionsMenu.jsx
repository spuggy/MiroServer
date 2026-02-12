"use client";

import { useState } from "react";
import Link from "next/link";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import { IconButton, Menu, MenuItem } from "@mui/material";

export default function CandidateActionsMenu({ projectId, candidateId }) {
  const [anchorEl, setAnchorEl] = useState(null);
  const open = Boolean(anchorEl);

  const baseHref = `/projects/${projectId}/candidates/${candidateId}`;

  function closeMenu() {
    setAnchorEl(null);
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
        <MenuItem component={Link} href={baseHref} onClick={closeMenu}>
          View details
        </MenuItem>
        <MenuItem component={Link} href={`${baseHref}/edit`} onClick={closeMenu}>
          Edit candidate
        </MenuItem>
        <MenuItem component={Link} href={`${baseHref}/delete`} onClick={closeMenu}>
          Delete candidate
        </MenuItem>
      </Menu>
    </>
  );
}
