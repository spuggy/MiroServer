-- drop the existing database
drop database mobriz4db;

-- create the test user
create user test password 'test';

-- create the database
create database mobriz4db owner test;
