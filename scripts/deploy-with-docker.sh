#!/bin/sh
set -o errexit ; set -o nounset

### Este script despliega un ambiente docker con las especificaciones necesarias en los ambientes docker
### Configura: 
###    Traefik y su red, Glowroot, Nombre de ambiente segun convencion de ReviewApps, 
###    marca el ambiente como temporal o no, volumen de mocks cache, restart automatico, properties externalizadas
export ENV_NAME=$1
export DOCKER_IMAGE=$2
export REVIEW_APP_URL=$3
# optional parameter with default value
export TEMPORAL=${4:-true}
export USE_LOCAL_BUILD=${5:-false}
GLOWROOT_PROJECT="sgh"

if [ -z $ENV_NAME ] || [ -z $DOCKER_IMAGE ] || 
   [ -z $REVIEW_APP_URL ] || [ -z $TEMPORAL ] ; then
  echo 'Deploy script: one or more parameters are undefined'
  exit 1
fi

# Download new image
if [ "$USE_LOCAL_BUILD" = "false" ]; then
  echo "Bajando imagen docker"
  docker pull ${DOCKER_IMAGE} || 
    (echo "ERROR: La imagen de docker no existe, corra el \"build docker\" de nuevo y reintente" && exit 1)
fi

# Kill old name convention environment
docker rm -f reviewapp-${ENV_NAME} 2> /dev/null || true
# Rename previous environment (if exists), avoid deadlocks killing multiple olds environments 
docker rm -f ${ENV_NAME}_old 2> /dev/null || true
echo "Renombra environment anterior"
docker rename ${ENV_NAME} ${ENV_NAME}_old 2> /dev/null || true
# Start environment
echo "Se usara la imagen docker ${DOCKER_IMAGE}"
echo Crea environment con hash: $(
    docker create -P  \
        -l traefik.frontend.rule=Host:${REVIEW_APP_URL} \
        -l traefik.port=8280 \
        -l temporal=${TEMPORAL} \
        --network=web \
        --restart=unless-stopped \
        -e GLOWROOT_PROJECT=${GLOWROOT_PROJECT} \
        -e GLOWROOT_AGENT_ID="${ENV_NAME}" \
        -e APP_DOMAIN=${REVIEW_APP_URL} \
        -e AUTH_DOMAIN=${REVIEW_APP_URL} \
        -e AUTH_SECURE=false \
        --name ${ENV_NAME} \
        -v /sgh/${ENV_NAME}:/sgh  \
        --log-opt max-size=10m \
        ${DOCKER_IMAGE} #docker image to run
)
docker cp /tmp/${ENV_NAME}.properties ${ENV_NAME}:/app/env.properties 
echo Inicia environment con name: $(
    docker start ${ENV_NAME}
)
# Stop previous environment (if exists) 
echo Destruye ambiente anterior con name: $(
    docker stop ${ENV_NAME}_old >/dev/null 2>&1 || true;
    docker rm -f ${ENV_NAME}_old 2> /dev/null || true
)