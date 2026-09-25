// Domain constants ported from the legacy Java report code
// (MiroReport.init, MiroConstants, MiroResponse, MiroReport.getMBTIValue).

export const SURVEY_ID_MIRO_V10 = 4;
export const SURVEY_ID_MIRO_V11 = 5;

export type MiroLetter = "D" | "E" | "A" | "O";

/** Default `miroLetters` setting value used by the legacy app. */
export const DEFAULT_MIRO_LETTERS: MiroLetter[] = ["D", "E", "A", "O"];

/**
 * Order in which the legacy Java code iterated its `HashMap<String, Integer>`
 * of letter scores. Tie-breaking and result ordering depend on it, so we
 * reproduce it explicitly rather than relying on insertion order.
 */
export const JAVA_HASHMAP_LETTER_ORDER: MiroLetter[] = ["A", "D", "E", "O"];

export const DEFAULT_THRESHOLDS = {
  testOffset: 32,
  engagedScore: 31,
  latentScore: 8,
  excessScore: 55,
};

export const MODE_NAMES: Record<MiroLetter, string> = {
  E: "Energising Mode",
  D: "Driving Mode",
  A: "Analysing Mode",
  O: "Organising Mode",
};

export const MODE_SHORT_NAMES: Record<MiroLetter, string> = {
  D: "Driving",
  E: "Energising",
  A: "Analysing",
  O: "Organising",
};

export const MIRO_COLOURS = {
  MIROYELLOW: "#fbc726",
  MIROGREEN: "#42b449",
  MIROBLUE: "#43add5",
  MIRORED: "#d53f35",
  TITLE_BG_GREY: "#e0e0e0",
} as const;

export const MODE_COLOURS: Record<MiroLetter, string> = {
  E: MIRO_COLOURS.MIROYELLOW,
  D: MIRO_COLOURS.MIRORED,
  A: MIRO_COLOURS.MIROBLUE,
  O: MIRO_COLOURS.MIROGREEN,
};

export const POSITION_LABELS = ["Leading", "Supporting", "Supplementary", "Dormant"] as const;

export const STATE_TEXT = {
  engaged: "Engaged",
  disengaged: "Disengaged",
  latent: "Latent",
  excess: "Excess",
} as const;

export const LEGEND_IMAGES: Record<MiroLetter, string> = {
  A: "analysing_mode_leg.png",
  E: "energising_mode_leg.png",
  D: "driving_mode_leg.png",
  O: "organising_mode_leg.png",
};

export const HEADING_ICON_IMAGES: Record<string, string> = {
  o: "organising_mode_icon.png",
  e: "energising_mode_icon.png",
  a: "analyser_mode_icon.png",
  d: "driving_mode_icon.png",
};

export const FOUR_ICON_IMAGES = [
  "driving_mode_icon_cover_page.png",
  "energising_mode_icon_cover_page.png",
  "analyser_mode_icon_cover_page.png",
  "organising-four-logos_cover_page.png",
];

/** Result letters (in order) + EX/IN → correlating MBTI type. */
export const MBTI_MAP: Record<string, string> = {
  DEAOEX: "ENTP",
  DEAOIN: "INTJ",
  DEOAEX: "ENTP",
  DEOAIN: "INTJ",
  DOAEEX: "ENTJ",
  DOAEIN: "INTP",
  DOEAEX: "ENTP",
  DOEAIN: "INTJ",
  DAOEEX: "ENTJ",
  DAOEIN: "INTP",
  DAEOEX: "ENTJ",
  DAEOIN: "INTP",
  EDOAEX: "ENFP",
  EDOAIN: "INFJ",
  EDAOEX: "ENFP",
  EDAOIN: "INFJ",
  EODAEX: "ENFJ",
  EODAIN: "INFP",
  EOADEX: "ENFJ",
  EOADIN: "INFP",
  EADOEX: "ENFP",
  EADOIN: "INFJ",
  EAODEX: "ENFJ",
  EAODIN: "INFP",
  ODEAEX: "ESFJ",
  ODEAIN: "ISFP",
  ODAEEX: "ESFP",
  ODAEIN: "ISFJ",
  OEDAEX: "ESFJ",
  OEDAIN: "ISFP",
  OEADEX: "ESFJ",
  OEADIN: "ISFP",
  OADEEX: "ESFP",
  OADEIN: "ISFJ",
  OAEDEX: "ESFP",
  OAEDIN: "ISFJ",
  ADEOEX: "ESTJ",
  ADEOIN: "ISTP",
  ADOEEX: "ESTJ",
  ADOEIN: "ISTP",
  AEDOEX: "ESTJ",
  AEDOIN: "ISTP",
  AEODEX: "ESTP",
  AEODIN: "ISTJ",
  AODEEX: "ESTP",
  AODEIN: "ISTJ",
  AOEDEX: "ESTP",
  AOEDIN: "ISTJ",
};

export const FOOTER_TEXT = "© MiRo Psychometrics Ltd ";

// ---------------------------------------------------------------------------
// V11 / leadership (MiroReport.init, getJPValues, ExtroIntraMapping)

