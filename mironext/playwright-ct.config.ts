import path from "node:path";
import { defineConfig } from "@playwright/experimental-ct-react";

export default defineConfig({
  testDir: "./tests/ct",
  timeout: 30_000,
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  workers: process.env.CI ? 1 : undefined,
  reporter: [
    ["list"],
    ["junit", { outputFile: "test-results/ct/junit.xml" }],
    ["json", { outputFile: "test-results/ct/results.json" }],
    ["html", { outputFolder: "playwright-report/ct", open: "never" }],
  ],
  use: {
    trace: "on-first-retry",
    ctPort: 3101,
    ctViteConfig: {
      resolve: {
        alias: {
          "@": path.resolve(__dirname, "./src"),
          "@mui/material-nextjs/v15-appRouter": path.resolve(
            __dirname,
            "./tests/ct/mocks/muiAppRouterCacheProvider.jsx",
          ),
        },
      },
    },
  },
});
