#!/bin/sh
set -o errexit

BASEDIR=$(dirname "$0")
cd "$BASEDIR"

# NPM_CACHE can be set as Environment Variable
# Defaults to front-end/.npm (added to gitignore)
[ -z "$NPM_CACHE" ] && NPM_CACHE=$(pwd)/.npm

echo "NPM_CACHE set as ${NPM_CACHE}"

start_fe_tests=$(date +%s)

###### app tests
cd apps
npm run ci:test
cd -

end_tests=$(date +%s)

echo "Elapsed Time for app tests: $(($end_tests-$start_fe_tests)) seconds"
