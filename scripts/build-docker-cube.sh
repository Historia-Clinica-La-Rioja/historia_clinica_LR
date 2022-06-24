#!/bin/sh
set -o errexit ; set -o nounset

export IMAGE_NAME=$1


if [ -z $IMAGE_NAME ] ; then
  echo 'Deploy script: one or more parameters are undefined'
  exit 1
fi


cd cubejs
docker build -t $IMAGE_NAME .
