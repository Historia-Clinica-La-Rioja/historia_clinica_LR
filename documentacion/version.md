# ![logo](apps/projects/hospital/src/assets/custom/icons/icon-72x72.png) HSI | Version

## Introducción

En este documento se describe la exposición del branch y commit tanto en un endpoint del API REST del backend como en el frontend. 

Esta funcionalidad es importante para identificar fácilmente la versión exacta del código que se está ejecutando en un ambiente dado, lo que facilita el seguimiento de versiones y la depuración de errores. A continuación, se detallará cómo se ha implementado esta funcionalidad en nuestro entorno de desarrollo.

La información de branch y commit se conoce en el build, por lo tanto, resulta esencial encontrar una forma de incluir esta información en los módulos o aplicaciones que se están construyendo. 

## Backend

En el backend, se hace uso de la funcionalidad provista por Actuator y del plugin maven `git-commit-id-maven-plugin` para exponer el branch y commit. Dado que por temas de seguridad se fomenta que Actuator no se active en ambientes productivos, también se ha expuesto el branch y commit en el endpoint `/api/public/info`.

### git-commit-id-maven-plugin

El `git-commit-id-maven-plugin` es una herramienta de Maven que permite generar un archivo de propiedades en tiempo de compilación que contiene información sobre el branch y commit actual del repositorio de Git.

Para utilizar esta herramienta, se agrega la configuración correspondiente en el archivo [pom.xml](../back-end/app/pom.xml).

El plugin generará un archivo `git.properties` en la carpeta de salida del proyecto que contendrá información sobre el branch y commit actual del repositorio de Git.

### Endpoint

Aunque con el plugin alcanza para que Actuator tome el valor de commit y branch correspondiente, se definió exponerlo en el endpoint [/api/public/info](../back-end/sgx-shared/src/main/java/ar/lamansys/sgx/shared/publicinfo/infrastructure/input/rest/PublicController.java).

## Frontend

En el frontend, se utiliza el script `build:prod` del [package.json](../front-end/apps/package.json) que incluye una llamada a [version.js](../front-end/apps/version.js) para que se agregue el valor de commit y branch correspondiente en el [environment.prod.ts](front-end/apps/projects/hospital/src/environments/environment.prod.ts) para ser usado por la App. 

De esta manera, la información se inserta en los assets que forman parte de la app, se puede observar en el asset `main.[hash-del-asset].js`. Esto significa que la información no se carga con una llamada REST que pueda ser cacheada.
