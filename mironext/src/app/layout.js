import "./globals.css";
import AppProviders from "@/components/AppProviders";

export const metadata = {
  title: "Miro Next",
  description: "Miro platform rewrite in Next.js",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body>
        <AppProviders>{children}</AppProviders>
      </body>
    </html>
  );
}
