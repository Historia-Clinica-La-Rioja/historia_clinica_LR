# HSI | Front-End | Apps

Este workspace fue generado y es mantenido usando [Angular CLI](https://github.com/angular/angular-cli) version 11.2.5.

Un workspace contiene los archivos de uno o varios projects. Un project es un conjunto de archivos que comprende una aplicación o una biblioteca. [Angular workspace](https://angular.io/guide/file-structure)

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

### Nuevo módulo con ruteo
> `ng generate module modules/backoffice --route backoffice --module app.module`
### Nuevo módulo sin ruteo
> `ng generate module modules/api-rest --module app.module`
### Nuevo endpoint
> `ng g s modules/api-rest/services/users`
### Nuevo componente ruteado
> `ng g c modules/home/routes/institucion`
### Nueva biblioteca
> `ng g library my-lib` this creates the projects/my-lib folder in the workspace, which contains a component and a service inside an NgModule.

## Build

Run `npm run build:prod` to build the project. The build artifacts will be stored in the `dist` directory. Check [/scripts/build-pack.sh](../../scripts/build-pack.sh) in order to see how production built is made.

Cuando trabajamos con bibliotecas del workspace es necesario buildearlas por separado antes de buildear la app principal. Para ello correr `ng build my-lib`

### PWA

Para testear la funcionalidad de la app hospital se requiere https, para ello se puede usar el script [/scripts/pwa-local.sh](../../scripts/pwa-local.sh).

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

## Convenciones

El código en este proyecto sigue los estilos propuestos en [Angular coding style guide](https://angular.io/guide/styleguide).

En el archivo [.editorconfig](./.editorconfig) se definen principalmente el estilo y tamaño de identación. Comprobar que el IDE esté utilizando esta configuración. Mas información en [EditorConfig](https://editorconfig.org/).

### Lint

La aplicación hace uso de la herramienta de _linting_ para verificar que se sigan las reglas especificadas.

En el CI se utilizan las reglas definidas en [tslint-ci.json](./tslint-ci.json) mientras se trabaja en eliminar las diferencias con las reglas originales de Angular definidas en [tslint.json](./tslint.json).

### Verificación que se hace en el CI
> `npm run lint`

### Reparación automática de la verificación del CI
> `npm run lint:fix`

##### Idioma 
El codigo debe estar por defecto escrito en idioma inglés, tener en cuenta principalmente nombres de clases, metodos y variables. Pueden 
existir excepciones en caso que algun nombre o denominacion este intrinsecamente relacionado con el dominio del sistema.
Todo texto que sera de visibilidad al cliente (principalmente los que se ubiquen en los archivos de extension html) 
debera utilizar archivos de traduccion para ser mostrados en el idioma 
requerido por el cliente, en este caso español. 
Los nombres de modulos, por lo general correspondientes al dominio del sistema, se definen en español. 
