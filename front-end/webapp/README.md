#SGH | Front-End | WebApp

Este proyecto fue generado y es mantenido usando [Angular CLI](https://github.com/angular/angular-cli) version 9.1.9.

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

## Build

Run `npm run build:prod` to build the project. The build artifacts will be stored in the `dist/` directory. Check [/scripts/build-pack.sh](../../scripts/build-pack.sh) in order to see how production built is made.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

## Convenciones

El código en este proyecto sigue los estilos propuestos en [Angular coding style guide](https://angular.io/guide/styleguide).

En el archivo [.editorconfig](./.editorconf) se definen principalmente el estilo y tamaño de identación. Comprobar que el IDE esté utilizando esta configuración. Mas información en [EditorConfig](https://editorconfig.org/).

### Módulos

La aplicación está compuesta por distintos módulos que se pueden agrupar en módulos de infraestructura y del dominio.

Los módulos de infraestructura son utilizados por los del dominio para poder implementar la funcionalidad requerida por el usuario. Proveen acceso al API REST, componentes y funcionalidad de uso común. 

1. `api-rest`: permite acceder a los Endpoints que expone el API REST, incluso expone las interfaces en TypeScript de los tipos de datos de cada Endpoint, tanto para la Petición como para la Respuesta. Los servicios expuestos y sus métodos mantienen el mismo nombre que los Controllers en el backend para permitir una búsqueda rápida.
2. `material`: declara y permite acceder a los componentes de [Material](https://material.angular.io/).
3. `presentation`: contiene los componentes visuales diseñados especificamente para este sistema.
4. `core`: contiene funcionalidad central y reutilizable, como por ejemplo Guards, Directivas, Servicios, etc.

El resto de los módulos corresponden al dominio, como por ejemplo camas, historia-clinica, pacientes, etc. La estructura de estos módulos está formada por una división en carpetas:

1. `routes`: siempre requerido
2. `components`: depende del reuso y/o modularización dentro del propio módulo
3. `services`: depende si tiene algun servicio propio

##### Idioma 
El codigo debe estar por defecto escrito en idioma inglés, tener en cuenta principalmente nombres de clases, metodos y variables. Pueden 
existir excepciones en caso que algun nombre o denominacion este intrinsecamente relacionado con el dominio del sistema.
Todo texto que sera de visibilidad al cliente (principalmente los que se ubiquen en los archivos de extension html) 
debera utilizar archivos de traduccion para ser mostrados en el idioma 
requerido por el cliente, en este caso español. 
Los nombres de modulos, por lo general correspondientes al dominio del sistema, se definen en español. 


### Assets

##### Traducciones

##### Estilos

##### Flavors

