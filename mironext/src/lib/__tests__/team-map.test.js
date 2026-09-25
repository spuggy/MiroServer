import { describe, expect, it } from "vitest";
import { BIG_DOTS_COUNT, plotTeamMap, uniqueInitials } from "@/lib/team-map";

describe("uniqueInitials", () => {
  it("de-duplicates clashing initials", () => {
    const used = new Set();
    expect(uniqueInitials("richard", "spence", used)).toBe("RS");
    expect(uniqueInitials("Rob", "Smith", used)).toBe("RS1");
    expect(uniqueInitials("Rae", "Sims", used)).toBe("RS2");
    expect(uniqueInitials("Ann", null, used)).toBe("A");
  });
});

describe("plotTeamMap", () => {
  it("mirrors the driving seed into each quadrant", () => {
    const { dots, radius } = plotTeamMap([
      { initials: "DD", leadingMode: "D" },
      { initials: "EE", leadingMode: "E" },
      { initials: "AA", leadingMode: "A" },
      { initials: "OO", leadingMode: "O" },
    ]);
    expect(radius).toBe(30);
    // The last seed is used first, as the legacy stack popped it.
    expect(dots.map(({ initials, x, y }) => [initials, x, y])).toEqual([
      ["DD", 495, 291],
      ["EE", 705, 291],
      ["AA", 495, 609],
      ["OO", 705, 609],
    ]);
  });

  it("plots attached secondary modes only when engaged", () => {
    const members = [
      { initials: "AB", leadingMode: "D", secondaryMode: "O" },
      { initials: "CD", leadingMode: "E", secondaryMode: null },
    ];
    expect(plotTeamMap(members, { engaged: false }).dots).toHaveLength(2);

    const { dots } = plotTeamMap(members);
    expect(dots).toHaveLength(3);
    expect(dots[1]).toMatchObject({ initials: "AB", mode: "O", leading: false, x: 1053, y: 656 });
  });

  it("switches to small dots for large teams and counts overflow", () => {
    const members = Array.from({ length: BIG_DOTS_COUNT + 60 }, (_, i) => ({
      initials: `P${i}`,
      leadingMode: "D",
    }));
    const { radius, dots, omitted } = plotTeamMap(members);
    expect(radius).toBe(20);
    expect(dots.length + omitted).toBe(members.length);
    expect(omitted).toBeGreaterThan(0);
    expect(new Set(dots.map((d) => `${d.x},${d.y}`)).size).toBe(dots.length);
  });
});
