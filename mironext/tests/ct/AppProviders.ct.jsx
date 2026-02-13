import { expect, test } from "@playwright/experimental-ct-react";
import AppProviders from "@/components/AppProviders";

test("renders wrapped children", async ({ mount }) => {
  const component = await mount(
    <AppProviders>
      <div data-testid="ct-child">Component test is running</div>
    </AppProviders>,
  );

  await expect(component.getByTestId("ct-child")).toHaveText("Component test is running");
});
