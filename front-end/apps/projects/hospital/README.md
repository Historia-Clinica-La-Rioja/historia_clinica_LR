### Módulos

La aplicación está compuesta por distintos módulos que se pueden agrupar en módulos de infraestructura y del dominio.

Los módulos de infraestructura son utilizados por los del dominio para poder implementar la funcionalidad requerida por el usuario. Proveen acceso al API REST, componentes y funcionalidad de uso común.

1. [AppModule](./src/app/app.module.ts): módulo principal de la aplicación.
	* Depende de: `ApiRestModule`, `CoreModule` y `AppRoutingModule`
2. [`ApiRestModule`](./src/app/modules/api-rest/api-rest.module.ts): permite acceder a los Endpoints que expone el API REST, incluso expone las interfaces en TypeScript de los tipos de datos de cada Endpoint, tanto para la Petición como para la Respuesta. Los servicios expuestos y sus métodos mantienen el mismo nombre que los Controllers en el backend para permitir una búsqueda rápida.
3. [`CoreModule`](./src/app/modules/core/core.module.ts): contiene funcionalidad central y reutilizable, como por ejemplo Guards, Directivas, Servicios, etc.
	* Depende de: `CoreMaterialModule`
4. [`CoreMaterialModule`](./src/app/modules/core/core.material.module.ts): como un `AppMaterialModule` reducido, contiene solo las dependencias necesarias para que la aplicación inicial funcione (sin módulos lazy).
5. [`AppRoutingModule`](./src/app/app-routing.module.ts): define los *routeos* principales, cargando de manera lazy los *Feature Modules*.
	* Estos *Feature Modules* dependen de: `PresentationModule`
6. [`PresentationModule`](./src/app/modules/presentation/presentation.module.ts): contiene los componentes visuales diseñados especificamente para este sistema.
	* Depende de: `CoreModule` y `AppMaterialModule`
7. [`AppMaterialModule`](./src/app/modules/material/app.material.module.ts): declara y permite acceder a los componentes de [Material](https://material.angular.io/). Para cada nuevo desarrollo  de componentes visuales se debera evaluar resolver en primera instancia con lo ofrecido por *Material* antes de aplicar un desarrollo custom o inclusión de nuevas librerías. 

#### Feature Modules

Son los módulos que corresponden al dominio, como por ejemplo [`CamasModule`](./src/app/modules/camas/camas.module.ts), [`HistoriaClinicaModule`](./src/app/modules/historia-clinica/historia-clinica.module.ts), pacientes, etc. 
La estructura de estos módulos está formada por una división en carpetas que debe seguir la siguiente definición:

1. `routes`: siempre requerido, corresponde a los componentes directamente relacionados con cada pagina de la aplicacion a la que le corresponde una ruta particular.
2. `components`: depende del reuso y/o modularización dentro del propio módulo, son utilizados por los componentes de *routes*.
3. `services`: Estaran presentes dependiendo si es requerido algun servicio propio del modulo.

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

El [AppComponent](./src/app/app.component.html) define la clase de la paleta seleccionada para afectar a toda la aplicación. 

### Layout

En el [AppRoutingModule](./src/app/app-routing.module.ts) se definen los *routeos* principales. Cada uno de estos routeos apunta a un módulo principal de funcionalidad. Estos módulos deben definir el layout de la aplicación, para esto utilizan el [`MainLayoutComponent`](./src/app/modules/presentation/components/main-layout/main-layout.component.ts).

Se puede ver un ejemplo de uso de `MainLayoutComponent` en los componentes principales de los módulos
* [AuthModule](./src/app/modules/auth/auth.component.html) donde no se muestra información del usuario ni de la institución
* [InstitucionModule](./src/app/modules/institucion/institucion.component.html) donde se muestra información del usuario y de la institución.




