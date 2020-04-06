# Base de datos

El DBMS utilizado actualmente es **Postgres**.

## Ambiente de desarrollo

En Pladema hay servidores de desarrollo disponibles para el DBMS soportado. A traves del CI se pueden crear bases personales si el proyecto lo permite, y copiar la utilizada de algun ambiente.

Para utilizar una base de datos local, se sugiere:
1. instalar docker
2. crear el contenedor con docker-compose
3. copiar una base de datos desde Pladema (opcional) descargandola desde el CI en gitlab
4. luego ejecutar liquibase
5. cambiar configuracion del proyecto para usar la base local

### Paso 2: Docker

En la raiz del proyecto, abrir shell y ejecutar:

```shell
docker-compose up -d
```

### Paso 3: Restaurar BBDD con datos de develop o stage

Al copiar una base de datos de un esquema a otro con el CI, genera un archivo descargable en dicho job. Descargarlo a la computadora, descomprimir el SQL y utilizar el script `ci/dba/restore.sh` 

./restore.sh $DB_SERVER $DB_USER $SQL_FILENAME $DST_DB $NEW_OWNER

En el caso de una base de datos en docker:
PGPASSWORD=sga ./restore.sh localhost sga $SQL_FILENAME sga  

Esto requiere tener instalado psql. Sino se puede ejecutar desde dentro del container. Para facilitar eso hay una carpeta compartida con el contenedor como volumen. Ubicar el archivo de dump descargado en `docker-data/postgres` y suponiendo que se llama `base_dump.pgsql` seguir los siguientes pasos.

```
#1. ingresar a bash en el container
docker-compose exec db bash
#2. restaurar la base de datos (dentro del bash del container)
PGPASSWORD=Local123 /app/pg/restore.sh localhost postgres /data/base_dump.pgsql sga postgres
#3. salir del container
exit 
```

### Paso 4: Migrar con liquibase

Usar la explicación del [directorio raiz](../README.md) o la del [archivo especifico](liquibase.md) para más detalle.

### Paso 5: Configuracion Spring

Para usar la base local que esta corriendo en Docker, abrir el archivo `application.properties` y poner las siguientes lineas:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/hospitalDB
spring.datasource.username=postgres
spring.datasource.password=Local123
```

### Errores comunes

#1. `Cannot create container for service db: status code not OK but 500: {"Message":"Unhandled exception: Drive has not been shared"}` Causado por no compartir el disco C con Docker. Hay que darle permisos.
