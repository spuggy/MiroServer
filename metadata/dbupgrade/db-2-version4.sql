ALTER TABLE app_user ADD COLUMN id int8;
ALTER TABLE app_user ALTER COLUMN id SET STORAGE PLAIN;
ALTER TABLE app_user ADD COLUMN account_id int8;
ALTER TABLE app_user ALTER COLUMN account_id SET STORAGE PLAIN;



update app_user set id = 0, account_id =0 where username = 'tomcat';

update app_user set id = (select max(id)+1 from app_user), account_id =1 where username != 'tomcat'; //new to work on this!!! to make correct ids


CREATE TABLE mr.app_user_backup AS   SELECT * FROM app_user ;


ALTER TABLE mr.survey_user DROP CONSTRAINT fk_username ;



DROP TABLE role  cascade ;
DROP TABLE user_cookie  cascade ;
drop table user_role cascade ;
drop table app_user cascade ;



 




/* create tables */
CREATE TABLE mr.account
(
  id int8 NOT NULL,
  companyname varchar(50) NOT NULL,
  CONSTRAINT account_pkey PRIMARY KEY (id)
) 
WITH OIDS;
ALTER TABLE mr.account OWNER TO mobriz;


CREATE TABLE app_user
(
  id int8 NOT NULL,
  username varchar(40) NOT NULL,
  version int4 NOT NULL,
  pinnumber varchar(255),
  userid int4,
  "password" varchar(255) NOT NULL,
  first_name varchar(50) NOT NULL,
  last_name varchar(50) NOT NULL,
  address varchar(150),
  city varchar(50),
  province varchar(100),
  country varchar(100),
  postal_code varchar(15),
  email varchar(255),
  phone_number varchar(255),
  website varchar(255),
  password_hint varchar(255),
  enabled bool,
  employeeref varchar(20) NOT NULL DEFAULT '-'::character varying,
  "checkpoint" int8 NOT NULL DEFAULT 1,
  oldfullname varchar(200),
  account_id int8,
  CONSTRAINT app_user_pkey PRIMARY KEY (id),
  CONSTRAINT account FOREIGN KEY (account_id) REFERENCES mr.account (id) ON UPDATE RESTRICT ON DELETE RESTRICT,
  CONSTRAINT app_user_username_key UNIQUE (username)
) 
WITH OIDS;
ALTER TABLE app_user OWNER TO test;



CREATE TABLE role
(
  id int8 NOT NULL,
  name varchar(20),
  description varchar(64),
  CONSTRAINT role_pkey PRIMARY KEY (id)
) 
WITH OIDS;
ALTER TABLE role OWNER TO test;


