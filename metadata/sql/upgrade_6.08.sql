drop view vw_lastmonth_detail;
drop view vw_lastmonth_totals ;
drop view vw_q1_totals ;
drop view vw_q2_totals ;
drop view vw_q3_totals ;
drop view vw_q4_totals ;
drop view vw_totals_detail ;
drop view vwtotals;
drop view vwpurchased;
drop view vwpracs;
drop view vwprojects;



create view vwpracs as
SELECT u.id, u.username, u.version, u.pinnumber, u.userid, u.password, u.first_name, u.last_name, u.department,u.address, u.city, u.province, u.country, u.postal_code, u.email, u.phone_number, u.website, u.password_hint, u.employeeref, u.checkpoint, u.oldfullname, u.account_id, u.account_expired, u.account_locked, u.credentials_expired, u.enabled, u.deleted, u.project_id, u.response_id, u.creditbalance, u.status, u.webaddress, u.company, u.postcode, u.county, u.address1, u.address2, u.created_on, u.oldreport, u.updated_at, u.lastupdatedby_id, u.createdby_id
  FROM app_user u, user_role r
 WHERE r.user_id = u.id AND r.role_id = 1;


create view vwprojects as 
SELECT p.id, p.projecttitle, p.projectdescription, p.emailinvitetext, p.projectstatus, p.checkpoint, p.created_on, p.updated_at, p.version, p.createdby_id, p.lastupdatedby_id, p.emailinvitesubject, u.id AS prid, u.first_name AS prac_first_name, u.last_name AS prac_last_name
  FROM mr.miroprojects p, app_user u
 WHERE p.createdby_id = u.id;


create view  vwpurchased as 
 SELECT p.prid, p.projecttitle, u.updated_at, u.first_name, u.id, u.last_name, 1 AS num
   FROM vwprojects p, app_user u
  WHERE u.project_id = p.id AND u.status = 40 AND u.updated_at > '2008-11-01 00:00:00'::timestamp without time zone;



create view vwtotals as
 SELECT u.id AS pr_id, u.first_name AS pr_fn, u.last_name AS pr_ln, u.department, p.prid, p.projecttitle, p.updated_at, p.first_name, p.id, p.last_name, p.num
   FROM vwpracs u
   LEFT JOIN vwpurchased p ON p.prid = u.id
  ORDER BY u.id;





