#!/bin/sh
set -o errexit

BASEDIR=$(dirname "$0")"/.."
cd "$BASEDIR"

./front-end/build.sh
./back-end/build.sh

echo "Running tests"

./front-end/run-tests.sh
./back-end/run-tests.sh
