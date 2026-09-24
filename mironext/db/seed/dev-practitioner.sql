-- Local development only: a practitioner you can sign in as, with one project
-- and two candidates, on a restored copy of the legacy database.
--   login: devprac / devpass123
-- Safe to re-run (it does nothing if devprac already exists).
-- Apply: docker exec -i miro-postgres psql -U mobriz -d mirotestdb < db/seed/dev-practitioner.sql

DO $$
DECLARE
  prac_id      BIGINT;
  project_id   BIGINT;
  cand_new_id  BIGINT;
  cand_done_id BIGINT;
  done_response BIGINT := (SELECT MAX(id) FROM mr.responses WHERE survey_id = 5);
BEGIN
  IF EXISTS (SELECT 1 FROM public.app_user WHERE username = 'devprac') THEN
    RAISE NOTICE 'devprac already exists; nothing to do';
    RETURN;
  END IF;

  prac_id := (SELECT MAX(id) + 1 FROM public.app_user);
  INSERT INTO public.app_user (
    id, version, user_type, default_survey_id, status, creditbalance, response_id, project_id,
    deleted, employeeref, pinnumber, account_id, username, password, first_name, last_name, email,
    enabled, gdpr, account_expired, account_locked, credentials_expired, checkpoint,
    created_on, updated_at, createdby_id, oldreport, company, phone_number, address1, city
  ) VALUES (
    prac_id, 0, 'DEFAULT', '5', 0, 10, 0, 0,
    FALSE, 'N/A', 'N/A', 1, 'devprac', encode(digest('devpass123', 'sha1'), 'hex'),
    'Dev', 'Practitioner', 'devprac@miro.dev.local',
    'Y', '1', 'N', 'N', 'N', 0,
    now(), now(), prac_id, FALSE, 'Dev Consulting Ltd', '01234 567890', '1 Test Street', 'Swindon'
  );

  project_id := (SELECT MAX(id) + 1 FROM mr.miroprojects);
  INSERT INTO mr.miroprojects (
    id, version, projecttitle, projectdescription, costcode, emailinvitesubject, emailinvitetext,
    projectstatus, bcc_practitioner, checkpoint, createdby_id, created_on, lastupdatedby_id, updated_at
  ) VALUES (
    project_id, 0, 'Dev test project', 'Local project for trying invites, assessments and reports',
    'DEV', 'Your MiRo assessment', 'Please complete your MiRo behavioural assessment using the link below.',
    1, 'N', 0, prac_id, now(), prac_id, now()
  );

  cand_new_id := (SELECT MAX(id) + 1 FROM public.app_user);
  INSERT INTO public.app_user (
    id, version, user_type, status, creditbalance, response_id, project_id, deleted, employeeref,
    pinnumber, account_id, username, password, first_name, last_name, email, enabled, gdpr,
    account_expired, account_locked, credentials_expired, checkpoint, created_on, updated_at,
    createdby_id, oldreport
  ) VALUES (
    cand_new_id, 0, 'DEFAULT', 0, 0, 0, project_id, FALSE, 'N/A', 'N/A', 1,
    'devcandidate' || cand_new_id, encode(gen_random_bytes(20), 'hex'), 'Ada', 'Lovelace',
    'ada@miro.dev.local', '1', '0', '0', '0', '0', 0, now(), now(), prac_id, FALSE
  );

  -- Already finished (points at an existing real V11 response) so "Buy report" works straight away.
  cand_done_id := (SELECT MAX(id) + 1 FROM public.app_user);
  INSERT INTO public.app_user (
    id, version, user_type, status, creditbalance, response_id, project_id, deleted, employeeref,
    pinnumber, account_id, username, password, first_name, last_name, email, enabled, gdpr,
    account_expired, account_locked, credentials_expired, checkpoint, created_on, updated_at,
    createdby_id, oldreport
  ) VALUES (
    cand_done_id, 0, 'DEFAULT', 30, 0, done_response, project_id, FALSE, 'N/A', 'N/A', 1,
    'devcandidate' || cand_done_id, encode(gen_random_bytes(20), 'hex'), 'Grace', 'Hopper',
    'grace@miro.dev.local', '1', '0', '0', '0', '0', 0, now(), now(), prac_id, FALSE
  );

  RAISE NOTICE 'devprac=% project=% candidates=%,%', prac_id, project_id, cand_new_id, cand_done_id;
END $$;
