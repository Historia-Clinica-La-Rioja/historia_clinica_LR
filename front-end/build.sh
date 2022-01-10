#!/bin/sh
set -o errexit

BASEDIR=$(dirname "$0")
cd "$BASEDIR"

# NPM_CACHE can be set as Environment Variable
# Defaults to front-end/.npm (added to gitignore)
[ -z "$NPM_CACHE" ] && NPM_CACHE=$(pwd)/.npm

echo "NPM_CACHE set as ${NPM_CACHE}"
node -v
npm -v

start_apps=$(date +%s)

echo "Building Webapp: start"
cd apps
npm ci --cache ${NPM_CACHE}
npm run build:odontology
npm run build
echo "Building Webapp: output"
ls -lha dist/hospital
cd -

start_backoffice=$(date +%s)

echo "Building Backoffice: start"
cd backoffice
npm ci --cache ${NPM_CACHE}
npm run build
echo "Building Backoffice: output"
ls -lha build
cd -

end_all=$(date +%s)

echo "Elapsed Time for app: $(($start_backoffice-$start_apps)) seconds"
echo "Elapsed Time for backoffice: $(($end_all-$start_backoffice)) seconds"
