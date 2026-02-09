# Recreate DB and restore dump into docker postgres (handles old WITH OIDS dumps)
docker exec -i miro-postgres psql -U mobriz -d postgres -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'mirotestdb' AND pid <> pg_backend_pid();" -c "DROP DATABASE IF EXISTS mirotestdb;" -c "CREATE DATABASE mirotestdb OWNER mobriz;" && sed '/^SET default_with_oids = true;$/d' "/Users/richardspence/Downloads/mirotestdb.sql" | docker exec -i miro-postgres psql -v ON_ERROR_STOP=1 -U mobriz -d mirotestdb


#quick verify
docker exec -i miro-postgres psql -U mobriz -d mirotestdb -c "SELECT schemaname, COUNT(*) FROM pg_tables WHERE schemaname IN ('mr','public') GROUP BY schemaname ORDER BY schemaname;"

