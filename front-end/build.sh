#!/bin/sh
set -o errexit

BASEDIR=$(dirname "$0")
cd "$BASEDIR"

echo "Node.js version:"
node -v
echo "Npm version:"
npm -v

start_apps=$(date +%s)
echo "Building Webapp: start"
cd apps
[ -d "node_modules" ] || npm ci
npm run build:prod
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

[ -d ".docker" ] && mv .docker .docker_$(date +%F'-'%T)
mkdir .docker

mv backoffice/build .docker/backoffice
mv apps/dist/hospital .docker/front-end

echo "el built-commit es $(git rev-parse HEAD)" > .docker/front-end/git-sha

echo "Building Frontend: output"
ls -lha .docker/*