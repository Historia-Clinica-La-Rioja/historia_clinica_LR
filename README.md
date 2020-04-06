# HospitalApp

El proyecto está formado por un front-end en Angular que utiliza la API REST expuesta por un back-end en Java/Spring Boot que almacena la información en una base de datos SQLServer. 

## Ambiente de desarrollo

Para levantar el ambiente de desarrollo se requiere tener una **base de datos** SQLServer que se puede crear siguiendo los pasos de [mas abajo](#bbdd).

El **backend** se puede levantar para desarrollo o solamente para dar soporte al desarrollo del frontend siguiendo los pasos del [back-end/README.md](back-end/README.md).

El frontend está formado por dos proyecto, una webapp y el backoffice. En cada caso puede levantar para desarrollo o solamente para dar soporte al desarrollo del backend siguiendo los pasos del [front-end/README.md](front-end/README.md).


## Ambiente con docker-compose

### BBDD

Para levantar y migrar la BBDD. Levanta Postgresql con usuario "postgres" y password "Local123". La monta localmente en el puerto localhost:1433

```shell
docker-compose up -d
export DB_TYPE=postgresql
export IP=$(ip addr show | sed -En 's/127.0.0.1//;s/.*inet (addr:)?(([0-9]*\.){3}[0-9]*).*/\2/p' | grep 192.168)
export DB_SERVER=${IP}
export DB_NAME=hospitalDB
export JDBC_URL=$(./scripts/make-jdbc.sh)
export MIGRATION_USER=postgres
export MIGRATION_PASSWORD=Local123
./scripts/migrate-liquibase.sh $DB_TYPE $JDBC_URL $MIGRATION_USER $MIGRATION_PASSWORD
```

##### En windows

Para exportar la variable IP utilizar el comando 'ipconfig' en consola para obtener la ip local y agregarla manualmente a la instrucción, por ej.
```shell
export IP=$192.168.1.3
```


En algunos casos en windows es necesario cambiar la forma de ejecucion de los archivos sh:
```shell
sh ./scripts/migrate-liquibase.sh $DB_TYPE $JDBC_URL $MIGRATION_USER $MIGRATION_PASSWORD
```

#### Migrate

Para unicamente migrar, ir a la carpeta /dba y ejecutar el siguiente comando
```shell
mvn -Dliquibase.propertyFile=liquibase/postgresql.properties liquibase:update
```