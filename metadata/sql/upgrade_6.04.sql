ALTER TABLE mr.miroteams ADD COLUMN deleted boolean;
ALTER TABLE mr.miroteams ALTER COLUMN deleted SET STORAGE PLAIN;
update mr.miroteams set deleted = false;
ALTER TABLE mr.miroteams ALTER COLUMN deleted SET NOT NULL;
