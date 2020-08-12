# Base de datos

El DBMS utilizado actualmente es **Postgres**.

## Ambiente de desarrollo

Para utilizar una base de datos local, se sugiere:
1. instalar docker
2. crear el contenedor con docker-compose
3. luego ejecutar liquibase
4. cambiar configuracion del proyecto para usar la base local

### Paso 2: Docker

En la raiz del proyecto, abrir shell y ejecutar:

```shell
docker-compose up -d
```

### Paso 4: Migrar con liquibase

Usar la explicación del [directorio raiz](../README.md) o la del [archivo especifico](../dba/Documentacion/liquibase.md) para más detalle.

### Errores comunes

#1. `Cannot create container for service db: status code not OK but 500: {"Message":"Unhandled exception: Drive has not been shared"}` Causado por no compartir el disco C con Docker. Hay que darle permisos.
