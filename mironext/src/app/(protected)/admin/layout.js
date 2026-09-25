import { Stack, Typography } from "@mui/material";
import { requireSysAdmin } from "@/lib/admin/access";
import AdminNav from "@/components/admin/AdminNav";

export default async function AdminLayout({ children }) {
  await requireSysAdmin();
  return (
    <Stack spacing={3}>
      <Typography variant="h1">Administration</Typography>
      <AdminNav />
      {children}
    </Stack>
  );
}
