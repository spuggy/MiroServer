import { z } from "zod";

// Mirrors the legacy MiroProject form. bcc_practitioner is a Hibernate yes_no column ("Y"/"N").
export const PROJECT_SCHEMA = z.object({
  projectTitle: z.string().trim().min(1).max(100),
  projectDescription: z.string().trim().min(1).max(254),
  costcode: z.string().trim().min(1).max(50),
  emailInviteSubject: z.string().trim().min(1).max(50),
  emailInviteText: z.string().trim().min(1).max(100),
  bccPractitioner: z.boolean().default(false),
});

export function toProjectData({ bccPractitioner, ...fields }) {
  return { ...fields, bccPractitioner: bccPractitioner ? "Y" : "N" };
}
