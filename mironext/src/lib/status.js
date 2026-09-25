const CANDIDATE_STATUS_MAP = {
  0: "Not invited",
  10: "Invite sent",
  20: "Assessment complete",
  30: "Ready to buy",
  40: "Report purchased",
};

export function toCandidateStatusLabel(status) {
  return CANDIDATE_STATUS_MAP[status] || `Status ${status ?? "unknown"}`;
}
