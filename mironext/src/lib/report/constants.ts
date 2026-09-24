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
