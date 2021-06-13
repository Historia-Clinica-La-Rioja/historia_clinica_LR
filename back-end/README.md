# Backend

El proyecto esta desarrollado en Java en su versión 11. 

## Flujo de Git 
Para instalar y hacer uso del sistema de gestión de hospitales se debe tener en cuenta el [flujo de git](https://docs.gitlab.com/ee/topics/gitlab_flow.html) utilizado para este proyecto:

* master: es la rama por defecto del repositorio y contiene la versión más actualizada en **estado de desarrollo**. A dicha rama les falta aplicar un proceso de regresión para detectar posibles errores.
* tg-###: son las ramas alternativas usadas por el equipo de desarrollo para llevar las funcionalidades que se estan implementando.
* rc-##.##: es la rama que contiene un estado de código confirmado para su uso y estable (release candidate). Se genera en cada sprint y es donde se realiza el proceso de regresión para detección y corrección de errores.
* v#.##.#: tag que contiene una versión específica del sistema.


## Ambiente de desarrollo

Para inicializar cada uno de los ambientes de desarrollo se requiere tener una **base de datos** PostgreSQL que se puede crear siguiendo los pasos de [mas abajo](#bbdd).

El **backend** se puede iniciar para desarrollo o solamente para dar soporte al desarrollo del frontend siguiendo los pasos detallados en este documento.

El frontend está formado por dos proyectos, la app hospital y el backoffice. En cada caso, se puede iniciar para desarrollo o solamente para dar soporte al desarrollo del backend siguiendo los pasos del [front-end/README.md](front-end/README.md).


## Ambiente con docker-compose

### BBDD

Para levantar y migrar la BBDD. Levanta Postgresql con usuario "postgres" y password "Local123". La monta localmente en el puerto localhost:5432

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


### Requerimientos

Se espera tener configurada una base de datos postgreSQL con las siguientes características descriptas en el paso anterior:

* IP: localhost
* puerto: 5432
* usuario: postgres
* password: Local123
* esquema: hospitalDB

Por defecto las propiedades de desarrollo ya se encuentran configuradas con los valores definidos para la base de datos. Si algún valor cambia, entonces se pueden configurar mediante [propiedades](../properties.md) externalizadas:

* spring.datasource.url=jdbc:postgresql://**localhost:5432/hospitalDB**
* spring.datasource.username=postgres
* spring.datasource.password=Local123
 
O haciendo uso de variables de ambientes
  
* DATABASE_IP_PORT: ip y puerto de la base
* DATABASE_SCHEMA: nombre del esquema
* DATABASE_USER: nombre de usuario
* DATABASE_PASS: password del usuario

**Aclaración**: Controlar que no haya espacios de más en los valores de las propiedades.

### Iniciar backend

Una vez importado el backend en entorno local deberia estar en condiciones de levantar la aplicacion de algunas de las siguientes formas:

1. Eclipse: Buscar el App.java que esta dentro del paquete base (net.pladema) y hacer 'Run application'
2. Intellij: Buscar el App.java que esta dentro del paquete base (net.pladema) y 'Run'
3. Consola: mvn spring-boot:run dentro de la carpeta /back-end/hospital-api


Si todo es correcto se puede comprobar accediendo a la [url](http://localhost:8080/api/swagger-ui.html#/). De esta forma se debe poder visualizar en el buscador la página principal de Swagger para invocar la API con los endpoints definidos.

Haciendo uso del siguiente usuario y contraseña: admin@example.com/admin123 se pueden logear en el endpoint de authenticación y obtener un token valido.

### Profiles

Se hara uso de SpringProfile para determinar en que contexto se encuentra la aplicacion. 

Se encuentran 4 archivos de propiedades, uno para cada **Profile** :
* `application-dev.properties`: desarrollo
* `application-chaco.properties`: desarrollo
* `application-tandil.properties`: desarrollo
* `application-prod.properties`: ambiente productivo

La forma de activar un **Profile** es mediante la propiedad `spring.profiles.active=` 
> Ejemplo: `spring.profiles.active=dev` 

Para el desarrollo se debe usar `dev`, el cuál requiere activación.


### Desarrollo

Al levantar el ambiente de desarrollo por primera vez se tiene un conjunto de usuarios creados en el application-dev.properties para testear los distintos roles de la aplicación.


##### Administrativo

* usuario: administrativo@example.com
* contraseña: admin123

##### Especialista medico

* usuario: especialista@example.com
* contraseña: admin123

##### Profesional de la salud

* usuario: profesionalsalud@example.com
* contraseña: admin123


##### Administrador institucional

* usuario: administradorinstitucional@example.com
* contraseña: admin123


##### Administrador institucional

* usuario: administradorinstitucional@example.com
* contraseña: admin123


##### Enfermero

* usuario: enfermero@example.com
* contraseña: admin123

##### Administrador agenda

* usuario: administradoragenda@example.com
* contraseña: admin123


#### Renaper y federar

Estos servicios estan deshabilitados para el ambiente de desarrollo. Esto se debe a que no tenemos ambientes de pruebas de cada uno para poder usarlos localmente.


#### Configuración de tareas programadas

En el backend existen tareas programadas (scheduled jobs), las cuales son implementadas con crons de Spring, que permite que sean configurables y se ejecuten automáticamente a determinado horario y/o con determinada frecuencia. Por defecto se encuentra habilitada la ejecución de tareas programadas, pero se pueden deshabilitar seteando la propiedad `scheduledjobs.enabled=false`.

Cada tarea programada existente se debe poder habilitar/deshabilitar por propiedad.
> Ejemplo: `scheduledjobs.some-job.enabled=true`

Para configurar el horario y/o frecuencia de ejecución, cada tarea debe tener 6 parámetros configurados también por propiedades: hora, minuto, segundo, día del mes, mes y día de la semana. Cada parámetro debe seguir sus pautas según cómo se definen las [expresiones cron](https://docs.oracle.com/cd/E12058_01/doc/doc.1014/e12030/cron_expressions.htm).

> Ejemplo 1: configurar ejecución para las 3 AM todos los días.
```
scheduledjobs.some-job.seconds=0
scheduledjobs.some-job.minutes=0
scheduledjobs.some-job.hours=3
scheduledjobs.some-job.dayofmonth=*
scheduledjobs.some-job.month=*
scheduledjobs.some-job.dayofweek=*
```

> Ejemplo 2: configurar ejecución cada 3 hs, a los 0 minutos y 0 segundos, sólo días martes y jueves.
```
scheduledjobs.some-job.seconds=0
scheduledjobs.some-job.minutes=0
scheduledjobs.some-job.hours=*/3
scheduledjobs.some-job.dayofmonth=*
scheduledjobs.some-job.month=*
scheduledjobs.some-job.dayofweek=TUE,THU
```