BEGIN;

CREATE SCHEMA IF NOT EXISTS mr;

CREATE SEQUENCE IF NOT EXISTS public.hibernate_sequence
  INCREMENT BY 1
  MINVALUE 1
  START WITH 5000;

CREATE TABLE IF NOT EXISTS mr.account (
  id BIGINT PRIMARY KEY,
  companyname VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.app_user (
  id BIGINT PRIMARY KEY,
  version INTEGER NOT NULL DEFAULT 0,
  user_type VARCHAR(50),
  account_id BIGINT NOT NULL,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  enabled CHAR(1),
  deleted BOOLEAN DEFAULT FALSE,
  account_expired CHAR(1) NOT NULL DEFAULT '0',
  account_locked CHAR(1) NOT NULL DEFAULT '0',
  credentials_expired CHAR(1) NOT NULL DEFAULT '0',
  created_on TIMESTAMPTZ DEFAULT NOW(),
  createdby_id BIGINT,
  updated_at TIMESTAMPTZ DEFAULT NOW(),
  project_id BIGINT,
  response_id BIGINT,
  gdpr CHAR(1),
  password_hint VARCHAR(255),
  status INTEGER,
  creditbalance INTEGER,
  checkpoint BIGINT,
  oldreport BOOLEAN,
  CONSTRAINT app_user_account_fk FOREIGN KEY (account_id) REFERENCES mr.account (id)
);

CREATE TABLE IF NOT EXISTS mr.miroprojects (
  id BIGINT PRIMARY KEY,
  version INTEGER,
  projecttitle VARCHAR(100) NOT NULL,
  projectdescription VARCHAR(254) NOT NULL,
  costcode VARCHAR(50) NOT NULL,
  emailinvitesubject VARCHAR(50) NOT NULL,
  emailinvitetext VARCHAR(100) NOT NULL,
  projectstatus INTEGER,
  createdby_id BIGINT,
  created_on TIMESTAMPTZ DEFAULT NOW(),
  lastupdatedby_id BIGINT,
  updated_at TIMESTAMPTZ DEFAULT NOW(),
  bcc_practitioner CHAR(1),
  checkpoint BIGINT
);

INSERT INTO mr.account (id, companyname)
VALUES (2001, 'Playwright Co')
ON CONFLICT (id) DO UPDATE SET
  companyname = EXCLUDED.companyname;

INSERT INTO public.app_user (
  id,
  version,
  user_type,
  account_id,
  username,
  password,
  first_name,
  last_name,
  email,
  enabled,
  deleted,
  account_expired,
  account_locked,
  credentials_expired,
  created_on,
  createdby_id,
  updated_at,
  project_id,
  response_id,
  gdpr,
  password_hint,
  status,
  creditbalance,
  checkpoint,
  oldreport
)
VALUES (
  1001,
  0,
  'ADMIN',
  2001,
  'pw_admin',
  'pw_password_123',
  'Playwright',
  'Admin',
  'pw_admin@example.test',
  '1',
  FALSE,
  '0',
  '0',
  '0',
  NOW(),
  1001,
  NOW(),
  NULL,
  NULL,
  '0',
  NULL,
  0,
  0,
  0,
  FALSE
)
ON CONFLICT (id) DO UPDATE SET
  username = EXCLUDED.username,
  password = EXCLUDED.password,
  first_name = EXCLUDED.first_name,
  last_name = EXCLUDED.last_name,
  email = EXCLUDED.email,
  enabled = EXCLUDED.enabled,
  deleted = EXCLUDED.deleted,
  updated_at = NOW();

INSERT INTO mr.miroprojects (
  id,
  version,
  projecttitle,
  projectdescription,
  costcode,
  emailinvitesubject,
  emailinvitetext,
  projectstatus,
  createdby_id,
  created_on,
  lastupdatedby_id,
  updated_at,
  bcc_practitioner,
  checkpoint
)
VALUES (
  3001,
  0,
  'Seeded Project',
  'Project created by automated Playwright fixture seed.',
  'STANDARD',
  'You have been invited to Miro',
  'Please complete your Miro assessment.',
  0,
  1001,
  NOW(),
  1001,
  NOW(),
  '0',
  0
)
ON CONFLICT (id) DO UPDATE SET
  projecttitle = EXCLUDED.projecttitle,
  projectdescription = EXCLUDED.projectdescription,
  updated_at = NOW();

SELECT setval('public.hibernate_sequence', 9000, true);

COMMIT;
