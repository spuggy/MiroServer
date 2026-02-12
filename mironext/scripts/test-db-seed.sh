#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
COMPOSE_FILE="${ROOT_DIR}/docker-compose.test.yml"

echo "Waiting for postgres-test to become ready..."
for _ in {1..30}; do
  if docker compose -f "${COMPOSE_FILE}" exec -T postgres-test pg_isready -U miro_test -d miro_next_test >/dev/null 2>&1; then
    break
  fi
  sleep 1
done

if ! docker compose -f "${COMPOSE_FILE}" exec -T postgres-test pg_isready -U miro_test -d miro_next_test >/dev/null 2>&1; then
  echo "postgres-test did not become ready in time."
  exit 1
fi

echo "Applying SQL fixture seed..."
docker compose -f "${COMPOSE_FILE}" exec -T postgres-test \
  psql -U miro_test -d miro_next_test -v ON_ERROR_STOP=1 -f /seed/test-seed.sql

echo "Seed complete."
