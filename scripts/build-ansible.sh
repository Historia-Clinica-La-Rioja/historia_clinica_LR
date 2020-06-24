#!/bin/sh
set -o errexit
PROJECTDIR=$(dirname "$0")"/.."
cd $PROJECTDIR

# supone que ya se corrio build-pack creando un war
# rm ci/docker/nginx-jar/*.jar || true

mkdir -p sgh-ansible/dist
ls back-end/hospital-api/target/*.war 

cp back-end/hospital-api/target/*.war sgh-ansible/dist/api.war 


rm -r sgh-ansible/front-end/webapp 2> /dev/null || true
cp -r front-end/webapp/dist/sgh sgh-ansible/dist/webapp

rm -r sgh-ansible/front-end/backoffice 2> /dev/null || true
cp -r front-end/backoffice/build/ sgh-ansible/dist/backoffice

