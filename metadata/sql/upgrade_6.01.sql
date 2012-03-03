/* added the team report status to the teams table and the mirroteam id to the user table so we can track what team they where on*/

	
ALTER TABLE mr.miroteams ADD COLUMN teamreportstatus integer;
update mr.miroteams set teamreportstatus = 10;
ALTER TABLE mr.miroteams ALTER COLUMN teamreportstatus SET STORAGE PLAIN;
ALTER TABLE mr.miroteams ALTER COLUMN teamreportstatus SET NOT NULL;

ALTER TABLE app_user ADD COLUMN miroteam_id bigint;
ALTER TABLE app_user ALTER COLUMN miroteam_id SET STORAGE PLAIN;

	