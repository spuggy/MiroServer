drop schema public cascade ; 
CREATE SCHEMA public
  AUTHORIZATION mobriz;
GRANT ALL ON SCHEMA public TO mobriz;
GRANT ALL ON SCHEMA public TO test;
COMMENT ON SCHEMA public IS 'Standard public schema';

CREATE TABLE app_user
(
  id int8 NOT NULL,
  version int4 NOT NULL,
  username varchar(50) NOT NULL,
  "password" varchar(255) NOT NULL,
  first_name varchar(50) NOT NULL,
  last_name varchar(50) NOT NULL,
  email varchar(255) NOT NULL,
  password_hint varchar(255),
  account_enabled char(1),
  account_expired char(1) NOT NULL,
  account_locked char(1) NOT NULL,
  credentials_expired char(1) NOT NULL,
  phone_number varchar(255),
  CONSTRAINT app_user_pkey PRIMARY KEY (id),
  CONSTRAINT app_user_email_key UNIQUE (email),
  CONSTRAINT app_user_username_key UNIQUE (username)
) 
WITH OIDS;
ALTER TABLE app_user OWNER TO mobriz;


CREATE TABLE role
(
  id int8 NOT NULL,
  name varchar(20),
  description varchar(64),
  CONSTRAINT role_pkey PRIMARY KEY (id)
) 
WITH OIDS;
ALTER TABLE role OWNER TO mobriz;

CREATE TABLE user_role
(
  user_id int8 NOT NULL,
  role_id int8 NOT NULL,
  CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk143bf46a6934ae9e FOREIGN KEY (user_id) REFERENCES app_user (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk143bf46ac409eabe FOREIGN KEY (role_id) REFERENCES role (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITH OIDS;
ALTER TABLE user_role OWNER TO mobriz;


CREATE OR REPLACE VIEW mr.responsecount AS 
 SELECT responses.survey_id AS id, count(*) AS count
   FROM mr.responses
  GROUP BY responses.survey_id;

ALTER TABLE mr.responsecount OWNER TO mobriz;



ALTER TABLE mr.setting
  ADD CONSTRAINT "AccountAndName" UNIQUE(account_id, settingname);



'get rid of username fields 
ALTER TABLE mr.responses ADD COLUMN createdby_id int8;
ALTER TABLE mr.responses ALTER COLUMN createdby_id SET STORAGE PLAIN;

ALTER TABLE mr.responses ADD COLUMN lastupdatedby_id int8;
ALTER TABLE mr.responses ALTER COLUMN lastupdatedby_id SET STORAGE PLAIN;


update mr.responses set 
createdby_id = u.id,
lastupdatedby_id = u.id 
from mr.responses mr inner join app_user u
on mr.createdby = u.username ;

ALTER TABLE mr.responses DROP COLUMN createdby ;

ALTER TABLE mr.responses DROP COLUMN lastupdateby ;

ALTER TABLE mr.responses ALTER COLUMN createdby_id SET NOT NULL;
ALTER TABLE mr.responses ALTER COLUMN lastupdatedby_id SET NOT NULL;

'update optiuons
ALTER TABLE mr.options ADD COLUMN createdby_id int8;
ALTER TABLE mr.options ALTER COLUMN createdby_id SET STORAGE PLAIN;

ALTER TABLE mr.options ADD COLUMN lastupdatedby_id int8;
ALTER TABLE mr.options ALTER COLUMN lastupdatedby_id SET STORAGE PLAIN;


update mr.options set 
createdby_id = u.id,
lastupdatedby_id = u.id 
from mr.options mr inner join app_user u
on mr.createdby = u.username ;

ALTER TABLE mr.options DROP COLUMN createdby ;

ALTER TABLE mr.options DROP COLUMN lastupdateby ;

ALTER TABLE mr.options ALTER COLUMN createdby_id SET NOT NULL;
ALTER TABLE mr.options ALTER COLUMN lastupdatedby_id SET NOT NULL;


'update constraints
ALTER TABLE mr.constraints ADD COLUMN createdby_id int8;
ALTER TABLE mr.constraints ALTER COLUMN createdby_id SET STORAGE PLAIN;

ALTER TABLE mr.constraints ADD COLUMN lastupdatedby_id int8;
ALTER TABLE mr.constraints ALTER COLUMN lastupdatedby_id SET STORAGE PLAIN;


update mr.constraints set 
createdby_id = u.id,
lastupdatedby_id = u.id 
from mr.constraints mr inner join app_user u
on mr.createdby = u.username ;

ALTER TABLE mr.constraints DROP COLUMN createdby ;

ALTER TABLE mr.constraints DROP COLUMN lastupdateby ;

ALTER TABLE mr.constraints ALTER COLUMN createdby_id SET NOT NULL;
ALTER TABLE mr.constraints ALTER COLUMN lastupdatedby_id SET NOT NULL;

'suer_user'
ALTER TABLE mr.survey_user ADD COLUMN user_id int8;

update mr.survey_user set 
user_id = u.id 
from mr.survey_user mr inner join app_user u
on mr.username = u.username ;

ALTER TABLE mr.survey_user ALTER COLUMN user_id SET NOT NULL;
ALTER TABLE mr.survey_user DROP COLUMN username ;


'surveys
ALTER TABLE mr.surveys ADD COLUMN createdby_id int8;
ALTER TABLE mr.surveys ALTER COLUMN createdby_id SET STORAGE PLAIN;

ALTER TABLE mr.surveys ADD COLUMN lastupdatedby_id int8;
ALTER TABLE mr.surveys ALTER COLUMN lastupdatedby_id SET STORAGE PLAIN;


update mr.surveys set 
createdby_id = u.id,
lastupdatedby_id = u.id 
from mr.surveys mr inner join app_user u
on mr.createdby = u.username ;

ALTER TABLE mr.surveys DROP COLUMN createdby ;

ALTER TABLE mr.surveys DROP COLUMN lastupdateby ;


ALTER TABLE mr.surveys ADD COLUMN photoFile varchar(255);
ALTER TABLE mr.surveys ALTER COLUMN photoFile SET STORAGE EXTENDED;
