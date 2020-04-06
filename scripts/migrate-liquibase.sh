#!/bin/sh
set -o errexit ; set -o nounset
BASEDIR=$(dirname "$0")"/.."
cd "$BASEDIR"

export DB_TYPE=$1
export JDBC_URL=$2
export MIGRATION_USER=$3
export MIGRATION_PASSWORD=$4
# optionals with default value
export CONTEXTS=${5:-"default"}
CI_JOB_ID=${CI_JOB_ID:-""}

if   [ -z $JDBC_URL ] || 
     [ -z $MIGRATION_USER ] || [ -z $MIGRATION_PASSWORD ] ; then
  echo 'SQL script: one or more parameters are undefined'
  exit 1
fi

# Copio en otra carpeta para preparar un contexto 
# reducido en tamaño para pasar a Docker
mkdir -p dba/target/dba/liquibase
cp -Rf dba/liquibase/* dba/target/dba/liquibase/
cp dba/pom.xml dba/target/dba/liquibase
cp pom-parent.xml dba/target/dba/

docker build dba/target/dba -f $(pwd)/ci/docker/liquibase/Dockerfile -t liquibase_migration:job${CI_JOB_ID}
docker run --rm -v mvn-liquibase:"/root/.m2/repository" \
     liquibase_migration:job${CI_JOB_ID}  \
     mvn  -B -DliquibasePropertyFile=$DB_TYPE.properties \
          -Dliquibase.url=$JDBC_URL -Dliquibase.username=$MIGRATION_USER \
          -Dliquibase.password=$MIGRATION_PASSWORD \
          -Dliquibase.changeLogFile=main.yml \
          -Dliquibase.contexts="$CONTEXTS" \
          liquibase:update && 
     rc=$? || rc=$? #save return code

docker rmi liquibase_migration:job${CI_JOB_ID} || true
exit $rc
