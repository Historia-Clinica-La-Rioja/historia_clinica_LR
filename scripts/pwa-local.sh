#!/bin/sh
#
# Este script no es parte del CI
# Documentación en front-end/README.md
#

set -o errexit
BASEDIR=$(dirname "$0")"/.."

WEBAPP_PROJECT_FOLDER=${BASEDIR}"/front-end/apps"
WEBAPP_DIST_FOLDER="dist/hospital"

cd ${WEBAPP_PROJECT_FOLDER}

# Build de webapp si no está buildeado

if [ ! -f "${WEBAPP_DIST_FOLDER}/index.html" ]; then
    echo "Falta buildear webapp"
    npm run build:prod
fi

# Build de nginx
docker build -t pwa ../nginx-docker
# Inicia nginx
docker run -d --rm -p 8001:8280 -v /${PWD}/${WEBAPP_DIST_FOLDER}:/app/front-end --name web pwa
# Inicia ngrok
docker run --rm -it --link web wernight/ngrok ngrok http web:8280
