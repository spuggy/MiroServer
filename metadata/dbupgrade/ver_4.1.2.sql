ALTER TABLE mr.surveys ADD COLUMN lastPhotoStampResponse_id bigint;
ALTER TABLE mr.surveys ALTER COLUMN lastPhotoStampResponse_id SET STORAGE PLAIN;
update mr.surveys set lastPhotoStampResponse_id = 0 ;
ALTER TABLE mr.surveys ALTER COLUMN lastPhotoStampResponse_id SET NOT NULL;

ALTER TABLE mr.surveys ADD COLUMN lastZipResponse_id bigint;
ALTER TABLE mr.surveys ALTER COLUMN lastZipResponse_id SET STORAGE PLAIN;
update mr.surveys set lastZipResponse_id = 0 ;
ALTER TABLE mr.surveys ALTER COLUMN lastZipResponse_id SET NOT NULL;

