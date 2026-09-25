import { Figtree } from "next/font/google";
import "./globals.css";
import AppProviders from "@/components/AppProviders";

const figtree = Figtree({ subsets: ["latin"], variable: "--font-figtree", display: "swap" });

export const metadata = {
  title: "MiRo",
  description: "MiRo behavioural assessments",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en" className={figtree.variable}>
      <body>
        <AppProviders>{children}</AppProviders>
      </body>
    </html>
  );
}
