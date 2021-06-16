# Liquibase

Liquibase es una herramienta para migrar bases de datos evolutivas, al igual que Flyway. 

Define changesets como cada migración. Cada migración puede estar compuesta por diferentes changes. 

## Puesta en Marcha 

Migrar BBDD con Liquibase desde `dba/`:

```shell
#Postgres
mvn -Dliquibase.propertyFile=liquibase/postgresql.properties -Dliquibase.contexts=default liquibase:update

#MSSQL
mvn -Dliquibase.propertyFile=liquibase/mssql.properties -Dliquibase.contexts=default liquibase:update

#DB2
mvn -Dliquibase.propertyFile=liquibase/db2.properties -Dliquibase.contexts=default liquibase:update

```

## Como escribir un changeset

Cada migración la escribimos en un unico archivo. Se define un archivo por sprint. Internamente tiene cada changeset. Usamos la sintaxis YML para intentar ganar compatibilidad entre diferentes DBMS. Algunas cosas las solucionamos con constantes específicas para cada motor, esas se agregan al multivendor.yml. En caso de no haber alternativa un changeset puede ser marcado para ejecutarse en solo un dbms (ej postgres), en ese caso se deben implementar todas las opciones soportadas por el sistema, o minimamente dejar las que no se hagan con otra implementación. El criterio es que es preferible que falle una migracion de algo que no se hizo a que pase silenciosamente y no se haya creado una tabla.

## Verbos

Se listan las opciones más frecuentes de uso de Liquibase de mayor a menor uso:

Utiles para el día a día sobre BBDD propias:
* **mvn liquibase:update** : Aplica los changeset necesarios en la base `url` (toma el lock de la base mientras tanto)
* **mvn liquibase:updateSQL** : Idem anterior pero genera el SQL en un archivo en lugar de aplicarlo a la base directo. Util para Codefreeze o para revisar el SQL generado, o pasarselo a un DBA para revisar.
* **mvn liquibase:releaseLocks** : CUIDADO! Usar con precaución! Suelta locks que hayan quedado tomados por ejecuciones canceladas (pasa al hacer Ctrl-C durante un update) 

Como pasar un archivo de properties `mvn -Dliquibase.propertyFile=liquibase/liquibase.properties  liquibase:update` y link con [parametros configurables en el properties](https://www.liquibase.org/documentation/maven/generated/migrate-mojo.html).

Utiles para operaciones:

* **[mvn liquibase:diff](https://www.liquibase.org/documentation/maven/generated/diff-mojo.html)** : Genera un changeset (si esta seteado el `diffChangeLogFile`) o un informe de diff entre `referenceUrl` y `url`
* **mvn liquibase:changelogSync** : Dice que la BBDD ya tiene todos los cambios aplicados 
* **mvn liquibase:dbDoc** : Genera archivo de documentación navegable


Para leer otro properties se utiliza una property de Java configurada en el pom.xml.
* `mvn -Dliquibase.propertyFile=liquibase/postgresql.properties liquibase:update`

Utiles para proyectos nuevos:
* **mvn -Dliquibase.propertyFile=liquibase-export.properties liquibase:generateChangeLog**: Genera un changelog de ingenieria inversa de metadatos de la BBDD 

## Contexts y flavors

Los contextos son especificaciones de las características del entorno sobre el que se va a migrar.
Al migrar, con el parametro `-Dliquibase.contexts=schema-only` se indica que esquema se va a usar. 
En los changelogs se puede indicar que un changeSet se ejecute solo en determinado contexto, o condicion logica de contextos. Ejemplo
`context: !schema-only && pba`
Contextos usados en este proyecto:
* uno por flavor. 
* schema-only: indica que solo se migren los esquemas sin datos, útil para testear metadatos rapido

Atencion: en caso de no especificar el esquema se correrán **todos** los esquemas, es decir, se migrará con todos los flavors en simultaneo.

## Documentacion

*[Tabla de tipos (puede variar entre versiones)](https://dba-presents.com/index.php/liquibase/216-liquibase-3-6-x-data-types-mapping-table)