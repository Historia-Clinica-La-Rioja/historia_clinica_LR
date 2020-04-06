#!/bin/bash
set -o errexit ; set -o nounset
export DB_SERVER=${1:-}
export DB_USER=${2:-}
export TARGET_DB=${3:-}
export SQL_FILENAME=${4:-}

# RESTORE (IMPORT) DATABASE
if [[ -z $PGPASSWORD || -z $SQL_FILENAME || -z $DB_SERVER || -z $DB_USER || -z $TARGET_DB ]]; then
  echo 'SQL script: one or more parameters are undefined'
  exit 1
fi

#-- Database Backup
# format: compressed-custom
pg_dump --format=c -h $DB_SERVER -d $TARGET_DB -U $DB_USER > $SQL_FILENAME