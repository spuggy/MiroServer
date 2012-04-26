ALTER TABLE mr.miroprojects ADD COLUMN costcode character varying(50);
ALTER TABLE mr.miroprojects ALTER COLUMN costcode SET STORAGE EXTENDED;