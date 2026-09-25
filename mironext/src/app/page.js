import { redirect } from "next/navigation";
import { auth } from "@/auth";
import { isSysAdmin } from "@/lib/admin/access";

export default async function HomePage() {
  const session = await auth();

  if (!session?.user) {
    redirect("/login");
  }

  // Super users land on the administration reports; everyone else on their projects.
  redirect((await isSysAdmin(BigInt(session.user.id))) ? "/admin" : "/projects");
}
