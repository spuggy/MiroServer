import { expect, test } from "@playwright/test";

test("redirects unauthenticated users to login", async ({ page }) => {
  await page.goto("/projects");
  await expect(page).toHaveURL(/\/login$/);
  await expect(page.getByRole("heading", { name: "Sign In" })).toBeVisible();
});

test("logs in with seeded credentials and lists projects", async ({ page }) => {
  await page.goto("/login");

  await page.getByLabel("Username").fill(process.env.PLAYWRIGHT_TEST_USER || "pw_admin");
  await page.getByLabel("Password").fill(process.env.PLAYWRIGHT_TEST_PASSWORD || "pw_password_123");
  await page.getByRole("button", { name: "Sign In" }).click();

  await expect(page).toHaveURL(/\/projects(\?.*)?$/);
  await expect(page.getByRole("heading", { name: "My Projects" })).toBeVisible();
  await expect(page.getByRole("link", { name: "Seeded Project" })).toBeVisible();
});
