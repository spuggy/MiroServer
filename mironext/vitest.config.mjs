import path from "node:path";
import { defineConfig } from "vitest/config";

export default defineConfig({
  test: {
    environment: "node",
    env: {
      NEXTAUTH_SECRET: "vitest-secret",
    },
  },
  resolve: {
    alias: {
      "@": path.resolve("./src"),
    },
  },
});
