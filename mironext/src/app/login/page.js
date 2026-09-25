import { redirect } from "next/navigation";
import { Box } from "@mui/material";
import { auth } from "@/auth";
import LoginForm from "@/components/LoginForm";

export default async function LoginPage() {
  const session = await auth();

  if (session?.user) {
    // "/" sends each role to its own landing page.
    redirect("/");
  }

  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        px: 2,
        bgcolor: "background.default",
      }}
    >
      <LoginForm />
    </Box>
  );
}
