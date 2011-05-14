CREATE TABLE mr.miroprojects
(
  id bigint NOT NULL,
  projectTitle character varying(100),
  projectDescription character varying(255),
  emailInviteText text,
  projectStatus integer NOT NULL,
  "checkpoint" bigint NOT NULL,
  created_on timestamp without time zone NOT NULL,
  updated_at timestamp without time zone NOT NULL,
  version integer NOT NULL,
  createdby_id bigint NOT NULL,
  lastupdatedby_id bigint NOT NULL,
  CONSTRAINT miroproject_pkey PRIMARY KEY (id)
  
)
WITH (OIDS=FALSE);
ALTER TABLE mr.miroprojects OWNER TO mobriz;


-- Column: id

-- ALTER TABLE app_user DROP COLUMN id;

ALTER TABLE app_user ADD COLUMN project_id bigint;
ALTER TABLE app_user ALTER COLUMN project_id SET STORAGE PLAIN;
update  app_user set project_id = 0 ;
ALTER TABLE app_user ALTER COLUMN project_id SET NOT NULL;


ALTER TABLE app_user ADD COLUMN response_id bigint;
ALTER TABLE app_user ALTER COLUMN response_id SET STORAGE PLAIN;
update app_user set response_id = 0 ;
ALTER TABLE app_user ALTER COLUMN response_id SET NOT NULL;

