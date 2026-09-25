import { redirect } from "next/navigation";
import { Box, Container } from "@mui/material";
import { auth } from "@/auth";
import { prisma } from "@/lib/prisma";
import { isSysAdmin } from "@/lib/admin/access";
import TopNav from "@/components/TopNav";

export default async function ProtectedLayout({ children }) {
  const session = await auth();

  if (!session?.user?.id) {
    redirect("/login");
  }

  const userId = BigInt(session.user.id);
  const [user, sysAdmin] = await Promise.all([
    prisma.appUser.findUnique({
      where: { id: userId },
      select: { firstName: true, lastName: true, email: true, creditBalance: true },
    }),
    isSysAdmin(userId),
  ]);

  return (
    <Box sx={{ minHeight: "100vh", bgcolor: "background.default" }}>
      <TopNav
        userName={user ? `${user.firstName} ${user.lastName}` : session.user.name}
        email={user?.email ?? session.user.email}
        creditBalance={user?.creditBalance ?? null}
        isSysAdmin={sysAdmin}
      />
      <Container maxWidth="lg" sx={{ py: 4, px: { xs: 2, sm: 5 } }}>
        {children}
      </Container>
    </Box>
  );
}
