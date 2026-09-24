// Candidate (app_user.status) lifecycle, from the legacy User model.
export const CANDIDATE_STATUS = {
  INVITE_NOT_SENT: 0,
  INVITE_SENT: 10,
  ASSESSMENT_COMPLETE: 20,
  /** Assessment scored; the practitioner can buy the report. */
  PURCHASE_REPORT: 30,
  /** Report bought; it can be downloaded. */
  DOWNLOAD_REPORT: 40,
} as const;

/** mr.mirotransactions.status for a report purchase (MiroTransaction.OK_BUY_REPORT). */
export const TRANSACTION_OK_BUY_REPORT = 6;

export function isEnabledFlagValue(value: string | null | undefined): boolean {
  if (value == null) return false;
  const v = String(value).trim().toLowerCase();
  return v === "1" || v === "y" || v === "t" || v === "true";
}
