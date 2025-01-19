#!/bin/sh
set -o errexit

BASEDIR=$(dirname "$0")
cd "$BASEDIR"

echo "Node.js version:"
node -v
echo "Npm version:"
npm -v

start_apps=$(date +%s)
echo "Building Storybook: start"
cd apps
npm run build-storybook
cd -

[ -d ".docker" ] && mv .docker .docker_"$(date +%F'-'%T)"
mkdir .docker
mv apps/storybook-static .docker/storybook

end_all=$(date +%s)

echo "Elapsed Time for storybook: $(($end_all-$start_apps)) seconds"

echo "Building Frontend: output"
ls -lha .docker/*