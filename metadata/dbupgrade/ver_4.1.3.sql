ALTER TABLE mr.responses ADD COLUMN photosStamped boolean;
ALTER TABLE mr.responses ALTER COLUMN photosStamped SET STORAGE PLAIN;
update mr.responses set photosStamped = false;
ALTER TABLE mr.responses ALTER COLUMN deleted SET NOT NULL;

INSERT INTO mr.setting (id, account_id, settingdescription, settingname, settingvalue) VALUES (3, 1, 'Question shortnames that will be used to stamp photos.  Separate the values with a semi-colon ";" ', 'PHOTO_QUESTION_NAMES', 'Store_id;store id;id');



INSERT INTO mr.setting (id, account_id, settingdescription, settingname, settingvalue) VALUES (4, 1, 'The Subject line for new account emails', 'NEWUSER_EMAILSUBJECT', 'Swift Account information');
INSERT INTO mr.setting (id, account_id, settingdescription, settingname, settingvalue) VALUES (5, 1, 'The message for new account emails', 'NEWUSER_EMAILMESSAGE', 'Your Swift account has been created, see login information below.');

ALTER TABLE app_user ADD COLUMN deleted boolean;
ALTER TABLE app_user ALTER COLUMN deleted SET STORAGE PLAIN;
update app_user set deleted = false;
ALTER TABLE app_user ALTER COLUMN deleted SET NOT NULL;
