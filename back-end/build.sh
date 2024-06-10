#!/bin/sh
set -o errexit

BASEDIR=$(dirname "$0")
cd "$BASEDIR"

# MVN_CACHE can be set as Environment Variable
# Defaults to back-end/.maven (added to gitignore)
[ -z "$MVN_CACHE" ] && MVN_CACHE=$(pwd)/.maven

echo "MVN_CACHE set as ${MVN_CACHE}"

export MAVEN_OPTS="$MAVEN_OPTS -Dmaven.repo.local=${MVN_CACHE}"

echo "MAVEN_OPTS set as '${MAVEN_OPTS}'"

start_all=$(date +%s)

echo "Building Backend: start"
mvn clean package -DskipTests

end_all=$(date +%s)

echo "Elapsed Time for backend: $(($end_all-$start_all)) seconds"

mkdir -p .docker
cp app/target/app*.jar .docker/app.jar

echo "Building Backend: output"
ls -lha .docker/

./check.sh
