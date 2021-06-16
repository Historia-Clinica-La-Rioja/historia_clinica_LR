## Convenciones

El código en este proyecto sigue los estilos propuestos en [Angular coding style guide](https://angular.io/guide/styleguide).

En el archivo [.editorconfig](./.editorconfig) se define principalmente el estilo y tamaño de identación, por lo que resulta fundamental comprobar que el IDE utilizado para desarrollo esté utilizando esta configuración. 

> Mas información en [EditorConfig](https://editorconfig.org/).

### Lint

La aplicación hace uso de la herramienta de _linting_ para verificar que se cumplan las reglas especificadas.

En el CI se utilizan las reglas definidas en [tslint-ci.json](./tslint-ci.json) mientras se trabaja en eliminar las diferencias con las reglas originales de Angular definidas en [tslint.json](./tslint.json).

**Verificación que se hace en el CI**
> `npm run lint`

**Reparación automática de la verificación del CI**
> `npm run lint:fix`

### Idioma 

El código debe estar por defecto escrito en **idioma inglés**.

- Nombres de clases, métodos y variables.

**Excepciones**:

- Nombres o denominaciones intrínsecamente relacionados con el dominio del sistema deben escribirse en español.
- Nombres de módulos — *generalmente correspondientes al dominio del sistema* — se definen en español.
- Textos con visibilidad al cliente — *principalmente ubicados en archivos de extensión html* — deberán utilizar archivos de traducción para ser mostrados en el idioma requerido por el cliente, en este caso, español.