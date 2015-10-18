ALTER TABLE mr.miroprojects ADD COLUMN bcc_practitioner character(1);
update mr.miroprojects set bcc_practitioner = 'N'