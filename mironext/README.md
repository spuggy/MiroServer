# Miro Next

Next.js rewrite of the legacy Java Miro app using Prisma, NextAuth, and MUI.

## Implemented scope

- `app_user` credential login with NextAuth credentials provider.
- Project list for current user, with pagination.
- Create new project.
- Project detail with candidate list and name filter.
- Add candidate via popup dialog.
- Top menu: My Projects, Team Reports, Help, More.
- Unit tests for key helpers.

## Setup

1. Copy `.env.example` to `.env` and fill values.
2. Point `DATABASE_URL` at your existing legacy database created from `create-tables.sql` (with `mr` and `public` schemas).
3. Install dependencies.
4. Generate Prisma client.
5. Run the app.

```bash
npm install
npm run prisma:generate
npm run dev
```

## Testing

```bash
npm run test
```

## Formatting

```bash
npm run format
```
