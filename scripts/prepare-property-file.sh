#!/bin/sh
set -o errexit ; set -o nounset

### Este script prepara el archivo de properties del sistema
# Para esto:
# 1. El archivo recibido en $1 (usar /dev/null) para vacio
# 2. Agrega properties de conexión a la BBDD si se recibieron
# 3. Archivo de properties externo en el repo de properties (se clona para obtenerlo)

export TEMPLATE_FILE=$(realpath $1)
export PROPERTIES_REPO=$2
export CI_ENVIRONMENT_NAME=$3
export DB_TYPE=${4:-""}
export JDBC_URL=${5:-""}
export DB_USER=${6:-""}
export DB_PASS=${7:-""}

BASEDIR=$(dirname "$0")"/.."
cd "${BASEDIR}"

cp $TEMPLATE_FILE env.properties

echo "
# -----------------------------------------------------------------------------------------------------------
# -----------------------------------------------------------------------------------------------------------
#                         Propiedades de base de datos
# -----------------------------------------------------------------------------------------------------------
# -----------------------------------------------------------------------------------------------------------
" >> env.properties

[ -z $JDBC_URL ] || echo "spring.datasource.url=${JDBC_URL}" >> env.properties
[ -z $DB_USER  ] || echo "spring.datasource.username=${DB_USER}" >> env.properties
[ -z $DB_PASS  ] || echo "spring.datasource.password=${DB_PASS}" >> env.properties

[ "$DB_TYPE" != "mssql" ] || echo "spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver" >> env.properties
[ "$DB_TYPE" != "mssql" ] || echo "spring.jpa.database-platform=org.hibernate.dialect.SQLServer2012Dialect" >> env.properties

if [ -z $PROPERTIES_REPO ]; then
  echo "No se uso repo de properties";
  exit 0;
fi

echo "
# -----------------------------------------------------------------------------------------------------------
# -----------------------------------------------------------------------------------------------------------
#                         Propiedades del archivo \"$CI_ENVIRONMENT_NAME.properties\"
# -----------------------------------------------------------------------------------------------------------
# -----------------------------------------------------------------------------------------------------------
" >> env.properties
echo "Se clona repositorio de properties"
git clone -q $PROPERTIES_REPO properties
echo "Se buscan propiedades en el archivo \"$CI_ENVIRONMENT_NAME.properties\""
cat "properties/$CI_ENVIRONMENT_NAME.properties" 2> /dev/null >> env.properties && 
  echo "--> Se agregaron las propiedades de ese archivo." ||
  echo "--> No hay propiedades adicionales"