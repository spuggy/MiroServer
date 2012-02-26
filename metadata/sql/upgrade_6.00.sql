/* add miro team and the many to many table */


CREATE TABLE mr.miroteams
(
  id bigint NOT NULL,
  miroteamname character varying(100),
  checkpoint bigint NOT NULL,
  created_on timestamp without time zone NOT NULL,
  updated_at timestamp without time zone NOT NULL,
  version integer NOT NULL,
  createdby_id bigint NOT NULL,
  lastupdatedby_id bigint NOT NULL,
  CONSTRAINT miroteam_pkey PRIMARY KEY (id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE mr.miroteams
  OWNER TO mobriz;



CREATE TABLE mr.miroteam_user
(
  miroteam_id bigint NOT NULL,
  user_id bigint NOT NULL,
  CONSTRAINT fk_miroteam FOREIGN KEY (miroteam_id)
      REFERENCES mr.miroteams (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE mr.miroteam_user
  OWNER TO mobriz;
