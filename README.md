![Historia Clinica](documentacion/images/HC-logo.png)

# HospitalApp

## Instalación

Para instalar y hacer uso del sistema de gestión de hospitales se debe tener en cuenta el [flujo de git](https://docs.gitlab.com/ee/topics/gitlab_flow.html) utilizado para este proyecto:

* master: es la rama por defecto del repositorio y contiene la versión más actualizada en **estado de desarrollo**. A dicha rama les falta aplicar un proceso de regresión para detectar posibles errores.
* tg-###: son las ramas alternativas usadas por el equipo de desarrollo para llevar las funcionalidades que se estan implementando.
* rc-##.##: es la rama que contiene un estado de código confirmado para su uso y estable (release candidate). Se genera en cada sprint y es donde se realiza el proceso de regresión para detección y corrección de errores.


Habiendo aclarado esto, para poder hacer uso del sistema estable, es necesario generar los artefactos a deployar desde las ramas de release candidate. Esta rama tiene el control de calidad necesario para su uso.


## Contribución

Para el envío de propuestas, consultas técnicas o especificación funcional y responsable de la aprobación de las mismas, escribir al siguiente mail de contacto: historia_clinica@pladema.exa.unicen.edu.ar. 
    
Este código de Historia clínica, se encuentra  disponible en [gitlab-publico.pladema.net/historia-clinica/sgh](http://gitlab-publico.pladema.net/historia-clinica/sgh). 
    
Para subir aportes utilizar un fork y pedir un Merge Request a dicha URL.

## Proyecto

Sitio de demo: http://sgh.pladema.net/auth/login

El proyecto está formado por los siguientes tres proyectos:

1. front-end/backoffice: Single Page Application del panel de administración (backoffice) para manejo de los flujos básicos y creación de datos maestros. Implementada con ReactAdmin.
2. front-end/webapp: Single Page Application que contiene la funcionalidad principal del sistema. Implementada en Angular 9.
3. back-end/hospital-api: API REST de todo el sistema implementada con Spring Boot 2.3 y Java 11

## Ambiente de desarrollo

Para inicializar cada uno de los ambientes de desarrollo se requiere tener una **base de datos** PostgreSQL que se puede crear siguiendo los pasos de [mas abajo](#bbdd).

El **backend** se puede iniciar para desarrollo o solamente para dar soporte al desarrollo del frontend siguiendo los pasos del [back-end/README.md](back-end/README.md).

El frontend está formado por dos proyectos, una webapp y el backoffice. En cada caso, se puede iniciar para desarrollo o solamente para dar soporte al desarrollo del backend siguiendo los pasos del [front-end/README.md](front-end/README.md).


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


## Ambiente completo con Docker (simil ambientes de QA)

Ejecutar los pasos de BBDD para tener la BBDD local. Despues en la raiz del proyecto.
Utiliza algunas variables ya creadas en el script de migracion, asegurarse de que estén creadas.

```shell
# Compilar binarios
./scripts/build-pack.sh 
# Preparar archivo de propiedades
./scripts/prepare-property-file.sh /dev/null "" "" postgresql $JDBC_URL postgres Local123
cp env.properties /tmp/app-local-test.properties
# Construir imagen docker
./scripts/build-docker.sh -t app:local
# desplegar usando la version que acabamos de buildear
./scripts/deploy-with-docker.sh app-local-test app:local url.example.com true true
# Ver el puerto en el que se mapeo con 
docker ps
```

## Licencia

Apache 2.0

Consulte el archivo de LICENCIA incluido para obtener más detalles.


