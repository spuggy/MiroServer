// Fixture data copied from test/service/uk/co/bluetrail/miro/MiroReport10Test.java

import type { SurveyDefinition, SurveyResponseInput } from "../scoring";
import type { Practitioner } from "../miro-report";

export const v10Questions: SurveyDefinition["questions"] = [
  { id: 1, shortname: "11", qMeta: "Q", jQuestionId: 2 },
  { id: 2, shortname: "10", qMeta: "Q", jQuestionId: 3 },
  { id: 3, shortname: "11", qMeta: "Q", jQuestionId: 4 },
  { id: 5, shortname: "2", qMeta: "Q", jQuestionId: 6 },
  { id: 6, shortname: "11", qMeta: "Q", jQuestionId: 7 },
  { id: 7, shortname: "6", qMeta: "Q", jQuestionId: 8 },
  { id: 10, shortname: "11", qMeta: "Q", jQuestionId: 11 },
  { id: 11, shortname: "9", qMeta: "Q", jQuestionId: 12 },
  { id: 13, shortname: "11", qMeta: "Q", jQuestionId: 14 },
  { id: 14, shortname: "11", qMeta: "Q", jQuestionId: 15 },
  { id: 15, shortname: "11", qMeta: "Q", jQuestionId: 16 },
  { id: 16, shortname: "11", qMeta: "Q", jQuestionId: 17 },
  { id: 17, shortname: "11", qMeta: "Q", jQuestionId: 18 },
  { id: 18, shortname: "5", qMeta: "Q", jQuestionId: 19 },
  { id: 19, shortname: "1", qMeta: "Q", jQuestionId: 20 },
  { id: 20, shortname: "11", qMeta: "Q", jQuestionId: 21 },
  { id: 21, shortname: "11", qMeta: "Q", jQuestionId: 22 },
  { id: 22, shortname: "11", qMeta: "Q", jQuestionId: 23 },
  { id: 23, shortname: "11", qMeta: "Q", jQuestionId: 24 },
  { id: 24, shortname: "7", qMeta: "Q", jQuestionId: 25 },
  { id: 26, shortname: "11", qMeta: "Q", jQuestionId: 27 },
  { id: 27, shortname: "11", qMeta: "Q", jQuestionId: 28 },
  { id: 28, shortname: "4", qMeta: "Q", jQuestionId: 29 },
  { id: 29, shortname: "11", qMeta: "Q", jQuestionId: 30 },
  { id: 31, shortname: "11", qMeta: "Q", jQuestionId: 0 },
  { id: 8, shortname: "D-O", qMeta: "tie", jQuestionId: 9 },
  { id: 4, shortname: "E-A", qMeta: "tie", jQuestionId: 5 },
  { id: 9, shortname: "E-D", qMeta: "tie", jQuestionId: 10 },
  { id: 12, shortname: "O-A", qMeta: "tie", jQuestionId: 13 },
  { id: 25, shortname: "E-O", qMeta: "tie", jQuestionId: 26 },
  { id: 30, shortname: "D-A", qMeta: "tie", jQuestionId: 0 },
  { id: 32, shortname: "123", qMeta: "23", jQuestionId: 0 },
  { id: 62, shortname: "D-A", qMeta: "tie", jQuestionId: 0 },
  { id: 33, shortname: "11", qMeta: "Q", jQuestionId: 34 },
  { id: 34, shortname: "6", qMeta: "Q", jQuestionId: 35 },
  { id: 35, shortname: "D-O", qMeta: "tie", jQuestionId: 36 },
  { id: 36, shortname: "E-A", qMeta: "tie", jQuestionId: 37 },
  { id: 37, shortname: "11", qMeta: "Q", jQuestionId: 38 },
  { id: 38, shortname: "2", qMeta: "Q", jQuestionId: 39 },
  { id: 39, shortname: "11", qMeta: "Q", jQuestionId: 40 },
  { id: 40, shortname: "10", qMeta: "Q", jQuestionId: 41 },
  { id: 41, shortname: "E-D", qMeta: "tie", jQuestionId: 42 },
  { id: 42, shortname: "11", qMeta: "Q", jQuestionId: 43 },
  { id: 43, shortname: "9", qMeta: "Q", jQuestionId: 44 },
  { id: 44, shortname: "O-A", qMeta: "tie", jQuestionId: 45 },
  { id: 45, shortname: "11", qMeta: "Q", jQuestionId: 46 },
  { id: 46, shortname: "11", qMeta: "Q", jQuestionId: 47 },
  { id: 47, shortname: "11", qMeta: "Q", jQuestionId: 48 },
  { id: 48, shortname: "11", qMeta: "Q", jQuestionId: 49 },
  { id: 49, shortname: "11", qMeta: "Q", jQuestionId: 50 },
  { id: 50, shortname: "5", qMeta: "Q", jQuestionId: 51 },
  { id: 51, shortname: "1", qMeta: "Q", jQuestionId: 52 },
  { id: 52, shortname: "11", qMeta: "Q", jQuestionId: 53 },
  { id: 53, shortname: "11", qMeta: "Q", jQuestionId: 54 },
  { id: 54, shortname: "11", qMeta: "Q", jQuestionId: 55 },
  { id: 55, shortname: "11", qMeta: "Q", jQuestionId: 56 },
  { id: 56, shortname: "7", qMeta: "Q", jQuestionId: 57 },
  { id: 57, shortname: "E-O", qMeta: "tie", jQuestionId: 58 },
  { id: 58, shortname: "11", qMeta: "Q", jQuestionId: 59 },
  { id: 59, shortname: "11", qMeta: "Q", jQuestionId: 60 },
  { id: 60, shortname: "4", qMeta: "Q", jQuestionId: 61 },
  { id: 61, shortname: "11", qMeta: "Q", jQuestionId: 62 },
];

