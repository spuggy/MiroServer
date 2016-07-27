#!/usr/bin/env bash
gunzip mirotestdb.sql.gz
psql -d mirotestdb -a -f dropschemas.sql
psql -d mirotestdb -a -f mirotestdb.sql
