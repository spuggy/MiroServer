import crypto from "node:crypto";

export function sha1(value) {
  return crypto.createHash("sha1").update(value).digest("hex");
}

export function verifyLegacyPassword(plainTextPassword, storedPassword) {
  if (!plainTextPassword || !storedPassword) {
    return false;
  }

  if (plainTextPassword === storedPassword) {
    return true;
  }

  return sha1(plainTextPassword) === storedPassword.toLowerCase();
}

export function createLegacyPassword() {
  return crypto.randomBytes(6).toString("base64url");
}