export const v10Survey: SurveyDefinition = { id: 4, firstQuestionId: 33, questions: v10Questions };

export const rogerTestResponse: SurveyResponseInput = {
  surveyId: 4,
  answerTrail:
    "Charming#E;Tolerant#O~" +
    "Self-assured#D;Impulsive#E~" +
    "Empathic#O;Competitive#D~" +
    "Charismatic#E;Methodical#A ~" +
    "Positive#E;Pioneering#D~" +
    "Amiable#O;Sceptical#D~" +
    "Good-natured#O;Unwavering#D~" +
    "Affable#E;Adventurous#D~" +
    "Playful#E;Demanding#D~" +
    "Admirable#E;Forceful#D~" +
    "Companionable#E;Self-sufficient#D~" +
    "Kind-hearted#O;Orderly#A~" +
    "Unconventional#E;Conventional#A~" +
    "Gregarious#E;Level-headed#A~" +
    "Open #E;No-nonsense #D~" +
    "Friendly#E;Forthright#D~" +
    "Big-hearted#O;Well-disciplined#A~" +
    "Relaxed#O;Exacting#A~" +
    "Gentle#O;Modest#A~" +
    "Sophisticated#A;Compassionate#O~" +
    "Popular#E;Eager#D~" +
    "Optimistic#E;Risk taking#D~" +
    "Open-minded#A;Self-confident#D~" +
    "Respectful  #O;Definite #D~" +
    "Unpredictable #E;Stable#O~" +
    "Self-reliant#D;Restrained#A~" +
    "Attentive#O;Diplomatic#A~" +
    "Helpful#O;Determined#D~" +
    "Contented#O;Restless#D~" +
    "Perfectionist#A ;Impatient#D ",
  questionTrail:
    "33~34~35~36~37~38~39~40~41~42~43~44~45~46~47~48~49~50~51~52~53~54~55~56~57~58~59~60~61~62",
};

export const kennyPractitioner: Practitioner = {
  name: "Kenny Practitioner",
  email: "rspence@pac.com",
  telNo: "07961236235",
  address: ["80 Sandringham Road", null, "Swindon", null, null],
};

// Fixture data copied from test/service/uk/co/bluetrail/miro/MiroReport11Test.java.
// testGenerateFreeReport runs this V11 response through the V10 report; that is
// the run which produced the reference web/miro/out/Roger_Test_1.pdf.

