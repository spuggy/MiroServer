INSERT INTO role (id, name, description) VALUES (5, 'departmentadmin', 'Department Administrator');

ALTER TABLE app_user ADD COLUMN department character varying(255);

	
INSERT INTO mr.setting (id, account_id, settingdescription, settingname, settingvalue) VALUES (10, 1, 'MIRO_DEPARTMENTS', 'MIRO_DEPARTMENTS', 'Capita;Capita Consulting;Capita Learning and Development ');
update app_user set department = 'Capita'  ;
	
//INSERT INTO mr.setting (id, account_id, settingdescription, settingname, settingvalue) VALUES (10, 1, 'MIRO_DEPARTMENTS', 'MIRO_DEPARTMENTS', 'miro client');
//update app_user set department = 'miro client'