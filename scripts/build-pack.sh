#!/bin/sh
set -o errexit

BASEDIR=$(dirname "$0")"/.."
cd "$BASEDIR"

NPM_CACHE=$(pwd)/.npm

start_apps=$(date +%s)

cd front-end/apps
npm ci --cache ${NPM_CACHE}
npm run build:odontology
npm run build:prod
cd -

start_backoffice=$(date +%s)

cd front-end/backoffice
npm ci --cache ${NPM_CACHE}
npm run build
cd -

start_backend=$(date +%s)

cd back-end
mvn clean package -DskipTests
cd -

end_all=$(date +%s)

echo "Elapsed Time for app: $(($start_backoffice-$start_apps)) seconds"
echo "Elapsed Time for backoffice: $(($start_backend-$start_backoffice)) seconds"
echo "Elapsed Time for back-end: $(($end_all-$start_backend)) seconds"

cp back-end/app/target/app*.jar hospital.jar

echo "Running tests"

###### app tests
start_fe_tests=$(date +%s)
cd front-end/apps
npm run ci:test
cd -

###### backend tests
start_be_tests=$(date +%s)
cd back-end
mvn test
cd -

end_tests=$(date +%s)
echo "Elapsed Time for app tests: $(($start_be_tests-$start_fe_tests)) seconds"
echo "Elapsed Time for backend tests: $(($end_tests-$start_be_tests)) seconds"

