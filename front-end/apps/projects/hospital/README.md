### Módulos

La aplicación está compuesta por distintos módulos que se pueden agrupar en módulos de infraestructura y del dominio.

Los módulos de infraestructura son utilizados por los del dominio para poder implementar la funcionalidad requerida por el usuario. Proveen acceso al API REST, componentes y funcionalidad de uso común. 

1. `api-rest`: permite acceder a los Endpoints que expone el API REST, incluso expone las interfaces en TypeScript de los tipos de datos de cada Endpoint, tanto para la Petición como para la Respuesta. Los servicios expuestos y sus métodos mantienen el mismo nombre que los Controllers en el backend para permitir una búsqueda rápida.
2. `material`: declara y permite acceder a los componentes de [Material](https://material.angular.io/). Para cada nuevo desarrollo 
de componentes visuales se debera evaluar resolver en primera instancia con lo ofrecido por *Material* antes de aplicar un desarrollo 
custom o inclusión de nuevas librerías. 
3. `presentation`: contiene los componentes visuales diseñados especificamente para este sistema.
4. `core`: contiene funcionalidad central y reutilizable, como por ejemplo Guards, Directivas, Servicios, etc.

El resto de los módulos corresponden al dominio, como por ejemplo camas, historia-clinica, pacientes, etc. 
La estructura de estos módulos está formada por una división en carpetas que debe seguir la siguiente definición:

1. `routes`: siempre requerido, corresponde a los componentes directamente relacionados con cada pagina de la aplicacion a la que le 
corresponde una ruta particular.
2. `components`: depende del reuso y/o modularización dentro del propio módulo, son utilizados por los componentes de *routes*.
3. `services`: Estaran presentes dependiendo si es requerido algun servicio propio del modulo.

### Dependencia de módulos

1. [AppModule](./src/app/app.module.ts)
	a. ==carga lazy=> *Feature Modules*[^1]

2. *Feature Modules* ==depende de=> PresentationModule, LazyMaterialModule (opcional)

3. [PresentationModule](./src/app/modules/presentation/presentation.module.ts) ==depende de=> AppMaterialModule, CoreModule

4. CoreModule ==depende de=> MinMaterialModule

5. AppModule ➡ ==depende de=> CoreModule, ApiRestModule

[^1]: Feature Modules: Auth, Home, Institucion, Camas, Guardia, Turnos, etc

### Assets

##### Traducciones

Los archivos de traducciones estan organizados dentro de la carpeta `i18n` separados para mantener una mejor separacion y orden de todos 
los textos presentes en la aplicacion. 
Existe una carpeta por módulo donde cada una contiene un archivo *es-AR.json* organizado de la siguiente forma:

&nbsp;{  
	&nbsp;&nbsp;&nbsp; "nombre-modulo": {  
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "nombre-submodulo/componente/pantalla": {  
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "NOMBRE_IDENTIFICADOR_TEXTO": "Texto"  
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; }  
	&nbsp;&nbsp;&nbsp; }  
&nbsp;}  
  
##### Estilos

En la carpeta `styles` se encuentran los archivos *.scss* correspondientes a los estilos generales o de uso común para la aplicación.

1. `variables`: Define las variables a utilizar.
2. `mixins`: Define mixins de uso comun para aplicar a distintos elementos de la aplicacion.
3. `mat-mixins`: Define mixins de uso comun para aplicar a distintos elementos de Material a customizar.
4. `palettes`: Corresponde a las paletas de colores.
5. `theme`: Configura propiedades del theme como la paleta de colores general y para distintos componentes en particular.


### Estructura

En el `body` se definen las clases [mat-typography](https://material.angular.io/guide/typography) [mat-app-background](https://material.angular.io/guide/theming) que afectan a toda la aplicación.

La etiqueta [`noscript`](https://uniwebsidad.com/libros/javascript/capitulo-1/etiqueta-noscript) contiene el mensaje sugerido.


