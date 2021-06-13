#!/bin/sh
set -o errexit

BASEDIR=$(dirname "$0")"/.."
cd "$BASEDIR"

cd front-end/apps

yarn install
npm run lint
npm run test -- --no-watch --no-progress --browsers=ChromeHeadlessCI
npm run build:odontology
npm run build:prod
cd -

cd front-end/backoffice
npm install
npm run build
cd -


cd back-end
#mvn -B -e dependency:go-offline -B
mvn clean package
cd -

cp back-end/app/target/app*.jar hospital.jar
