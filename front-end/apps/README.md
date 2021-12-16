# ![logo](projects/hospital/src/assets/custom/icons/icon-72x72.png) HSI | Front-End | Apps

Este workspace fue generado y es mantenido usando [Angular CLI](https://github.com/angular/angular-cli) en la version definida en el [archivo de configuración principal](package.json#L55).

Un [**workspace**](https://angular.io/guide/file-structure) contiene los archivos de uno o varios **projects**. Un **project** es un conjunto de archivos que comprende una aplicación o una biblioteca.


## Development server

```shell
ng serve
```

> Abrir http://localhost:4200/ en el navegador. La aplicación se recargará automáticamente al modificar cualquier archivo fuente. 

## Code scaffolding

```shell
#Generar nuevo componente
ng generate component component-name

#También se encuentran disponibles los siguientes comandos
ng generate directive|pipe|service|class|guard|interface|enum|module
```

#### Nuevo módulo con ruteo
> `ng generate module modules/backoffice --route backoffice --module app.module`
#### Nuevo módulo sin ruteo
> `ng generate module modules/api-rest --module app.module`
#### Nuevo endpoint
> `ng g s modules/api-rest/services/users`
#### Nuevo componente ruteado
> `ng g c modules/home/routes/institucion`
#### Nueva biblioteca
> `ng g library my-lib` this creates the projects/my-lib folder in the workspace, which contains a component and a service inside an NgModule.

## Build

```shell
#Buildear bibliotecas del workspace (si aplica)
ng build my-lib

#Buildear la app principal
npm run build
```

> Los artefactos generados se almacenarán en el directorio `dist/`. Ver el archivo [build-pack.sh](../../scripts/build-pack.sh) para más detalle sobre cómo se realiza el proceso en producción.


#### Ver además:

- Convenciones para escritura de código. [Ver](../documentacion/convenciones.md)
- Mockups para desarrollo ágil de nuevos módulos. [Ver](../documentacion/mockups.md)

## PWA

Dado que levantar el proyecto en modo desarrollo (por ejemplo: `npm start`) no permite usar las características PWA — *requiere el uso de HTTPS* —, se ofrece un script y un contenedor Docker para poder realizar pruebas localmente.

El script [pwa-local.sh](../scripts/pwa-local.sh) buildea la app Hospital, levanta un contenedor Docker con nginx configurado para acceder al código buildeado y al backend en http://localhost:8080, y por último utiliza ngrok para obtener un acceso con https.

> NOTA: no funciona de la misma manera en Windows, Linux o Mac así que algunos ajustes podrían ser necesarios.

## Testing

Ejecutar **Unit Tests** vía [Karma](https://karma-runner.github.io).

```shell
ng test
```

Ejecutar **End-to-end Tests** via una plataforma de tu elección.

```shell
ng e2e
```

> Para usar este comando, primero debe agregar un paquete que implemente capacidades de testing end-to-end. 
