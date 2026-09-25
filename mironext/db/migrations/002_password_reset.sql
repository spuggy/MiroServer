-- Emailed password-reset links for practitioners. Apply with:
--   psql "$DATABASE_URL" -f db/migrations/002_password_reset.sql
CREATE TABLE IF NOT EXISTS public.password_reset (
  id          BIGSERIAL PRIMARY KEY,
  token_hash  CHAR(64)    NOT NULL UNIQUE,
  user_id     BIGINT      NOT NULL REFERENCES public.app_user (id),
  created_on  TIMESTAMPTZ NOT NULL DEFAULT now(),
  expires_at  TIMESTAMPTZ NOT NULL,
  used_at     TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS password_reset_user_id_idx
  ON public.password_reset (user_id);