/** Jungian function stack per MBTI type, pivot first (subPieOrdering). */
export const JUNGIAN_FUNCTION_ORDER: Record<string, [string, string, string, string]> = {
  ENTJ: ["Te", "Ni", "Si", "Fe"],
  ENTP: ["Ne", "Ti", "Fi", "Se"],
  INTJ: ["Ni", "Te", "Fe", "Si"],
  INTP: ["Ti", "Ne", "Se", "Fi"],
  ENFJ: ["Fe", "Ni", "Si", "Te"],
  ENFP: ["Ne", "Fi", "Ti", "Se"],
  INFJ: ["Ni", "Fe", "Te", "Si"],
  INFP: ["Fi", "Ne", "Se", "Ti"],
  ESFJ: ["Fe", "Si", "Ni", "Te"],
  ESFP: ["Se", "Fi", "Ti", "Ne"],
  ISFJ: ["Si", "Fe", "Te", "Ni"],
  ISFP: ["Fi", "Se", "Ne", "Ti"],
  ESTJ: ["Te", "Si", "Ni", "Fe"],
  ESTP: ["Se", "Ti", "Fi", "Ne"],
  ISTJ: ["Si", "Te", "Fe", "Ni"],
  ISTP: ["Ti", "Se", "Ne", "Fi"],
};

export const MBTI_TYPES = Object.keys(JUNGIAN_FUNCTION_ORDER);

export const JUNGIAN_FUNCTION_NAMES: Record<string, string> = {
  Ni: "Introverted iNtuition",
  Ne: "Extroverted iNtuition",
  Si: "Introverted Sensing",
  Se: "Extroverted Sensing",
  Ti: "Introverted Thinking",
  Te: "Extroverted Thinking",
  Fi: "Introverted Feeling",
  Fe: "Extroverted Feeling",
};

export const JUNGIAN_POSITION_LABELS = [
  "Pivot Point (Dominant Function)",
  "Auxiliary Function",
  "Tertiary Function",
  "Inferior Function",
] as const;

/** Strength letter of the extro/intro strata (e.g. the "M" of "MIN"). */
export const STRATA_STRENGTH: Record<string, string> = {
  L: "Slightly",
  M: "Moderately",
  H: "Strongly",
};

/**
 * ExtroIntraMapping: the first three result letters pick the pivot function
 * (iNtuition, Thinking, Feeling, Sensing); the V11 section id is that letter
 * plus the extro/intro strata, e.g. "DEA" + "MIN" → "NMIN".
 */
export const PIVOT_FUNCTION_BY_LETTERS: Record<string, "N" | "T" | "F" | "S"> = {
  DEA: "N",
  DEO: "N",
  DOA: "T",
  DOE: "N",
  DAO: "T",
  DAE: "T",
  EDO: "N",
  EDA: "N",
  EOD: "F",
  EOA: "F",
  EAD: "N",
  EAO: "F",
  ODE: "F",
  ODA: "S",
  OED: "F",
  OEA: "F",
  OAD: "S",
  OAE: "S",
  ADE: "T",
  ADO: "T",
  AED: "T",
  AEO: "S",
  AOD: "S",
  AOE: "S",
};

/**
 * getJPValues: 1-based result positions averaged for the Judging (left, first
 * pair) and Perceiving (right, second pair) project-management bar.
 */
export const JP_POSITIONS: Record<string, [number, number, number, number]> = {
  DEAOEX: [3, 4, 1, 2],
  DEAOIN: [1, 3, 2, 4],
  DEOAEX: [3, 4, 1, 2],
  DEOAIN: [1, 3, 2, 4],
  DOAEEX: [1, 3, 2, 4],
  DOAEIN: [2, 4, 1, 3],
  DOEAEX: [2, 4, 1, 3],
  DOEAIN: [1, 3, 2, 4],
  DAOEEX: [1, 2, 3, 4],
  DAOEIN: [2, 4, 1, 3],
  DAEOEX: [1, 2, 3, 4],
  DAEOIN: [2, 4, 1, 3],
  EDOAEX: [3, 4, 1, 2],
  EDOAIN: [1, 3, 2, 4],
  EDAOEX: [3, 4, 1, 2],
  EDAOIN: [1, 3, 2, 4],
  EODAEX: [1, 2, 3, 4],
  EODAIN: [2, 4, 1, 3],
  EOADEX: [1, 2, 3, 4],
  EOADIN: [2, 4, 1, 3],
  EADOEX: [2, 4, 1, 3],
  EADOIN: [1, 3, 2, 4],
  EAODEX: [1, 3, 2, 4],
  EAODIN: [2, 4, 1, 3],
  ODEAEX: [1, 3, 2, 4],
  ODEAIN: [2, 4, 1, 3],
  ODAEEX: [2, 4, 1, 3],
  ODAEIN: [1, 3, 2, 4],
  OEDAEX: [1, 2, 3, 4],
  OEDAIN: [2, 4, 1, 3],
  OEADEX: [1, 3, 2, 4],
  OEADIN: [2, 4, 1, 3],
  OADEEX: [3, 4, 1, 2],
  OADEIN: [1, 3, 2, 4],
  OAEDEX: [3, 4, 1, 2],
  OAEDIN: [1, 3, 2, 4],
  ADEOEX: [1, 2, 3, 4],
  ADEOIN: [2, 4, 1, 3],
  ADOEEX: [1, 2, 3, 4],
  ADOEIN: [2, 4, 1, 3],
  AEDOEX: [1, 3, 2, 4],
  AEDOIN: [2, 4, 1, 3],
  AEODEX: [2, 4, 1, 3],
  AEODIN: [1, 3, 2, 4],
  AODEEX: [3, 4, 1, 2],
  AODEIN: [1, 3, 2, 4],
  AOEDEX: [3, 4, 1, 2],
  AOEDIN: [1, 2, 3, 4],
};
