#!/bin/sh
set -o errexit ; set -o nounset

export ENV_NAME=$1
export DB_TYPE=$2
export DB_SERVER=$3
export DB_PORT=$4
export DB_NAME=$5
export DB_USER=$6
export DB_PASS=$7
export DOCKER_IMAGE=$8



if [ -z $ENV_NAME ] || [ -z $DB_TYPE ] || 
   [ -z $DB_SERVER ] || [ -z $DB_PORT ] || 
   [ -z $DB_NAME ] || [ -z $DB_USER ] || 
   [ -z $DB_PASS ] || [ -z $DOCKER_IMAGE ] ; then
  echo 'Deploy script: one or more parameters are undefined'
  exit 1
fi

echo "Imagen a utilizar --> $DOCKER_IMAGE"

#echo "Parametros: "
#echo "ENV_NAME --> $ENV_NAME"
#echo "DB_TYPE --> $DB_TYPE"
#echo "DB_SERVER --> $DB_SERVER"
#echo "DB_PORT --> $DB_PORT"
#echo "DB_NAME --> $DB_NAME"
#echo "DB_USER --> $DB_USER"
#echo "DB_PASS --> $DB_PASS"

docker pull $DOCKER_IMAGE

echo Borra el ambiente anterior si no fue eliminado en ejecuciones anteriores
docker rm -f "cubejs-${ENV_NAME}_old" 2> /dev/null || true

echo "Renombra environment anterior"
docker rename "cubejs-${ENV_NAME}" "cubejs-${ENV_NAME}_old" 2> /dev/null || true
docker stop "cubejs-${ENV_NAME}"_old 2> /dev/null || true

echo Crea environment con hash: $(
   docker create -P \
    --name cubejs-$ENV_NAME \
    -e CUBEJS_DB_TYPE=$DB_TYPE \
    -e CUBEJS_DB_HOST=$DB_SERVER \
    -e CUBEJS_DB_PORT=$DB_PORT \
    -e CUBEJS_DB_NAME=$DB_NAME \
    -e CUBEJS_DB_USER=$DB_USER \
    -e CUBEJS_DB_PASS=$DB_PASS \
    -e CUBEJS_WEB_SOCKETS=true \
    -e CUBEJS_DEV_MODE=true \
    --network=web \
    $DOCKER_IMAGE
)

echo Inicia environment con name: $(
    docker start "cubejs-${ENV_NAME}"
)
# Stop previous environment (if exists) 
echo Destruye ambiente anterior con name: $(
    docker stop "cubejs-${ENV_NAME}"_old >/dev/null 2>&1 || true;
    docker rm -f "cubejs-${ENV_NAME}"_old 2> /dev/null || true
)

