"use client";

import Link from "next/link";
import Image from "next/image";
import { AppBar, Box, Button, Container, Toolbar } from "@mui/material";
import { signOut } from "next-auth/react";

const LINKS = [
  { href: "/projects", label: "My Projects" },
  { href: "/team-reports", label: "Team Reports" },
  { href: "/help", label: "Help" },
  { href: "/more", label: "More" },
];

export default function TopNav() {
  return (
    <AppBar
      position="sticky"
      color="transparent"
      elevation={0}
      sx={{ borderBottom: "1px solid #e9e9e9", backgroundColor: "#fff" }}
    >
      <Container maxWidth="lg">
        <Toolbar disableGutters sx={{ gap: 2, py: 1, minHeight: "64px !important" }}>
          <Box sx={{ display: "flex", alignItems: "center", mr: 2 }}>
            <Image src="/miro-logo-small.png" alt="Miro" width={114} height={49} priority />
          </Box>
          <Box sx={{ display: "flex", gap: 0.75, flex: 1, flexWrap: "wrap", alignItems: "center" }}>
            {LINKS.map((link) => (
              <Link
                key={link.href}
                href={link.href}
                style={{ whiteSpace: "normal", marginRight: "10px", fontSize: "20px" }}
              >
                {link.label}
              </Link>
            ))}
          </Box>
          <Button
            type="button"
            variant="outlined"
            onClick={() => signOut({ callbackUrl: "/login" })}
            sx={{
              borderColor: "#dddddd",
              color: "#292929",
              textTransform: "none",
              "&:hover": {
                borderColor: "#d0d0d0",
                backgroundColor: "#f3f3f3",
              },
            }}
          >
            Sign Out
          </Button>
        </Toolbar>
      </Container>
    </AppBar>
  );
}
