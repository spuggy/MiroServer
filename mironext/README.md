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

### Local Docker Postgres (dev)

`docker-compose.dev.yml` runs a local Postgres container named `mironextdb` and maps host `5433` to container `5432`, matching the default `DATABASE_URL` in `.env`.

```bash
npm run devdb:up
npm run devdb:psql
npm run devdb:down
```

## Testing

```bash
npm run test
```

### Python report tests

The V11 ReportLab rewrite scaffold lives in `report/`.

```bash
python3 -m venv report/.venv
report/.venv/bin/pip install -r report/requirements.txt
npm run test:report
```

To generate the sample report manually:

```bash
npm run report:generate:sample
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

## Individual report PDF (TypeScript port)

`src/lib/report/` is a TypeScript port of the Java `MiroReport.generateReportV10`
pipeline: scoring (`MiroResponse`), page selection, the XHTML template
(`report-assets/xhtml/mirosource11.xhtml`), and PDF output. Instead of iText, the
report is rendered as HTML/CSS and printed by headless Chromium (Playwright),
with Paged.js handling pagination, the running header/footer and TOC page numbers.

```js
import { calculateScores, generateReportV10 } from "@/lib/report";

const scores = calculateScores(survey, response, { testOffset: 32 });
const { pdf, pageCount } = await generateReportV10({
  testId, surveyId, candidate, practitioner, scores,
  thresholds: { engagedScore: 31, excessScore: 55, latentScore: 8 },
});
```

`npm run test:report:ts` runs the port of `MiroReport10Test` (plus the V10 part of
`MiroReport11Test#testGenerateFreeReport`) and writes the PDFs/HTML to
`test-results/report/`. The generated `.html` opens in a browser for design work.
V11 is not ported yet.

## Formatting

```bash
npm run format
```
