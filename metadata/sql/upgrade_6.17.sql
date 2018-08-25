ALTER TABLE app_user ADD COLUMN gdpr character(1);
update app_user set gdpr = 'N';