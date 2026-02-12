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

### Playwright (E2E + Component)

1. Copy the test env template and adjust values if needed.
2. Start and seed the isolated Docker Postgres test database.
3. Run E2E and component tests.

```bash
cp .env.test.template .env.test
npm run testdb:reset
npm run test:e2e
npm run test:ct
```

Or run both suites together:

```bash
npm run test:all
```

Useful helper commands:

```bash
npm run testdb:up
npm run testdb:seed
npm run testdb:down
npm run playwright:install
```

### Test Results

- E2E machine-readable results: `test-results/e2e/results.json`
- E2E JUnit: `test-results/e2e/junit.xml`
- E2E HTML report: `playwright-report/e2e/index.html`
- CT machine-readable results: `test-results/ct/results.json`
- CT JUnit: `test-results/ct/junit.xml`
- CT HTML report: `playwright-report/ct/index.html`

## Formatting

```bash
npm run format
```
