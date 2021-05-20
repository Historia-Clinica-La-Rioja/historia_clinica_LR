#!/bin/sh
set -o errexit
PROJECTDIR=$(dirname "$0")"/.."
cd $PROJECTDIR

./scripts/build-pack.sh
mvn -f back-end/pom.xml -Pwar package -DskipTests

mkdir -p sgh-ansible/dist
ls back-end/app/target/*.war

cp back-end/app/target/*.war sgh-ansible/dist/api.war


rm -r sgh-ansible/dist/webapp 2> /dev/null || true
cp -r front-end/apps/dist/hospital sgh-ansible/dist/webapp

rm -r sgh-ansible/dist/backoffice 2> /dev/null || true
cp -r front-end/backoffice/build/ sgh-ansible/dist/backoffice

cd sgh-ansible/dist
tar -czf front-end.tar.gz webapp backoffice
cd -
