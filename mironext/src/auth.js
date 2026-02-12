import { getServerSession } from "next-auth";
import Credentials from "next-auth/providers/credentials";
import { prisma } from "@/lib/prisma";
import { verifyLegacyPassword } from "@/lib/password";
import { isEnabledFlag } from "@/lib/auth-utils";

export const authOptions = {
  pages: {
    signIn: "/login",
  },
  session: {
    strategy: "jwt",
  },
  providers: [
    Credentials({
      name: "Credentials",
      credentials: {
        username: { label: "Username", type: "text" },
        password: { label: "Password", type: "password" },
      },
      async authorize(credentials) {
        const username = credentials?.username?.trim().toLowerCase();
        const password = credentials?.password;

        if (!username || !password) {
          return null;
        }

        const user = await prisma.appUser.findUnique({
          where: {
            username,
          },
          select: {
            id: true,
            accountId: true,
            username: true,
            password: true,
            firstName: true,
            lastName: true,
            email: true,
            enabled: true,
            deleted: true,
          },
        });

        if (!user || user.deleted || !isEnabledFlag(user.enabled)) {
          return null;
        }

        if (!verifyLegacyPassword(password, user.password)) {
          return null;
        }

        return {
          id: user.id.toString(),
          accountId: user.accountId.toString(),
          name: `${user.firstName} ${user.lastName}`,
          email: user.email,
        };
      },
    }),
  ],
  callbacks: {
    async jwt({ token, user }) {
      if (user) {
        token.id = user.id;
        token.accountId = user.accountId;
      }
      return token;
    },
    async session({ session, token }) {
      if (session.user) {
        session.user.id = token.id;
        session.user.accountId = token.accountId;
      }
      return session;
    },
  },
};

export function auth() {
  return getServerSession(authOptions);
}
