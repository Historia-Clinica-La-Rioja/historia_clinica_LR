#!/bin/sh
set -o errexit ; set -o nounset
if [ -z $DB_TYPE ] || [ -z $DB_SERVER ] || [ -z $DB_NAME ] ; then
  echo 'SQL script: one or more parameters are undefined'
  exit 1
fi

export JDBC_URL=""
[ $DB_TYPE != "postgresql" ] || export JDBC_URL=jdbc:postgresql://${DB_SERVER}:5432/${DB_NAME}
[ $DB_TYPE != "mssql" ] || export JDBC_URL="jdbc:sqlserver://${DB_SERVER}:1433;databaseName=${DB_NAME};integratedSecurity=false;"
[ $DB_TYPE != "db2" ] || export JDBC_URL="jdbc:db2://${DB_SERVER}:50000/${DB_NAME}"

echo $JDBC_URL


