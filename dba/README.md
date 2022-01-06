# ![logo](../front-end/apps/projects/hospital/src/assets/custom/icons/icon-72x72.png) HSI | Base de datos

El DBMS utilizado actualmente es **Postgres**. En el desarrollo iterativo e incremental se producen cambios estructurales y metadatos de la base de datos, para gestionar esto se utiliza la herramienta [liquibase](https://liquibase.org).

## Requisitos 

1. Instalar y correr [docker](https://www.docker.com/products/docker-desktop).
2. Maven 3.6.

## Puesta en Marcha 

Levantar una DDBB de PostgreSQL:

```shell
docker-compose up -d postgresql
```

La creación o actualización de las estructuras y metadatos requeridos en el sistema lo realiza el backend al iniciar.

Además, se podrá levantar Cube con el siguiente comando:

```shell
docker-compose up -d cube
```

## Popular DDBB con datos de ambiente

Tras ejecutar los pasos descriptos en el apartado **Puesta en Marcha**, la base de datos dispondrá de todas las estructuras básicas — *tablas, vistas, claves primarias, claves extranjeras, índices, etc.*— y todos los metadatos. Sin embargo, al momento de desarrollar una nueva funcionalidad en el sistema o resolver un error, podría ser necesario contar con una base completa de datos. Para ello, se recomienda ***copiar*** una DDBB de un ambiente de testing a la base de datos local en Docker. 
> Para realizar la copia de datos debe seguirse el [instructivo](https://git.pladema.net/minsalud/sgh-os-infra/-/blob/master/guides/database/dump&restore.md#ejemplo-de-aplicaci%C3%B3n) correspondiente. 

### Desarrollo

- Información detallada y comandos útiles para el desarrollo. [Leer más](documentacion/liquibase.md).
- Convenciones para escribir cambios en la DDBB. [Leer más](documentacion/convenciones.md). 
- Diagrama de entidades y relaciones [Ver](https://app.diagrams.net/#G1r06bHAOUC4iIMkH8zuPFilpe8w9U2d7f).

### Troubleshooting

| Diagnóstico | Descripción |
|---|---|
| Problema | **Windows**: `Cannot create container for service db: status code not OK but 500: {"Message":"Unhandled exception: Drive has not been shared"}`|
| Causa | El disco C no está compartido con Docker |
| Solución | Otorgar permisos|

