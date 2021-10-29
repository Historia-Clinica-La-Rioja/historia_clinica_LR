### ![logo](../../front-end/apps/projects/hospital/src/assets/custom/icons/icon-72x72.png) Módulo de extensiones | Componentes
 


# Introducción a extensiones

Una Jurisdicción que cuente con un equipo de desarrollo propio puede decidir implementar sus propias funcionalidades usando las extensiones disponibles en el sistema.

Esta alternativa tiene la ventaja de permitir actualizar la versión del sistema central sin requerir una revisión manual del código, buildear y publicar un contenedor modificado.

La desventaja es que los puntos de extensión permitidos son definidos previamente y aunque pueden ampliarse en una nueva versión del sistema, puede suceder que no sea suficiente para la Jurisdicción.


## Implementación

Para cumplir con el propósito mencionado se creó un módulo bajo la carpeta de back-end llamado `extensiones`.

Actualmente SGH brinda tres puntos de extensión de la aplicación:

* Extender el menú en la página principal del sistemas.
* Extender el menú una vez dentro de una institución.
* Extender los tabs de la historia clínica de un paciente.  

Cada uno de estos puntos de extensión debe contener en un archivo `menu.json` el listado de elementos que desea añadir. Cada uno de los ítems que se agreguen a la lista deben ser del tipo `UIMenuItemDto`.

```typescript
export interface UIMenuItemDto {
    icon: string; // ==> Icono a agregar
    id: string; // ==> nombre del archivo .json en donde se encuentran los componentes a visualizar
    label: UILabelDto; // ==> Texto informativo que puede ser el valor o bien una key para traducción
}
```
Como se anticipó en la definición del dto, se debe contar con otro archivo de extensión `.json` bajo el módulo `page` que contenga la lista de componentes que se desea agregar. Los mismos estarán definidos por el `UIComponentDto` 

```typescript
export interface UIComponentDto {
    type: string; // ==> Tipo de componente que se desea agregar
    args: { [index: string]: any }; // ==> Lista de argumentos para el tipo de componente deseado
}
```

## Definición de componentes
Los componentes que serán brindados para el uso de las jurisdicciones estan definidos en el front-end de la aplicación y los mismos se encuentran en [ui-component.component.html][2] 

## Componentes

A continuación se listan una serie de componentes diseñados por el equipo de desarrollo con el fin de ser utilizados por cada uno de las jurisdicciones:

* Typography: Permite añadir un texto añadiendo los [estilos provistos por material][1] ( No debe agregarse el prefijo `mat`)
* Link: Permite agregar un botón con un título
* Html: Permite insertar una porcion de codigo html
* Code: Permite insertar texto con formato de código

Cuando un componente que se desea agregar no coincide con ninguno de los mencionados anteriormente, obtendremos un texto que informa que el mismo es indefinido. 

## Módulo Demo

El propósito del mismo es mostrar ejemplos de cómo una jurisdicción puede agregar sus funcionalidades. Este se encuentra  bajo el módulo de `extensiones` mencionado anteriormente.

Para conocer cómo es posible agregar componentes, se agrego un item al menu de la pagina principal de la aplicación en donde se cuenta con el código json de cada uno de los componentes proveídos por el sistema junto a su código `json` para que el mismo pueda ser copiado y pegado en el lugar deseado. 

 [1]: https://material.angular.io/guide/typography#using-typography-styles-in-your-application
 [2]: https://git.pladema.net/minsalud/sgh-os/-/blob/67b497d838947e7cccfa41bda1f9fa6279fa4bbf/front-end/apps/projects/hospital/src/app/modules/presentation/components/ui-component/ui-component.component.html


