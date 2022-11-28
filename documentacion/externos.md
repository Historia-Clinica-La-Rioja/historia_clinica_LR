

# ![logo](apps/projects/hospital/src/assets/custom/icons/icon-72x72.png) HSI | Requerimientos privados

En este documento se detalla el problema que presenta  HSI al incorporar funcionalidad requerida por un dominio en particular y la solucion al mismo.

## Introduccion 

El sistema de hospitales es un codigo open source que contiene un conjunto de  requerimientos comunes que han sido solicitados por la mayor parte de los clientes.

Actualmente, para abarcar las necesidades de cada uno de los clientes, los desarrolladores de la empresa deben incorporar el desarrollo solicitado en el código principal del proyecto, lo que resulta en un continuo crecimiento del mismo ya sea por agregar código al proyecto ( desarrollar lo requerido) o bien por agregar dependencias con distintos sistemas externos al SGH (por ejemplo, llamados REST). Hacer estas incorporaciones en el código principal resulta en un entregable de mayor tamaño innecesario para aquellos dominios que no desean incorporar la funcionalidad solicitada por otros.

Además de este inconveniente a nivel producto, el equipo debe realizar las estimaciones de costos y tiempo para cada cliente y para cada desarrollo en particular y a partir de allí, realizar el desarrollo de los mismos.


##Realidad
Para mostrar la situación con mayor claridad, a continuación se describen algunas situaciones a las que el grupo de trabajo de Pladema está expuesto en el proyecto:

- Se necesita notificar sobre tareas a realizar a usuarios o roles, dentro de una institución de salud.
Esta funcionalidad es solicitada por entidades privadas y es por esto que el código para abarcar esta funcionalidad no debiera pertenecer al código open source del proyecto. De ser así, se debería buscar algún mecanismo para qué esta funcionalidad este deshabilitada para aquellas entidades que no están interesadas.

  *Hasta este momento, este tipo de inconvenientes es solucionado con el uso Features Flags*.

- Se necesita avisar a otros sistemas o módulos sobre eventos que ocurren en el sistema, como por ejemplo: cada vez que se realiza una internación se debe avisar al sistema de facturación.
Esta comunicación no es conveniente hacerla de manera explícita en el código ya qué sino SGH quedaría con una dependencia con los nuevos sistemas involucrados. 

    *Hasta este momento, este tipo de inconvenientes es soluciona agregando la dependencia explícita con los sistemas*.

##Solucion
Con el fin de cubrir las problemáticas con las qué se encuentra Pladema en SGH, en el siguiente informe se hace hincapié en agregar una publicaciones de eventos ( también conocida como EDA por sus siglas en inglés: Event Driven Architecture). La misma permite cubrir las necesidades mencionadas en el apartado anterior, haciendo qué el sistema quede desacoplado del resto de sistemas, evitando así las dificultades y diferencias que se presentan hoy en día.

Al momento de finalizar una acción en el sistema, que implica el desencadenamiento de otras acciones en sistemas ajenos, el sistema SGH publica un evento que notifica que ha finalizado un cambio de estado de una parte del sistema, haciendo así que otros puntos interesados reciban esta notificación y actúen en consecuencia, haciendo qué cada sistema pueda continuar con su tecnología de preferencia.
