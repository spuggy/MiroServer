// SVG replacement for MiroPieChartGenerator (JFreeChart).
//
// Geometry rules carried over from the Java:
// - the first slice (leading mode) is centred at the bottom (6 o'clock);
// - remaining slices follow clockwise;
// - slices that are not "attached" (engaged/excess) are exploded outwards.

export interface PieSlice {
  value: number;
  colour: string;
  exploded: boolean;
  label?: string;
}

export interface PieOptions {
  width?: number;
  height?: number;
  /** Fraction of the radius an exploded slice is pushed out by. */
  explodeFraction?: number;
  strokeColour?: string;
  /** Hole size as a fraction of the radius; 0 (default) draws a solid pie. */
  innerRadiusFraction?: number;
  /** Two lines of text drawn in the hole of a donut. */
  centreLabel?: { title: string; subtitle?: string };
}

const escapeXml = (s: string) =>
  s.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");

const round = (n: number) => Math.round(n * 100) / 100;
const pt = (p: { x: number; y: number }) => `${round(p.x)} ${round(p.y)}`;

/** Point on a circle for a JFreeChart-style angle (degrees, 0 = 3 o'clock, counter-clockwise). */
function polar(cx: number, cy: number, r: number, degrees: number) {
  const rad = (degrees * Math.PI) / 180;
  return { x: cx + r * Math.cos(rad), y: cy - r * Math.sin(rad) };
}

export function computePieAngles(values: number[]) {
  const total = values.reduce((a, b) => a + b, 0);
  if (total <= 0) return values.map(() => ({ start: 0, extent: 0, mid: 0 }));
  const extents = values.map((v) => (v / total) * 360);
  // MiroPieChartGenerator: startAngle = 270 + half the first slice, drawn clockwise.
  let angle = 270 + extents[0] / 2;
  return extents.map((extent) => {
    const start = angle;
    angle -= extent;
    return { start, extent, mid: start - extent / 2 };
  });
}

export function renderPieSvg(slices: PieSlice[], options: PieOptions = {}): string {
  const width = options.width ?? 400;
  const height = options.height ?? 400;
  const explodeFraction = options.explodeFraction ?? 0.1;
  const stroke = options.strokeColour ?? "#ffffff";
  const anyExploded = slices.some((s) => s.exploded);
  const cx = width / 2;
  const cy = height / 2;
  const maxR = Math.min(width, height) / 2 - 2;
  const r = anyExploded ? maxR / (1 + explodeFraction) : maxR;
  const inner = r * Math.min(Math.max(options.innerRadiusFraction ?? 0, 0), 0.9);

  const angles = computePieAngles(slices.map((s) => s.value));
  const paths = slices.map((slice, i) => {
    const { start, extent, mid } = angles[i];
    if (extent <= 0) return "";
    const offset = slice.exploded ? r * explodeFraction : 0;
    const centre = polar(cx, cy, offset, mid);
    const title = slice.label ? `<title>${escapeXml(slice.label)}</title>` : "";
    if (extent >= 359.999) {
      const c = `cx="${round(centre.x)}" cy="${round(centre.y)}"`;
      return inner > 0
        ? `<circle ${c} r="${round((r + inner) / 2)}" fill="none" stroke="${slice.colour}" stroke-width="${round(r - inner)}">${title}</circle>`
        : `<circle ${c} r="${round(r)}" fill="${slice.colour}">${title}</circle>`;
    }
    const from = polar(centre.x, centre.y, r, start);
    const to = polar(centre.x, centre.y, r, start - extent);
    const largeArc = extent > 180 ? 1 : 0;
    // Clockwise on screen = SVG sweep-flag 1.
    const d =
      inner > 0
        ? [
            `M ${round(from.x)} ${round(from.y)}`,
            `A ${round(r)} ${round(r)} 0 ${largeArc} 1 ${round(to.x)} ${round(to.y)}`,
            `L ${pt(polar(centre.x, centre.y, inner, start - extent))}`,
            `A ${round(inner)} ${round(inner)} 0 ${largeArc} 0 ${pt(polar(centre.x, centre.y, inner, start))}`,
            "Z",
          ].join(" ")
        : [
            `M ${round(centre.x)} ${round(centre.y)}`,
            `L ${round(from.x)} ${round(from.y)}`,
            `A ${round(r)} ${round(r)} 0 ${largeArc} 1 ${round(to.x)} ${round(to.y)}`,
            "Z",
          ].join(" ");
    return `<path d="${d}" fill="${slice.colour}" stroke="${stroke}" stroke-width="2" stroke-linejoin="round">${title}</path>`;
  });

  const label = options.centreLabel;
  if (label && inner > 0) {
    const titleSize = round(inner * 0.3);
    const subSize = round(inner * 0.19);
    const titleY = label.subtitle ? cy : cy + titleSize * 0.35;
    paths.push(
      `<text x="${cx}" y="${round(titleY)}" text-anchor="middle" font-size="${titleSize}" font-weight="700" class="pie-centre-title">${escapeXml(label.title)}</text>`,
    );
    if (label.subtitle) {
      paths.push(
        `<text x="${cx}" y="${round(cy + subSize * 1.6)}" text-anchor="middle" font-size="${subSize}" class="pie-centre-subtitle">${escapeXml(label.subtitle)}</text>`,
      );
    }
  }

  return (
    `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 ${width} ${height}" ` +
    `width="${width}" height="${height}" role="img" aria-label="MiRo results pie chart">` +
    paths.join("") +
    `</svg>`
  );
}
