#!/bin/sh
set -o errexit
BASEDIR=$(dirname "$0")"/.."

# supone que ya se corrio build-pack
rm ci/docker/nginx-jar/*.jar || true
cp back-end/app/target/app*.jar ci/docker/nginx-jar/hospital.jar

rm -r ci/docker/nginx-jar/front-end-dist 2> /dev/null || true
cp -r front-end/apps/dist/ ci/docker/nginx-jar/front-end-dist

rm -r ci/docker/nginx-jar/backoffice-dist 2> /dev/null || true
cp -r front-end/backoffice/build/ ci/docker/nginx-jar/backoffice-dist

cd ci/docker/nginx-jar/
docker build . "$@"