CREATE TABLE user_role
(
  user_id int8 NOT NULL,
  role_id int8 NOT NULL,
  CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk143bf46a6934ae9e FOREIGN KEY (user_id) REFERENCES app_user (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk143bf46ac409eabe FOREIGN KEY (role_id) REFERENCES role (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITH OIDS;
ALTER TABLE user_role OWNER TO test;






CREATE TABLE mr.setting
(
  id int8 NOT NULL,
  settingdescription varchar(255) NOT NULL,
  settingname varchar(30) NOT NULL,
  settingvalue varchar(255) NOT NULL,
  account_id int8,
  CONSTRAINT setting_pkey PRIMARY KEY (id),
  CONSTRAINT account FOREIGN KEY (account_id) REFERENCES mr.account (id) ON UPDATE RESTRICT ON DELETE RESTRICT
) 
WITH OIDS;
ALTER TABLE mr.setting OWNER TO mobriz;


INSERT INTO mr.account(id, companyname)
  VALUES(0, 'System') ; 

INSERT INTO mr.account(id, companyname)
  VALUES(1, 'Company Name Here') ;



INSERT INTO public.role(id, name, description)
  VALUES(1, 'admin', 'Administrator role (can edit Users)') ; 

INSERT INTO public.role(id, name, description)
  VALUES(2, 'user', 'Default role for all Users') ;


INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('tomcat', 238, 'aa', 0, 'St@ndard99', 'Tomcat', 'User', '', 'Denver', 'CO', 'US', '80210', 'rspence@bluetrail.co.uk', '07918193864', NULL, 'TBA', true, '-', 21708, 'Tomcat User', 0, 0);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Aberdeen1 ', 0, '5130eddc', 10, 'St@ndard99', 'John', 'Inch ', '-', '-', 'North', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '1', 21708, 'John Inch ', 1, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Birmingham 1', 0, 'd4e3c60c', 20, 'St@ndard99', 'Ash', 'Averill ', '-', '-', 'Midlands', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '2', 21708, 'Ash Averill ', 2, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Birmingham 2', 0, 'd4e3c208', 30, 'St@ndard99', 'Charlie', 'Ralphs', '-', '-', 'Midlands', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '3', 21708, 'Charlie Ralphs', 3, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Durham1', 0, 'd4e3c610', 40, 'St@ndard99', 'Dan', 'Bean', '-', '-', 'North', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '4', 21708, 'Dan Bean', 4, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Edinburgh1', 0, 'd4e3c62f', 50, 'St@ndard99', 'Charley', 'Tysler', '-', '-', 'North', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '5', 21708, 'Charley Tysler', 5, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Edinburgh2', 0, '65a9cb8', 60, 'St@ndard99', 'Brooke', 'Hitching', '-', '-', 'North', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '6', 21708, 'Brooke Hitching', 6, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Leeds1', 0, 'd4e3c266', 70, 'St@ndard99', 'Trev', 'Symmons', '-', '-', 'Midlands', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '7', 21708, 'Trev Symmons', 7, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Leeds2', 0, 'd4e3c1eb', 80, 'St@ndard99', 'Robbie', 'Gree', '-', '-', 'Midlands', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '8', 21708, 'Robbie Gree', 8, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Liverpool 1', 0, 'b3ba914d', 90, 'St@ndard99', 'Andy', 'Brown', '-', '-', 'Midlands', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '9', 21708, 'Andy Brown', 9, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Loughborough1', 0, 'd4e3c629', 100, 'St@ndard99', 'Sam', 'Bloc ', '-', '-', 'Midlands', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '10', 21708, 'Sam Bloc ', 10, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Loughborough2', 0, 'd4e3c60e', 110, 'St@ndard99', 'Jon', 'Ritterband', '-', '-', 'Midlands', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '11', 21708, 'Jon Ritterband', 11, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Manchester1', 0, 'ad7e8f0c', 120, 'St@ndard99', 'Charlie', 'Tams', '-', '-', 'Midlands', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '12', 21708, 'Charlie Tams', 12, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Newcastle1', 0, 'd4e3c20d', 130, 'St@ndard99', 'Angus', 'Ropner', '-', '-', 'North', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '13', 21708, 'Angus Ropner', 13, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Newcastle2', 0, 'd4e3c2c5', 140, 'St@ndard99', 'Kiran', 'Radhakrishnan', '-', '-', 'North', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '14', 21708, 'Kiran Radhakrishnan', 14, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Nottingham1', 0, 'b3ba910c', 150, 'St@ndard99', 'Stewart', 'Bailey', '-', '-', 'Midlands', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '15', 21708, 'Stewart Bailey', 15, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Nottingham2', 0, 'b3ba910b', 160, 'St@ndard99', 'Charlie', 'Vance', '-', '-', 'Midlands', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '16', 21708, 'Charlie Vance', 16, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Oxford1', 0, 'b3ba910n', 170, 'St@ndard99', 'Andre', 'De Haes', '-', '-', 'Midlands', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '17', 21708, 'Andre De Haes', 17, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Oxford2', 0, '9ecf7347', 180, 'St@ndard99', 'Tom', 'Howard', '-', '-', 'Midlands', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '18', 21708, 'Tom Howard', 18, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Sheffield1', 0, 'c794ffb2', 190, 'St@ndard99', 'Saj', 'Huq', '-', '-', 'Midlands', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '19', 21708, 'Saj Huq', 19, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Sheffield2', 0, '81d90bd', 200, 'St@ndard99', 'Chris', 'Antsey', '-', '-', 'Midlands', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '20', 21708, 'Chris Antsey', 20, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('StAndrews1', 0, 'd4e3c62c', 210, 'St@ndard99', 'Hugh', 'Walters', '-', '-', 'North', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '21', 21708, 'Hugh Walters', 21, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Warwick1', 0, '1c122640', 220, 'St@ndard99', 'Rory', 'Horgan', '-', '-', 'Midlands', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '22', 21708, 'Rory Horgan', 22, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Aberystwyth1', 0, '5130ee97', 230, 'St@ndard99', 'Chris', 'Astle', '-', '-', 'South', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '23', 21708, 'Chris Astle', 23, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Bath 1', 0, 'b3ba910x', 240, 'St@ndard99', 'Rob', 'Halsall', '-', '-', 'South', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '24', 21708, 'Rob Halsall', 24, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Bath 2', 0, 'b3ba912b', 250, 'St@ndard99', 'Conrad', 'Oakley', '-', '-', 'South', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '25', 21708, 'Conrad Oakley', 25, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Bouremouth 1', 0, 'b3ba8ccc', 260, 'St@ndard99', 'Jeremy', 'Pack', '-', '-', 'South', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '26', 21708, 'Jeremy Pack', 26, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Bristol1', 0, 'd4e3c64b', 270, 'St@ndard99', 'Si', 'Bleasdale', '-', '-', 'South', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '27', 21708, 'Si Bleasdale', 27, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Bristol2', 0, 'b3ba8ccb', 280, 'St@ndard99', 'Leo', 'Hirson', '-', '-', 'South', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '28', 21708, 'Leo Hirson', 28, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Bristol3', 0, 'b3ba8ccf', 290, 'St@ndard99', 'Andy', 'Barber', '-', '-', 'South', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '29', 21708, 'Andy Barber', 29, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Cardiff1', 0, 'b3ba8ccg', 300, 'St@ndard99', 'Ben', 'Kendall', '-', '-', 'South', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '30', 21708, 'Ben Kendall', 30, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Exeter1', 0, '78ea4a8b', 310, 'St@ndard99', 'Nick', 'Bennett', '-', '-', 'South', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '31', 21708, 'Nick Bennett', 31, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Exeter2', 0, '78ea4a8c', 320, 'St@ndard99', 'Hugo', 'Denee', '-', '-', 'South', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '32', 21708, 'Hugo Denee', 32, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Portsmouth 1', 0, '78ea4a8d', 330, 'St@ndard99', 'Nick', 'Harvey', '-', '-', 'South', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '33', 21708, 'Nick Harvey', 33, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Reading1', 0, 'e40c04d9', 340, 'St@ndard99', 'Andy', 'May', '-', '-', 'South', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '34', 21708, 'Andy May', 34, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('Southampton 1', 0, 'e40c04da', 350, 'St@ndard99', 'Kris', 'Boger', '-', '-', 'South', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '35', 21708, 'Kris Boger', 35, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('London1', 0, '104bc59a', 360, 'St@ndard99', 'George', 'Adigbli', '-', '-', 'London ', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '36', 21708, 'George Adigbli', 36, 1);
INSERT INTO app_user (username, version, pinnumber, userid, "password", first_name, last_name, address, city, province, country, postal_code, email, phone_number, website, password_hint, enabled, employeeref, "checkpoint", oldfullname, id, account_id) VALUES ('London2', 0, '104bc59b', 370, 'St@ndard99', 'Terea', 'Gebski ', '-', '-', 'London ', '-', '-', 'sav@uk.redbull.com.', 'TBA', '-', 'TBA', true, '37', 21708, 'Terea Gebski ', 37, 1);


insert into user_role select id, 2 from app_user;

insert into user_role values(0,1);

ALTER TABLE app_user DROP COLUMN enabled  ;


ALTER TABLE app_user ADD COLUMN account_expired char(1);
ALTER TABLE app_user ALTER COLUMN account_expired SET STORAGE PLAIN;
ALTER TABLE app_user ADD COLUMN account_locked char(1);
ALTER TABLE app_user ALTER COLUMN account_locked SET STORAGE PLAIN;
ALTER TABLE app_user ADD COLUMN credentials_expired char(1);
ALTER TABLE app_user ALTER COLUMN credentials_expired SET STORAGE PLAIN;
ALTER TABLE app_user ADD COLUMN enabled char(1);
ALTER TABLE app_user ALTER COLUMN enabled SET STORAGE PLAIN;



update app_user set account_expired = 'N' ;
update app_user set account_locked = 'N' ;
update app_user set credentials_expired = 'N' ;
update app_user set enabled = 'Y' ;


drop table mr.app_user_backup;


CREATE SEQUENCE hibernate_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 5
  CACHE 1;
ALTER TABLE hibernate_sequence OWNER TO mobriz;


ALTER TABLE mr.surveys ADD COLUMN account_id int8;
ALTER TABLE mr.surveys ALTER COLUMN account_id SET STORAGE PLAIN;

update mr.surveys set account_id = 1;

ALTER TABLE app_user ALTER COLUMN account_id SET NOT NULL;
ALTER TABLE mr.surveys ALTER COLUMN account_id SET NOT NULL;


ALTER TABLE mr.surveys
  ADD CONSTRAINT accountfk FOREIGN KEY (account_id) REFERENCES mr.account (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE mr.survey_user ADD COLUMN user_id int8;
ALTER TABLE mr.survey_user ALTER COLUMN user_id SET STORAGE PLAIN;

update mr.survey_user set user_id = (select id from app_user a, mr.survey_user s where a.username = s.username);


ALTER TABLE mr.survey_user ALTER COLUMN user_id SET NOT NULL;

ALTER TABLE mr.survey_user DROP CONSTRAINT user_role_pkey ; 

ALTER TABLE mr.survey_user  ADD CONSTRAINT fk143bf46a6934ae9e FOREIGN KEY (user_id) REFERENCES app_user (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE mr.survey_user   ADD CONSTRAINT user_role_pkey PRIMARY KEY(user_id, survey_id);


ALTER TABLE mr.surveys DROP COLUMN surveystatus ; 

ALTER TABLE mr.surveys ADD COLUMN surveystatus char(1);
ALTER TABLE mr.surveys ALTER COLUMN surveystatus SET STORAGE EXTENDED;

update mr.surveys set surveystatus = 'L' ;

update mr.surveys set surveystatus = 'X' where deleted = true ;

ALTER TABLE mr.surveys DROP COLUMN deleted ; 
