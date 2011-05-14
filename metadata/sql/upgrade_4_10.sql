CREATE TABLE mr.lookupdefs
(
  id int8 NOT NULL,
  name varchar(30) NOT NULL,
  description varchar(255) NOT NULL,
  lookupdesc1 varchar(30) NOT NULL,
  lookupdesc2 varchar(30) NOT NULL,
  lookupdesc3 varchar(30) NOT NULL,
  lookupdesc4 varchar(30) NOT NULL,
  lookupdesc5 varchar(30) NOT NULL,
  "checkpoint" int8 NOT NULL,
  created_on timestamp NOT NULL,
  updated_at timestamp NOT NULL,
  version int4 NOT NULL,
  deleted bool NOT NULL,
  branch_jquestion_id int8 NOT NULL DEFAULT 0,
  createdby_id int8 NOT NULL,
  lastupdatedby_id int8 NOT NULL,
  account_id int8 NOT NULL,
  lookupdesc6 varchar(30),
  lookupdesc7 varchar(30),
  lookupsearch1 bool NOT NULL,
  lookupsearch2 bool NOT NULL,
  lookupsearch3 bool NOT NULL,
  lookupsearch4 bool NOT NULL,
  lookupsearch5 bool NOT NULL,
  lookupsearch6 bool NOT NULL,
  lookupsearch7 bool NOT NULL,
  lookupreport1 bool NOT NULL,
  lookupreport2 bool NOT NULL,
  lookupreport3 bool NOT NULL,
  lookupreport4 bool NOT NULL,
  lookupreport5 bool NOT NULL,
  lookupreport6 bool NOT NULL,
  lookupreport7 bool NOT NULL,
  lookupprompt varchar(100),
  CONSTRAINT lookupdef_pkey PRIMARY KEY (id),
  CONSTRAINT fk2a93b7079a76b56 FOREIGN KEY (account_id) REFERENCES mr.account (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITHOUT OIDS;
ALTER TABLE mr.lookupdefs OWNER TO mobriz;


CREATE TABLE mr.lookupdefitems
(
  id int8 NOT NULL,
  lookupdef_id int8 NOT NULL,
  lookup1 varchar(50) NOT NULL,
  lookup2 varchar(50) NOT NULL,
  lookup3 varchar(50) NOT NULL,
  lookup4 varchar(50) NOT NULL,
  lookup5 varchar(50) NOT NULL,
  deleted bool NOT NULL,
  account_id int8 NOT NULL,
  lookup6 varchar(50) NOT NULL,
  lookup7 varchar(50) NOT NULL,
  CONSTRAINT lookupdefitems_pkey PRIMARY KEY (id),
  CONSTRAINT fk2a93b7079a76b56 FOREIGN KEY (account_id) REFERENCES mr.account (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITHOUT OIDS;
ALTER TABLE mr.lookupdefitems OWNER TO mobriz;
;



ALTER TABLE mr.questions ADD COLUMN qmeta varchar(255);
ALTER TABLE mr.questions ALTER COLUMN qmeta SET STORAGE EXTENDED;

