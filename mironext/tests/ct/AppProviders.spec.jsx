import { expect, test } from "@playwright/experimental-ct-react";

test("mounts a basic component", async ({ mount }) => {
  const component = await mount(<div data-testid="ct-child">Component test is running</div>);

  await expect(component).toContainText("Component test is running");
});
