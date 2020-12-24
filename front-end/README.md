# SGH | Front-End

El front-end del proyecto está compuesto por una WebApp y un Backoffice.

## Ambiente de desarrollo

Para el desarrollo de ambos proyectos se puede utilizar [Visual Studio Code](https://code.visualstudio.com/Download) o [WebStorm](https://www.jetbrains.com/webstorm/download/).

Además ambos proyectos requieren tener instalado [npm](https://www.npmjs.com/get-npm) en la versión definida en [../pom-parent.xml](../pom-parent.xml#L19).

### WebApp

El primer paso es descargar todas las dependencias de desarrollo, este paso se debe hacer cada vez que se agregue una nueva dependencia. En la raíz del repositorio ejecute:

```shell
# entrar al directorio del proyecto
cd front-end/webapp/
# descargar las dependencias de desarrollo
npm install
# levantar el proyecto en modo desarrollo
npm start
```

> Mas comandos útiles para el desarrollo en [webapp/README.md](webapp/README.md).

### Backoffice

El primer paso es descargar todas las dependencias de desarrollo, este paso se debe hacer cada vez que se agregue una nueva dependencia. En la raíz del repositorio ejecute:

```shell
# entrar al directorio del proyecto
cd front-end/backoffice/
# descargar las dependencias de desarrollo
npm install
# levantar el proyecto en modo desarrollo
npm start
```

> Mas comandos útiles para el desarrollo en [backoffice/README.md](backoffice/README.md).

## Configuración de IDE

### WebStorm 

1. Settings -> Editor -> Code Style -> JavaScript -> Spaces -> Within ES6 Import/export braces
