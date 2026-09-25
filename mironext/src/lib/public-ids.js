import crypto from "node:crypto";

// Database ids never leave the server as-is. Each id is encrypted, together
// with a kind tag, as a single AES block and sent as a 22-character base64url
// token, so URLs can't be enumerated or edited to reach another record, and a
// project token can't be used as a candidate id. Decoding rejects anything
// that wasn't produced by encodeId with the same key and kind.
//
// The key is derived from ID_SECRET (or NEXTAUTH_SECRET); changing it breaks
// existing links.

const KINDS = { project: 1, candidate: 2, customer: 3 };
const TOKEN_PATTERN = /^[A-Za-z0-9_-]{22}$/;

let cachedKey = null;

function key() {
  if (cachedKey) return cachedKey;
  const secret = process.env.ID_SECRET || process.env.NEXTAUTH_SECRET;
  if (!secret) {
    throw new Error("ID_SECRET or NEXTAUTH_SECRET must be set to encode ids");
  }
  cachedKey = crypto.createHash("sha256").update(`miro-public-id:${secret}`).digest();
  return cachedKey;
}

function kindTag(kind) {
  const tag = KINDS[kind];
  if (!tag) throw new Error(`Unknown id kind: ${kind}`);
  return tag;
}

/** Encodes a database id (bigint, number or numeric string) for use in URLs and client code. */
export function encodeId(kind, id) {
  const block = Buffer.alloc(16);
  block[0] = kindTag(kind);
  block.writeBigUInt64BE(BigInt(id), 8);
  const cipher = crypto.createCipheriv("aes-256-ecb", key(), null);
  cipher.setAutoPadding(false);
  return Buffer.concat([cipher.update(block), cipher.final()]).toString("base64url");
}

/** Decodes a token from encodeId back to a bigint id, or null if it isn't valid for `kind`. */
export function decodeId(kind, token) {
  const tag = kindTag(kind);
  if (typeof token !== "string" || !TOKEN_PATTERN.test(token)) return null;

  const decipher = crypto.createDecipheriv("aes-256-ecb", key(), null);
  decipher.setAutoPadding(false);
  const block = Buffer.concat([decipher.update(Buffer.from(token, "base64url")), decipher.final()]);

  if (block[0] !== tag) return null;
  for (let i = 1; i < 8; i += 1) {
    if (block[i] !== 0) return null;
  }
  return block.readBigUInt64BE(8);
}

export const encodeProjectId = (id) => encodeId("project", id);
export const decodeProjectId = (token) => decodeId("project", token);
export const encodeCandidateId = (id) => encodeId("candidate", id);
export const decodeCandidateId = (token) => decodeId("candidate", token);
export const encodeCustomerId = (id) => encodeId("customer", id);
export const decodeCustomerId = (token) => decodeId("customer", token);