export const v11Questions: SurveyDefinition["questions"] = [
  { id: 1, shortname: "11", qMeta: "Q", jQuestionId: 2 },
  { id: 2, shortname: "10", qMeta: "Q", jQuestionId: 3 },
  { id: 3, shortname: "11", qMeta: "Q", jQuestionId: 4 },
  { id: 5, shortname: "2", qMeta: "Q", jQuestionId: 6 },
  { id: 6, shortname: "11", qMeta: "Q", jQuestionId: 7 },
  { id: 7, shortname: "6", qMeta: "Q", jQuestionId: 8 },
  { id: 10, shortname: "11", qMeta: "Q", jQuestionId: 11 },
  { id: 11, shortname: "9", qMeta: "Q", jQuestionId: 12 },
  { id: 13, shortname: "11", qMeta: "Q", jQuestionId: 14 },
  { id: 14, shortname: "11", qMeta: "Q", jQuestionId: 15 },
  { id: 15, shortname: "11", qMeta: "Q", jQuestionId: 16 },
  { id: 16, shortname: "11", qMeta: "Q", jQuestionId: 17 },
  { id: 17, shortname: "11", qMeta: "Q", jQuestionId: 18 },
  { id: 18, shortname: "5", qMeta: "Q", jQuestionId: 19 },
  { id: 19, shortname: "1", qMeta: "Q", jQuestionId: 20 },
  { id: 20, shortname: "11", qMeta: "Q", jQuestionId: 21 },
  { id: 21, shortname: "11", qMeta: "Q", jQuestionId: 22 },
  { id: 22, shortname: "11", qMeta: "Q", jQuestionId: 23 },
  { id: 23, shortname: "11", qMeta: "Q", jQuestionId: 24 },
  { id: 24, shortname: "7", qMeta: "Q", jQuestionId: 25 },
  { id: 26, shortname: "11", qMeta: "Q", jQuestionId: 27 },
  { id: 27, shortname: "11", qMeta: "Q", jQuestionId: 28 },
  { id: 28, shortname: "4", qMeta: "Q", jQuestionId: 29 },
  { id: 29, shortname: "11", qMeta: "Q", jQuestionId: 30 },
  { id: 31, shortname: "11", qMeta: "Q", jQuestionId: 0 },
  { id: 8, shortname: "D-O", qMeta: "tie", jQuestionId: 9 },
  { id: 4, shortname: "E-A", qMeta: "tie", jQuestionId: 5 },
  { id: 9, shortname: "E-D", qMeta: "tie", jQuestionId: 10 },
  { id: 12, shortname: "O-A", qMeta: "tie", jQuestionId: 13 },
  { id: 25, shortname: "E-O", qMeta: "tie", jQuestionId: 26 },
  { id: 30, shortname: "D-A", qMeta: "tie", jQuestionId: 0 },
  { id: 32, shortname: "123", qMeta: "23", jQuestionId: 0 },
  { id: 33, shortname: "11", qMeta: "Q", jQuestionId: 34 },
  { id: 34, shortname: "6", qMeta: "Q", jQuestionId: 35 },
  { id: 35, shortname: "D-O", qMeta: "tie", jQuestionId: 36 },
  { id: 36, shortname: "E-A", qMeta: "tie", jQuestionId: 37 },
  { id: 37, shortname: "11", qMeta: "Q", jQuestionId: 38 },
  { id: 38, shortname: "2", qMeta: "Q", jQuestionId: 39 },
  { id: 39, shortname: "11", qMeta: "Q", jQuestionId: 40 },
  { id: 40, shortname: "10", qMeta: "Q", jQuestionId: 41 },
  { id: 41, shortname: "E-D", qMeta: "tie", jQuestionId: 42 },
  { id: 42, shortname: "11", qMeta: "Q", jQuestionId: 43 },
  { id: 43, shortname: "9", qMeta: "Q", jQuestionId: 44 },
  { id: 44, shortname: "O-A", qMeta: "tie", jQuestionId: 45 },
  { id: 45, shortname: "11", qMeta: "Q", jQuestionId: 46 },
  { id: 46, shortname: "11", qMeta: "Q", jQuestionId: 47 },
  { id: 47, shortname: "11", qMeta: "Q", jQuestionId: 48 },
  { id: 48, shortname: "11", qMeta: "Q", jQuestionId: 49 },
  { id: 49, shortname: "11", qMeta: "Q", jQuestionId: 50 },
  { id: 50, shortname: "5", qMeta: "Q", jQuestionId: 51 },
  { id: 51, shortname: "1", qMeta: "Q", jQuestionId: 52 },
  { id: 52, shortname: "11", qMeta: "Q", jQuestionId: 53 },
  { id: 53, shortname: "11", qMeta: "Q", jQuestionId: 54 },
  { id: 54, shortname: "11", qMeta: "Q", jQuestionId: 55 },
  { id: 55, shortname: "11", qMeta: "Q", jQuestionId: 56 },
  { id: 56, shortname: "7", qMeta: "Q", jQuestionId: 57 },
  { id: 57, shortname: "E-O", qMeta: "tie", jQuestionId: 58 },
  { id: 58, shortname: "11", qMeta: "Q", jQuestionId: 59 },
  { id: 59, shortname: "11", qMeta: "Q", jQuestionId: 60 },
  { id: 60, shortname: "4", qMeta: "Q", jQuestionId: 61 },
  { id: 61, shortname: "11", qMeta: "Q", jQuestionId: 62 },
  { id: 62, shortname: "D-A", qMeta: "tie", jQuestionId: 63 },
  { id: 63, shortname: "", qMeta: "Q11Inst", jQuestionId: 64 },
  { id: 64, shortname: "", qMeta: "Q11", jQuestionId: 65 },
  { id: 65, shortname: "", qMeta: "Q11", jQuestionId: 66 },
  { id: 66, shortname: "", qMeta: "Q11", jQuestionId: 67 },
  { id: 67, shortname: "", qMeta: "Q11", jQuestionId: 68 },
  { id: 68, shortname: "", qMeta: "Q11", jQuestionId: 69 },
  { id: 69, shortname: "", qMeta: "Q11", jQuestionId: 70 },
  { id: 70, shortname: "", qMeta: "Q11", jQuestionId: 71 },
  { id: 71, shortname: "", qMeta: "Q11", jQuestionId: 72 },
  { id: 72, shortname: "", qMeta: "Q11", jQuestionId: 73 },
  { id: 73, shortname: "", qMeta: "Q11", jQuestionId: 74 },
  { id: 74, shortname: "", qMeta: "Q11", jQuestionId: 75 },
  { id: 75, shortname: "", qMeta: "Q11", jQuestionId: 76 },
  { id: 76, shortname: "", qMeta: "Q11", jQuestionId: 77 },
  { id: 77, shortname: "", qMeta: "Q11", jQuestionId: 78 },
  { id: 78, shortname: "", qMeta: "Q11", jQuestionId: 79 },
  { id: 79, shortname: "", qMeta: "Q11", jQuestionId: 80 },
  { id: 80, shortname: "", qMeta: "Q11", jQuestionId: 81 },
  { id: 81, shortname: "", qMeta: "Q11", jQuestionId: 82 },
  { id: 82, shortname: "", qMeta: "Q11", jQuestionId: 0 },
];

