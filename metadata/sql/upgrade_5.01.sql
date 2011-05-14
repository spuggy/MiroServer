

CREATE TABLE mr.mirotransactions
(
  id bigint NOT NULL,
  user_id bigint NOT NULL,
  status integer NOT NULL,
  errorcode integer NOT NULL,
  paymentmethod integer NOT NULL,
  credits integer NOT NULL,
  "checkpoint" bigint NOT NULL,
  created_on timestamp without time zone NOT NULL,
  updated_at timestamp without time zone NOT NULL,
  version integer NOT NULL,
  deleted boolean NOT NULL,
  createdby_id bigint NOT NULL,
  lastupdatedby_id bigint NOT NULL,
  transvalue double precision,
  paymentstatus character varying(255),
  paymentstatusdetail character varying(255),
  paymenttransid character varying(255),
  CONSTRAINT mirotrans_prim_key PRIMARY KEY (id),
  CONSTRAINT appusers_fk FOREIGN KEY (user_id)
      REFERENCES app_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE mr.mirotransactions OWNER TO mobriz;

-- Index: mr.fki_appusers_fk

-- DROP INDEX mr.fki_appusers_fk;

CREATE INDEX fki_appusers_fk
  ON mr.mirotransactions
  USING btree
  (user_id);

-- Index: mr.mirotransactions_pkey

-- DROP INDEX mr.mirotransactions_pkey;

CREATE UNIQUE INDEX mirotransactions_pkey
  ON mr.mirotransactions
  USING btree
  (id);






ALTER TABLE app_user ADD COLUMN creditbalance integer;
ALTER TABLE app_user ALTER COLUMN creditbalance SET STORAGE PLAIN;
update app_user set creditbalance = 0 ;
ALTER TABLE app_user ALTER COLUMN creditbalance SET NOT NULL;


INSERT INTO mr.setting (id, account_id, settingdescription, settingname, settingvalue) VALUES (6, 1, 'The price of a credit make sure its a valid number!! ";"', 'MIRO_CREDIT_PRICE', '20.00');
INSERT INTO mr.setting (id, account_id, settingdescription, settingname, settingvalue) VALUES (7, 1, 'The number of credits a new prac get ";"', 'NEWUSER_CREDITS', '10');

ALTER TABLE app_user ADD COLUMN status integer;
update app_user set status = 0;
update app_user set status = 40 where response_id in (select id from mr.responses where alertsprocessed = true);
update app_user set status = 10 where response_id in (select id from mr.responses where alertsprocessed = false);
ALTER TABLE app_user ALTER COLUMN status SET STORAGE PLAIN;
ALTER TABLE app_user ALTER COLUMN status SET NOT NULL;

ALTER TABLE mr.miroprojects ADD COLUMN emailinvitesubject text;
ALTER TABLE mr.miroprojects ALTER COLUMN emailinvitesubject SET STORAGE EXTENDED;



ALTER TABLE app_user ADD COLUMN webaddress character varying(255);
ALTER TABLE app_user ALTER COLUMN webaddress SET STORAGE EXTENDED;


ALTER TABLE app_user ADD COLUMN company character varying(100);
    ALTER TABLE app_user ALTER COLUMN company SET STORAGE EXTENDED;



ALTER TABLE app_user ADD COLUMN postcode character varying(100);
    ALTER TABLE app_user ALTER COLUMN postcode SET STORAGE EXTENDED;


ALTER TABLE app_user ADD COLUMN county character varying(100);
    ALTER TABLE app_user ALTER COLUMN county SET STORAGE EXTENDED;


ALTER TABLE app_user ADD COLUMN city character varying(100);
    ALTER TABLE app_user ALTER COLUMN city SET STORAGE EXTENDED;

    ALTER TABLE app_user ADD COLUMN address1 character varying(100);
    ALTER TABLE app_user ALTER COLUMN address1 SET STORAGE EXTENDED;

ALTER TABLE app_user ADD COLUMN address2 character varying(100);
    ALTER TABLE app_user ALTER COLUMN address2 SET STORAGE EXTENDED;



ALTER TABLE app_user ADD COLUMN created_on timestamp without time zone;
ALTER TABLE app_user ALTER COLUMN created_on SET STORAGE PLAIN;
update app_user set created_on = now();
ALTER TABLE app_user ALTER COLUMN created_on SET NOT NULL;

ALTER TABLE app_user ADD COLUMN oldreport boolean;
ALTER TABLE app_user ALTER COLUMN oldreport SET STORAGE PLAIN;
update app_user set oldreport = true;
ALTER TABLE app_user ALTER COLUMN oldreport SET NOT NULL;

ALTER TABLE app_user ADD COLUMN updated_at timestamp without time zone;
ALTER TABLE app_user ALTER COLUMN updated_at SET STORAGE PLAIN;


ALTER TABLE app_user ADD COLUMN lastupdatedby_id bigint;
ALTER TABLE app_user ALTER COLUMN lastupdatedby_id SET STORAGE PLAIN;

ALTER TABLE app_user ADD COLUMN createdby_id bigint;
ALTER TABLE app_user ALTER COLUMN createdby_id SET STORAGE PLAIN;
