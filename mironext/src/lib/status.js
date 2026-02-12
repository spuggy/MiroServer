const CANDIDATE_STATUS_MAP = {
  0: "Invite not sent",
  10: "Invite sent",
  20: "Assessment complete",
  30: "Purchased",
  40: "Ready to download",
};

export function toCandidateStatusLabel(status) {
  return CANDIDATE_STATUS_MAP[status] || `Status ${status ?? "unknown"}`;
}
