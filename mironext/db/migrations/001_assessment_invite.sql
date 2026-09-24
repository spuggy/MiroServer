-- Emailed, login-free assessment links. Apply with:
--   psql "$DATABASE_URL" -f db/migrations/001_assessment_invite.sql
CREATE TABLE IF NOT EXISTS public.assessment_invite (
  id            BIGSERIAL PRIMARY KEY,
  token_hash    CHAR(64)    NOT NULL UNIQUE,
  candidate_id  BIGINT      NOT NULL REFERENCES public.app_user (id),
  survey_id     BIGINT      NOT NULL,
  progress      JSONB       NOT NULL DEFAULT '{}'::jsonb,
  created_on    TIMESTAMPTZ NOT NULL DEFAULT now(),
  expires_at    TIMESTAMPTZ NOT NULL,
  last_used_at  TIMESTAMPTZ,
  revoked_at    TIMESTAMPTZ,
  completed_at  TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS assessment_invite_candidate_id_idx
  ON public.assessment_invite (candidate_id);
