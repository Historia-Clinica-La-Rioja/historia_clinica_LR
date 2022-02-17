### ![logo](../../front-end/apps/projects/hospital/src/assets/custom/icons/icon-72x72.png) Módulo de extensiones | Componentes



# Introducción a extensiones

Extensiones tiene como objetivo plantear un mecanismo claro y sencillo para permitir agregar cualquier tipo de funcionalidad en la solución sin la necesidad de mantener un fork.

Esta alternativa tiene la ventaja de permitir actualizar la versión del sistema central sin requerir una revisión manual del código, buildear y publicar un contenedor modificado.

La desventaja es que los puntos de extensión permitidos son definidos previamente y aunque pueden ampliarse en una nueva versión del sistema, puede suceder que no sea suficiente para la Jurisdicción.


## Implementación

Para cumplir con el propósito mencionado se creó un módulo bajo la carpeta de back-end llamado `extensiones` que expone los endpoints con el prefijo "extensions".

Actualmente SGH brinda tres puntos de extensión de la aplicación:

* `/extensions/menu` Opciones a agregar en el menú principal del sistema.
* `/extensions/page/{menuId}` Página a mostrar en la opción de menú del sistema seleccionada.
* `/extensions/institution/{institutionId}/menu` Opciones a agregar en el menú de una institución.
* `/extensions/institution/{institutionId}/page/{menuId}` Página a mostrar en la opción de menú seleccionada en una institución.
* `/extensions/patient/{patientId}/menu` Pestañas a agregar en la historia clínica de un paciente.
* `/extensions/patient/{patientId}/page/{menuId}` Contenido a mostrar en en el tab de paciente.

La clave del módulo de extensiones es que se puede configurar para que redirija los pedidos a estos endpoints a un sistema externo que debe ser capaz de implementar todos estos endpoints.

Cada endpoint de menu retorna una lista de [UIMenuItemDto](../../back-end/extensions/src/main/java/net/pladema/hsi/extensions/infrastructure/controller/dto/UIMenuItemDto.java) y cada endpoint de página asociada al menú un [UIPageDto](../../back-end/extensions/src/main/java/net/pladema/hsi/extensions/infrastructure/controller/dto/UIPageDto.java).

La lista completa de DTOs de este módulo se puede observar en [extensions-model.d.ts](../../front-end/apps/projects/hospital/src/app/modules/extensions/extensions-model.d.ts).

A continuación se detallan los componentes que se pueden utilizar para definir una página asociada a un menú.

## Componentes

Los componentes que serán brindados para el uso de las jurisdicciones estan definidos en el front-end de la aplicación y los mismos se encuentran en [ui-component.component.html](../../front-end/apps/projects/hospital/src/app/modules/extensions/components/ui-component/ui-component.component.html).

El catálogo completo de componentes, su parametrización y su apariencia visual, se puede apreciar en la opción del menú "Componentes" al activar el "Módo de demostración" del módulo de extenciones.

Cuando un componente que se desea agregar no coincide con ninguno de los mencionados anteriormente, obtendremos un texto que informa que el mismo es indefinido.

## Módo de demostración

El propósito del mismo es agregar opciones de menú y solapas en la historia clínica del paciente para mostrar ejemplos de cómo se puede agregar funcionalidades.

Este módo utiliza los archivos estáticos que se encuentran como "ClassPathResource" dentro de [module/demo](../../back-end/extensions/src/main/resources/module/demo).

# Extensiones Locales

Los puntos de extensión también pueden ser aprovechados por módulos opcionales del sistema para mostrar información particular. Por ejemplo, el módulo [sgx-dashboards](../sgx-dashboards/src/main/java/ar/lamansys/sgx/cubejs/infrastructure/configuration/CubejsAutoConfiguration.java) cuando está activo extiende el menú en la página principal del sistema para mostrar tableros (actualmente un placeholder).

Para agregar un menú en la página principal del sistema se debe crear un `@Bean` que implemente `SystemMenuExtensionPlugin` conteniendo información del menú y la página asociada. Convenientemente se implementó el método [SystemMenuExtensionPlugin fromResources(String menuId)](../extensions/src/main/java/net/pladema/hsi/extensions/configuration/plugins/SystemMenuExtensionPluginBuilder.java) que sencillamente retorna `menu` y su `page` cargando el json correspondiente en `classpath:extension/{menuId}/menu_o_page.json`.

> Nota: cada módulo puede tener estos recursos dentro de su carpeta 'resources', como [sgx-dashboards](../sgx-dashboards/src/main/resources/extension/tableros/).

Esto crea una dependencia de este módulo con `extensions`, en el [pom.xml](../sgx-dashboards/pom.xml#L24).

``` xml
 <dependency>
     <groupId>ar.lamansys</groupId>
     <artifactId>extensions</artifactId>
     <version>${revision}</version>
     <scope>compile</scope>
 </dependency>
```

Esta dependencia no es un problema si es desde un módulo `net.pladema.sgh` (los módulos `sgx-` no deberían tener esta dependencia).

## FeatureFlags

Definir si se necesita integración con FeatureFlags

## Authorization

Será necesario que las extensiones estén habilitadas para determinados roles.