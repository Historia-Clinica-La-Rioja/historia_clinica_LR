#!/bin/sh
set -o errexit

BASEDIR=$(dirname "$0")
cd "$BASEDIR"

start_apps=$(date +%s)
echo "Building Webapp: start"
cd apps
[ -d "node_modules" ] || npm install --legacy-peer-deps
npm run build
cd -

start_backoffice=$(date +%s)

echo "Building Backoffice: start"
cd backoffice
[ -d "node_modules" ] || npm install --legacy-peer-deps
npm run build
cd -

end_all=$(date +%s)

echo "Elapsed Time for app: $(($start_backoffice-$start_apps)) seconds"
echo "Elapsed Time for backoffice: $(($end_all-$start_backoffice)) seconds"


echo "el built-commit es $(git rev-parse HEAD)" > apps/dist/hospital/git-sha

echo "Building Frontend: output"
ls -lha apps/dist/hospital
ls -lha backoffice/build
