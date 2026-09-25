// Team map: each team member's initials plotted on the four-quadrant MiRo
// background (teammapbackground.png, 1200x900). Port of the legacy
// MiroTeamMapPlotter / MiroTeamMapChart and User.getInitials.
//
// A member's leading mode goes inside the circle in that mode's quadrant; when
// "engaged" is on, their secondary mode (only set if it is attached) is also
// plotted outside the circle, in that quadrant's corner. The seed positions
// below are for the Driving (top-left) quadrant and are mirrored for the rest.

export const TEAM_MAP_WIDTH = 1200;
export const TEAM_MAP_HEIGHT = 900;
export const TEAM_MAP_BACKGROUND = "/images/teammapbackground.png";

// Up to this many members get the larger dots and the sparser seed grid.
export const BIG_DOTS_COUNT = 17;

const CENTRE_X = TEAM_MAP_WIDTH / 2;
const CENTRE_Y = TEAM_MAP_HEIGHT / 2;

const BIG_DOTS = {
  radius: 30,
  fontSize: 20,
  leading: [
    [359, 215],
    [359, 291],
    [359, 366],
    [294, 330],
    [294, 403],
    [495, 139],
    [495, 366],
    [558, 178],
    [558, 403],
    [558, 254],
    [558, 330],
    [429, 403],
    [429, 178],
    [429, 254],
    [429, 330],
    [495, 215],
    [495, 291],
  ],
  secondary: [
    [60, 400],
    [60, 323],
    [60, 244],
    [60, 165],
    [60, 87],
    [381, 87],
    [147, 400],
    [304, 87],
    [227, 87],
    [147, 87],
    [147, 323],
    [147, 165],
    [227, 165],
    [227, 244],
    [147, 244],
  ],
};

const SMALL_DOTS = {
  radius: 20,
  fontSize: 15,
  leading: [
    [529, 112],
    [571, 112],
    [571, 154],
    [571, 196],
    [319, 238],
    [571, 238],
    [277, 280],
    [319, 280],
    [571, 280],
    [277, 322],
    [319, 322],
    [571, 322],
    [277, 364],
    [319, 364],
    [571, 364],
    [235, 406],
    [277, 406],
    [319, 406],
    [571, 406],
    [403, 154],
    [445, 154],
    [487, 154],
    [529, 154],
    [361, 196],
    [403, 196],
    [445, 196],
    [487, 196],
    [529, 196],
    [361, 406],
    [403, 406],
    [445, 406],
    [487, 406],
    [529, 406],
    [361, 364],
    [403, 364],
    [445, 364],
    [487, 364],
    [529, 364],
    [361, 322],
    [403, 322],
    [445, 322],
    [487, 322],
    [529, 322],
    [361, 280],
    [403, 280],
    [445, 280],
    [487, 280],
    [529, 280],
    [361, 238],
    [403, 238],
    [445, 238],
    [487, 238],
    [529, 238],
  ],
  secondary: [
    [25, 70],
    [277, 70],
    [319, 70],
    [361, 70],
    [403, 70],
    [445, 70],
    [487, 70],
    [529, 70],
    [571, 70],
    [25, 112],
    [319, 112],
    [361, 112],
    [403, 112],
    [25, 154],
    [277, 154],
    [319, 154],
    [25, 196],
    [235, 196],
    [277, 196],
    [25, 238],
    [235, 238],
    [25, 280],
    [235, 280],
    [25, 322],
    [193, 322],
    [25, 364],
    [193, 364],
    [25, 406],
    [67, 406],
    [109, 406],
    [151, 406],
    [193, 406],
    [67, 70],
    [109, 70],
    [151, 70],
    [193, 70],
    [235, 70],
    [67, 112],
    [109, 112],
    [151, 112],
    [193, 112],
    [235, 112],
    [277, 112],
    [67, 154],
    [109, 154],
    [151, 154],
    [193, 154],
    [235, 154],
    [67, 364],
    [109, 364],
    [151, 364],
    [67, 322],
    [109, 322],
    [151, 322],
    [67, 280],
    [109, 280],
    [151, 280],
    [193, 280],
    [67, 238],
    [109, 238],
    [151, 238],
    [193, 238],
    [67, 196],
    [109, 196],
    [151, 196],
    [193, 196],
  ],
};

const MIRRORS = {
  D: ([x, y]) => [x, y],
  E: ([x, y]) => [2 * CENTRE_X - x, y],
  A: ([x, y]) => [x, 2 * CENTRE_Y - y],
  O: ([x, y]) => [2 * CENTRE_X - x, 2 * CENTRE_Y - y],
};

function buildSlots(seeds) {
  const slots = {};
  for (const [mode, mirror] of Object.entries(MIRRORS)) {
    // The legacy code popped positions off a stack, i.e. last seed first.
    slots[mode] = seeds.map(mirror).reverse();
  }
  return slots;
}

/**
 * Initials from first/last name, de-duplicated against `used` (a Set) by
 * appending 1..9, as User.getInitials(HashMap) did.
 */
export function uniqueInitials(firstName, lastName, used) {
  const base =
    `${(firstName || "").trim().charAt(0)}${(lastName || "").trim().charAt(0)}`.toUpperCase();
  if (!used.has(base)) {
    used.add(base);
    return base;
  }
  for (let i = 1; i < 10; i += 1) {
    const candidate = `${base}${i}`;
    if (!used.has(candidate)) {
      used.add(candidate);
      return candidate;
    }
  }
  return base;
}

/**
 * Lays out team members on the map.
 *
 * @param {Array<{initials: string, leadingMode: string, secondaryMode?: string|null}>} members
 * @param {{engaged?: boolean}} options engaged also plots secondary modes
 * @returns {{radius: number, fontSize: number, dots: Array<{initials, x, y, mode, leading}>, omitted: number}}
 */
export function plotTeamMap(members, { engaged = true } = {}) {
  const size = members.length > BIG_DOTS_COUNT ? SMALL_DOTS : BIG_DOTS;
  const leadingSlots = buildSlots(size.leading);
  const secondarySlots = buildSlots(size.secondary);
  const dots = [];
  let omitted = 0;

  function place(slots, initials, mode, leading) {
    const stack = slots[mode];
    if (!stack) return;
    const position = stack.shift();
    if (!position) {
      omitted += 1;
      return;
    }
    dots.push({ initials, x: position[0], y: position[1], mode, leading });
  }

  for (const member of members) {
    place(leadingSlots, member.initials, member.leadingMode, true);
    if (engaged && member.secondaryMode) {
      place(secondarySlots, member.initials, member.secondaryMode, false);
    }
  }

  return { radius: size.radius, fontSize: size.fontSize, dots, omitted };
}
