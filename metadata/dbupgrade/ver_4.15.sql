-- Table: mr.lookupdefs

-- DROP TABLE mr.lookupdefs;

CREATE TABLE mr.mobrizalerts
(
  id bigint NOT NULL,


	
	executionOrder integer NOT NULL,
  emailAdress character varying(100) NOT NULL,	
  
  emailBody text,
  emailSubject character varying(255) ,
  
  alertTitle character varying(100) NOT NULL,
  
  alertRules text NOT NULL,
   enabled boolean NOT NULL,
     stopProcessing boolean NOT NULL,
 
  "checkpoint" bigint NOT NULL,
  created_on timestamp without time zone NOT NULL,
  updated_at timestamp without time zone NOT NULL,
  version integer NOT NULL,
  deleted boolean NOT NULL,
  createdby_id bigint NOT NULL,
  lastupdatedby_id bigint NOT NULL,
  account_id bigint NOT NULL,
  CONSTRAINT mobrizalert_pkey PRIMARY KEY (id),
  CONSTRAINT fk2a93b7079a76b56 FOREIGN KEY (account_id)
      REFERENCES mr.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE mr.mobrizalerts OWNER TO mobriz;


ALTER TABLE mr.responses ADD COLUMN alertsProcessed boolean;
ALTER TABLE mr.responses ALTER COLUMN alertsProcessed SET STORAGE PLAIN;
update mr.responses set alertsProcessed = true;
ALTER TABLE mr.responses ALTER COLUMN alertsProcessed SET NOT NULL;
