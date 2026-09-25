"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { Box } from "@mui/material";

const TABS = [
  { href: "/admin", label: "Overview", exact: true },
  { href: "/admin/customers", label: "Customers" },
  { href: "/admin/transactions", label: "Transactions" },
];

export default function AdminNav() {
  const pathname = usePathname();
  return (
    <Box
      component="nav"
      aria-label="Administration"
      sx={{ display: "flex", gap: 3, borderBottom: "1px solid", borderColor: "divider" }}
    >
      {TABS.map((tab) => {
        const active = tab.exact ? pathname === tab.href : pathname.startsWith(tab.href);
        return (
          <Box
            key={tab.href}
            component={Link}
            href={tab.href}
            aria-current={active ? "page" : undefined}
            sx={{
              py: 1.25,
              mb: "-1px",
              fontSize: 15,
              fontWeight: active ? 600 : 500,
              color: active ? "primary.dark" : "text.secondary",
              borderBottom: "2px solid",
              borderColor: active ? "primary.main" : "transparent",
              "&:hover": { color: "text.primary" },
            }}
          >
            {tab.label}
          </Box>
        );
      })}
    </Box>
  );
}
