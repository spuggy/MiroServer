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
}

const round = (n: number) => Math.round(n * 100) / 100;

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

  const angles = computePieAngles(slices.map((s) => s.value));
  const paths = slices.map((slice, i) => {
    const { start, extent, mid } = angles[i];
    if (extent <= 0) return "";
    const offset = slice.exploded ? r * explodeFraction : 0;
    const centre = polar(cx, cy, offset, mid);
    const title = slice.label ? `<title>${slice.label}</title>` : "";
    if (extent >= 359.999) {
      return `<circle cx="${round(centre.x)}" cy="${round(centre.y)}" r="${round(r)}" fill="${slice.colour}">${title}</circle>`;
    }
    const from = polar(centre.x, centre.y, r, start);
    const to = polar(centre.x, centre.y, r, start - extent);
    const largeArc = extent > 180 ? 1 : 0;
    // Clockwise on screen = SVG sweep-flag 1.
    const d = [
      `M ${round(centre.x)} ${round(centre.y)}`,
      `L ${round(from.x)} ${round(from.y)}`,
      `A ${round(r)} ${round(r)} 0 ${largeArc} 1 ${round(to.x)} ${round(to.y)}`,
      "Z",
    ].join(" ");
    return `<path d="${d}" fill="${slice.colour}" stroke="${stroke}" stroke-width="2" stroke-linejoin="round">${title}</path>`;
  });

  return (
    `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 ${width} ${height}" ` +
    `width="${width}" height="${height}" role="img" aria-label="MiRo results pie chart">` +
    paths.join("") +
    `</svg>`
  );
}
