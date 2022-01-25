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

start_be_tests=$(date +%s)
mvn test

end_tests=$(date +%s)

echo "Elapsed Time for backend tests: $(($end_tests-$start_be_tests)) seconds"
