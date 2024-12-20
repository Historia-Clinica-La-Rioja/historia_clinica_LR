#!/bin/sh
#
# Este script no es parte del CI
#
# Para ejecutarlo en windows (en la raiz del proyecto) :
# winpty bash ./scripts/restore-backup.sh <path .bkp>
#

set -o errexit
BASEDIR=$(dirname "$0")"/.."

DOCKER_COMPOSE=${BASEDIR}"/docker-compose.yml"
BACKUP_DIR=${BASEDIR}"/.docker-data/backups"
POSTGRES_DATA=${BASEDIR}"/.docker-data/postgres/"
TARGET_CONFIG=${BACKUP_DIR}"/.target-db"
DATABASE_CONTAINER="hsi-db"

mkdir -p $BACKUP_DIR

if [ ! -f $TARGET_CONFIG ]; then
    echo "==> Target DB CONFIG was not found, it will be created"
    echo "TARGET_HOST=host.docker.internal" > $TARGET_CONFIG
    echo "TARGET_USER=postgres" >> $TARGET_CONFIG
    echo "TARGET_DATABASE=hospitalDB" >> $TARGET_CONFIG
    echo "TARGET_PASSWORD=Local123" >> $TARGET_CONFIG
fi

DUMP_FILE=${1?"Usage: $0 [path to dump file]"}

if [ ! -f $DUMP_FILE ]; then
    echo "Dump file was not found"
    exit 1
fi

if [ ! -f $DOCKER_COMPOSE ]; then
    echo "docker-compose.yml not found"
    exit 1
fi

# Verificar si 'docker compose' está disponible
if docker compose version >/dev/null 2>&1; then
  DOCKER_COMPOSE_CMD="docker compose"
# Si no está disponible, verificar si 'docker-compose' está disponible
elif docker-compose version >/dev/null 2>&1; then
  DOCKER_COMPOSE_CMD="docker-compose"
else
  echo "Neither 'docker compose' nor 'docker-compose' is available. Please install Docker."
  exit 1
fi

echo "==> Stopping ${DATABASE_CONTAINER}"
$DOCKER_COMPOSE_CMD stop ${DATABASE_CONTAINER}

echo "==> Deleting ${DATABASE_CONTAINER}"
docker rm ${DATABASE_CONTAINER} || true

echo "==> Deleting postgres data"
rm -rf ${POSTGRES_DATA}

echo "==> Starting empty ${DATABASE_CONTAINER}"
$DOCKER_COMPOSE_CMD up -d ${DATABASE_CONTAINER}

echo "==> Waiting 10 seconds"
sleep 10

echo "==> Restoring $DUMP_FILE into ${DATABASE_CONTAINER}"
docker run -itv ${DUMP_FILE}:/app/pg-task/backups/dump.bkp --env-file ${TARGET_CONFIG} registry.lamansys.ar/lamansys/community/pg-task16:latest

