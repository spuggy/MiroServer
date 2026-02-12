import { redirect } from "next/navigation";
import { Box, Container } from "@mui/material";
import { auth } from "@/auth";
import TopNav from "@/components/TopNav";

export default async function ProtectedLayout({ children }) {
  const session = await auth();

  if (!session?.user?.id) {
    redirect("/login");
  }

  return (
    <Box>
      <TopNav />
      <Container maxWidth="lg" sx={{ py: 3, px: { xs: 1.25, sm: 5 } }}>
        {children}
      </Container>
    </Box>
  );
}
