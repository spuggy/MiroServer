ALTER TABLE mr.webpages ADD COLUMN publisheddate timestamp without time zone;
update mr.webpages set publisheddate = '2016-01-01';
ALTER TABLE mr.webpages ALTER COLUMN publisheddate SET NOT NULL;
ALTER TABLE mr.webpages ADD COLUMN pagetype integer;
update mr.webpages set pagetype = 0;
ALTER TABLE mr.webpages ALTER COLUMN pagetype SET NOT NULL;
ALTER TABLE mr.webpages ADD COLUMN pagestatus integer;
update mr.webpages set pagestatus = 1;
ALTER TABLE mr.webpages ALTER COLUMN pagestatus SET NOT NULL;