#!/bin/sh
set -o errexit ; set -o nounset

export IMAGE_NAME=$1


if [ -z $IMAGE_NAME ] ; then
  echo 'Deploy script: one or more parameters are undefined'
  exit 1
fi

# supone que ya se corrio build-pack
rm -r ci/docker/cubejs/schema || true
cp -r cubejs/schema ci/docker/cubejs/schema

cd ci/docker/cubejs
docker build -t $IMAGE_NAME .
