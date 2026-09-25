import crypto from "node:crypto";
import { promisify } from "node:util";

const scrypt = promisify(crypto.scrypt);

// New passwords are stored as "scrypt$N$r$p$<salt>$<hash>" (salt/hash base64url).
// Legacy rows (SHA-1 hex or plain text, from the Java app) still verify, but
// anything this app writes uses scrypt.
const SCRYPT_PREFIX = "scrypt$";
const SCRYPT_PARAMS = { N: 16384, r: 8, p: 1 };
const KEY_LENGTH = 64;

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

export function isScryptHash(storedPassword) {
  return typeof storedPassword === "string" && storedPassword.startsWith(SCRYPT_PREFIX);
}

export async function hashPassword(plainTextPassword) {
  const { N, r, p } = SCRYPT_PARAMS;
  const salt = crypto.randomBytes(16);
  const hash = await scrypt(plainTextPassword, salt, KEY_LENGTH, { N, r, p });
  return `${SCRYPT_PREFIX}${N}$${r}$${p}$${salt.toString("base64url")}$${hash.toString("base64url")}`;
}

/** Checks a password against any stored format (scrypt, SHA-1 hex or legacy plain text). */
export async function verifyPassword(plainTextPassword, storedPassword) {
  if (!plainTextPassword || !storedPassword) {
    return false;
  }

  // Never fall through to the legacy checks for scrypt rows: the plain-text
  // comparison would accept the stored hash string itself as a password.
  if (!isScryptHash(storedPassword)) {
    return verifyLegacyPassword(plainTextPassword, storedPassword);
  }

  const parts = storedPassword.split("$");
  if (parts.length !== 6) {
    return false;
  }
  const [, n, r, p, saltText, hashText] = parts;
  const expected = Buffer.from(hashText, "base64url");
  if (expected.length === 0) {
    return false;
  }

  try {
    const actual = await scrypt(plainTextPassword, Buffer.from(saltText, "base64url"), expected.length, {
      N: Number(n),
      r: Number(r),
      p: Number(p),
    });
    return crypto.timingSafeEqual(actual, expected);
  } catch {
    return false;
  }
}

export function createLegacyPassword() {
  return crypto.randomBytes(6).toString("base64url");
}