export const v11Survey: SurveyDefinition = { id: 5, firstQuestionId: 33, questions: v11Questions };

export const rogerTestV11Response: SurveyResponseInput = {
  surveyId: 5,
  answerTrail:
    "Tolerant#O;Thorough#A~" +
    "Understanding#O;Agreeable#A~" +
    "Empathic#O;Self-starter#D ~" +
    "Charismatic#E;Exacting#A~" +
    "Positive#E;Pioneering#D~" +
    "Loyal#A;Sceptical#D~" +
    "Persuasive#E;Caref ul#A~" +
    "Adventurous#D;Temperate#O~" +
    "Playful#E;Demanding#D~" +
    "Admirable#E;Precise#A~" +
    "Companionable#E;Patient#O~" +
    "Empathic#O;Meticulous#A~" +
    "Unconventional#E;Conventional#A~" +
    "Level-headed#A;Gregarious#E~" +
    "Open #E;No-nonsense#D~" +
    "Friendly#E;Accurate#A~" +
    "Stubborn#D;Well-disciplined#A~" +
    "Relaxed#O;Exacting#A~" +
    "Modest#A;Influencing#E~" +
    "Sophisticated#A;Good-mixer#E~" +
    "Faithful#A;Popular#E~" +
    "Optimistic#E;Analytical#A~" +
    "Open-minded#A;Self-confident#D~" +
    "Respectful  #O;Particular#A~" +
    "Stable#O;Unpredictable #E~" +
    "Self-reliant#D;Alert#O~" +
    "Realistic#D;Sociable#E~" +
    "Tolerant#A;Determined#D~" +
    "Peaceable#A;Convincing#E~" +
    "Detached#A ;Perfectionist#A ~" +
    "null;N/A~" +
    "null;false#min us~" +
    "null;false#minus~" +
    "null;true#minus~" +
    "null;false#plus~" +
    "null;false#minus~" +
    "null;true#minus~" +
    "null;false#plus~" +
    "null;true#minus~" +
    "null;false#plus~" +
    "null;false#minus~" +
    "null;true#minus~" +
    "null;true#plus~" +
    "null;true#minus~" +
    "null;fa lse#minus~" +
    "null;true#minus~" +
    "null;true#minus~" +
    "null;true#minus~" +
    "null;false#minus~" +
    "null;true#minus",
  questionTrail:
    "33~34~35~36~37~38~39~40~41~42~43~44~45~46~47~48~49~50~51~52~53~54~55~56~57~58~59~60~61~62~63~64~65~66~67~68~69~70~71~72~73~74~75~76~77~78~79~80~81~82~83",
};
