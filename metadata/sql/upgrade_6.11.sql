ALTER TABLE app_user ADD COLUMN default_survey_id character varying(20);
update app_user set default_survey_id = '4'