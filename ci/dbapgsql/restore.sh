#!/bin/bash
set -o errexit
export DB_SERVER=${1:-}
export DB_USER=${2:-}
export DUMP_FILENAME=${3:-}
export TARGET_DB=${4:-}
export NEW_OWNER=${5:-} # optional
# RESTORE (IMPORT) DATABASE
if [[ -z $PGPASSWORD || -z $DUMP_FILENAME || -z $DB_SERVER || 
    -z $DB_USER || -z $TARGET_DB || -z $NEW_OWNER ]]; then
  echo 'SQL script: one or more parameters are undefined'
  exit 1
fi
echo "Preparing database..."
#-- Database Restore
psql -h $DB_SERVER -U $DB_USER -qAt -c "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = '$TARGET_DB' AND pid <> pg_backend_pid();"
psql -h $DB_SERVER -U $DB_USER -qAt -c "drop database if exists $TARGET_DB;"
psql -h $DB_SERVER -U $DB_USER -qAt -c "create database $TARGET_DB;"
psql -h $DB_SERVER -U $DB_USER -qAt -c "ALTER DATABASE $TARGET_DB OWNER TO \"$NEW_OWNER\";"
psql -h $DB_SERVER -U $DB_USER -qAt -c "ALTER SCHEMA public OWNER TO \"$NEW_OWNER\";" $TARGET_DB

echo "Restoring database..."
pg_restore --clean --if-exists --no-owner --no-privileges \
            -h $DB_SERVER -d $TARGET_DB -U $DB_USER \
            $DUMP_FILENAME && \
  echo "Database restored" || \
  echo "Something failed"

echo "Resetting ownership..."
( \
for tbl in `psql -h $DB_SERVER -U $DB_USER -qAt -c "select tablename from pg_tables where schemaname = 'public';" $TARGET_DB` ; do  echo "alter table \"$tbl\" owner to \"$NEW_OWNER\";"; done; \
for tbl in `psql -h $DB_SERVER -U $DB_USER -qAt -c "select sequence_name from information_schema.sequences where sequence_schema = 'public';" $TARGET_DB` ; do  echo "alter sequence \"$tbl\" owner to \"$NEW_OWNER\";" ; done; \
for tbl in `psql -h $DB_SERVER -U $DB_USER -qAt -c "select table_name from information_schema.views where table_schema = 'public';" $TARGET_DB` ; do  echo "alter view \"$tbl\" owner to \"$NEW_OWNER\";"; done; \
) | psql -h $DB_SERVER -U $DB_USER -d $TARGET_DB
echo "Done."
