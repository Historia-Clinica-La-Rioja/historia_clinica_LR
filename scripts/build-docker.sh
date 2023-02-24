#!/bin/sh
set -o errexit
BASEDIR=$(dirname "$0")"/.."

DOCKER_FOLDER="ci/docker/nginx-jar"

mv -v back-end/.docker/* ${DOCKER_FOLDER}/
mv -v front-end/.docker/* ${DOCKER_FOLDER}/

cd ci/docker/nginx-jar/
docker build . "$@"