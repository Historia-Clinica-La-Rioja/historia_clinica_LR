# ![logo](apps/projects/hospital/src/assets/custom/icons/icon-72x72.png) HSI | Front-End

El front-end del proyecto está compuesto por la app Hospital y un Backoffice.

## Requisitos

1. Para el desarrollo de ambos proyectos se puede utilizar [Visual Studio Code](https://code.visualstudio.com/Download) o [WebStorm](https://www.jetbrains.com/webstorm/download/).
2. Node en la versión definida en el [POM principal](../pom-parent.xml#L18) del proyecto.
3. [NPM](https://www.npmjs.com/get-npm) en la versión definida en el [POM principal](../pom-parent.xml#L19) del proyecto.
4. Yarn. 

## LR/DR;

### Hospital

**Descargar** dependencias de desarrollo desde la carpeta `front-end/apps`

```shell
npm install
yarn install
```

> Este paso sólo debe ejecutarse cada vez que se agregue una nueva dependencia.

**Buildear** bibliotecas propias del proyecto:

```shell
npm run build:odontology
```

**Levantar** servidor node local de desarrollo:

```shell
npm start
```

> Las características PWA no pueden probarse en modo desarrollo.

### Backoffice

**Descargar** dependencias de desarrollo desde la carpeta `front-end/backoffice`

```shell
npm install
```

**Levantar** servidor local de desarrollo:

```shell
npm start
```


## Desarrollo

- Comandos útiles para desarrollo de app Hospital. [Leer más](apps/README.md).
- Comandos útiles para desarrollo de backoffice. [Leer más](backoffice/README.md).

## Configuración de IDE

### WebStorm 

1. Settings -> Editor -> Code Style -> JavaScript -> Spaces -> Within ES6 Import/export braces
