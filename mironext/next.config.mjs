/** @type {import('next').NextConfig} */
const nextConfig = {
  // Report generation runs headless Chromium; keep these out of the server bundle.
  serverExternalPackages: ["playwright", "@xmldom/xmldom"],
  allowedDevOrigins: [
    "http://127.0.0.1:3000",
    "http://localhost:3000",
    "http://127.0.0.1:3005",
    "http://localhost:3005",
    "http://127.0.0.1:3010",
    "http://localhost:3010",
    "http://127.0.0.1:3200",
    "http://localhost:3200",
  ],
};

export default nextConfig;
