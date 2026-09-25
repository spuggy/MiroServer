"use client";

import { useState } from "react";
import Link from "next/link";
import Image from "next/image";
import { usePathname } from "next/navigation";
import {
  AppBar,
  Box,
  ButtonBase,
  Container,
  Divider,
  ListItemText,
  Menu,
  MenuItem,
  Toolbar,
  Typography,
} from "@mui/material";
import { signOut } from "next-auth/react";

const HELP_LINKS = [
  { href: "/help", label: "Help" },
  { href: "/more", label: "More" },
];

const PRACTITIONER_LINKS = [
  { href: "/projects", label: "My Projects" },
  { href: "/team-reports", label: "Team Reports" },
  ...HELP_LINKS,
];

// The super user has no projects or team reports of their own.
const ADMIN_LINKS = [{ href: "/admin", label: "Admin" }, ...HELP_LINKS];

function initials(name) {
  return (name || "")
    .split(/\s+/)
    .filter(Boolean)
    .slice(0, 2)
    .map((part) => part[0].toUpperCase())
    .join("");
}

export default function TopNav({ userName, email, creditBalance, isSysAdmin = false }) {
  const pathname = usePathname();
  const [anchorEl, setAnchorEl] = useState(null);

  return (
    <AppBar
      position="sticky"
      color="inherit"
      elevation={0}
      sx={{ bgcolor: "#ffffff", borderBottom: "1px solid", borderColor: "divider" }}
    >
      <Container maxWidth="lg" sx={{ px: { xs: 2, sm: 5 } }}>
        <Toolbar disableGutters sx={{ gap: { xs: 2, md: 5 }, minHeight: "72px !important" }}>
          <Link
            href={isSysAdmin ? "/admin" : "/projects"}
            aria-label="MiRo home"
            style={{ display: "flex" }}
          >
            <Image
              src="/miro-logo-small.png"
              alt="MiRo — understanding people"
              width={93}
              height={40}
              priority
            />
          </Link>

          <Box
            component="nav"
            aria-label="Main"
            sx={{ display: "flex", gap: 0.5, flex: 1, flexWrap: "wrap" }}
          >
            {(isSysAdmin ? ADMIN_LINKS : PRACTITIONER_LINKS).map((link) => {
              const active = pathname === link.href || pathname.startsWith(`${link.href}/`);
              return (
                <Box
                  key={link.href}
                  component={Link}
                  href={link.href}
                  aria-current={active ? "page" : undefined}
                  sx={{
                    px: 1.75,
                    py: 1,
                    borderRadius: 2,
                    fontSize: 15,
                    fontWeight: active ? 600 : 500,
                    color: active ? "primary.dark" : "#4a5263",
                    bgcolor: active ? "primary.light" : "transparent",
                    "&:hover": {
                      bgcolor: active ? "primary.light" : "#f2f4f6",
                      color: "text.primary",
                    },
                  }}
                >
                  {link.label}
                </Box>
              );
            })}
          </Box>

          <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
            {creditBalance != null ? (
              <Typography
                variant="body2"
                color="text.secondary"
                sx={{ display: { xs: "none", sm: "block" } }}
              >
                <Box component="strong" sx={{ color: "text.primary" }}>
                  {creditBalance}
                </Box>{" "}
                {creditBalance === 1 ? "credit" : "credits"}
              </Typography>
            ) : null}
            <ButtonBase
              aria-label={`Account menu${userName ? `: ${userName}` : ""}`}
              aria-haspopup="menu"
              onClick={(event) => setAnchorEl(event.currentTarget)}
              sx={{
                width: 40,
                height: 40,
                borderRadius: "50%",
                bgcolor: "text.primary",
                color: "#ffffff",
                fontWeight: 600,
                fontSize: 14,
              }}
            >
              {initials(userName) || "?"}
            </ButtonBase>
            <Menu
              anchorEl={anchorEl}
              open={Boolean(anchorEl)}
              onClose={() => setAnchorEl(null)}
              anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
              transformOrigin={{ vertical: "top", horizontal: "right" }}
            >
              <Box sx={{ px: 2, py: 1.25, minWidth: 220 }}>
                <Typography fontWeight={600}>{userName}</Typography>
                <Typography variant="body2" color="text.secondary">
                  {email}
                </Typography>
              </Box>
              <Divider />
              <MenuItem onClick={() => signOut({ callbackUrl: "/login" })}>
                <ListItemText>Sign out</ListItemText>
              </MenuItem>
            </Menu>
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
}
