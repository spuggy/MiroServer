export function isEnabledFlag(enabled) {
  if (enabled == null) {
    return false;
  }

  const normalized = String(enabled).toLowerCase();
  return normalized === "1" || normalized === "t" || normalized === "y";
}
