# Prisma schema notes

- `schema.prisma` maps the core legacy tables used by migrated features.
- It is configured for the original legacy schemas: `public` and `mr`.
- Prisma 7 connection URL config lives in `prisma.config.ts` (not in `schema.prisma`).

Run:

```bash
npm run prisma:generate
```
