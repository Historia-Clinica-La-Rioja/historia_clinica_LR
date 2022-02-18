# ¿Qué contiene este MR?

<!-- Breve descripción -->
Añadir una breve descripción del contenido del MR, incluyendo aclaraciones si fuera necesario.

# ¿A que conjunto de issue/tickets esta asociado el MR?

<!-- Lista de links a los issues o user stories asociados en GIT -->
Listar en formato de links los issues o user stories asociados al MR. 
  
# Requerimientos a cumplir

## Generales

- [ ] Añadir excepciones específicas de los flujos, incluyendo mensajes de errores o advertencias.
- [ ] Respetar la división de paquetes establecida para el proyecto.
- [ ] No exceder las responsabilidades de la clase.
- [ ] Respetar la convención de nombres en Java. [Ver más](https://howtodoinjava.com/java/basics/java-naming-conventions/).
- [ ] Añadir logs bajo el nivel más apropiado según la información a mostrar. Declarar el método [toString](https://projectlombok.org/features/ToString) en los objetos para que el log muestre bien la información
- [ ] Escribir código legible —*por ejemplo a través del uso de **Java.util.Stream** en lugar de **for***—). [Ver más](https://medium.com/swlh/writing-readable-and-maintainable-code-java-8c0adc2f5930).
- [ ] Separar en nuevas tareas funcionalidad resultante de detectar mejoras/bugs no contempladas en la tarea original.
- [ ] Analizar la necesidad de refactorizar el código en base al tamaño de la funcionalidad.
 
## Repositorio

- [ ] Respetar la siguiente nomenclatura para los mensajes de commits: **tg-###. descripción**.  Donde **###** es el identificador de tarea en Taiga.
- [ ] Agregar descripciones claras y concisas en los mensajes de commits. El mensaje debe describir ***QUÉ*** se hizo y no ***CÓMO***. 
- [ ] Dividir los commits en funcionalidades atómicas, generalmente en concordancia con las tareas creadas en Taiga.
- [ ] Verificar la ejecución exitosa del pipeline en el CI.

## Base de datos

- [ ] Respetar las convenciones definidas para la [base de datos](../dba/documentacion/convenciones.md).
- [ ] Escribir queries bajo el siguiente orden de prioridad:
	- Queries abstractas cuando sea posible —*bajo lenguaje de JPA*—.
	- Queries nativas con sintaxis SQL estándar para evitar dependencia con un motor de base de datos específico. 
	- Queries nativas con lenguaje SQL específico de un motor considerando el cambio en el resto de dialectos definidos. 
- [ ] Escribir queries optimizadas tanto en la capa de repositorio como en la capa de servicio.
- [ ] Sí hay cambios de base de datos, probar ejecutar el deploy para determinar que funcione bien cuando se vaya a deployar en master.
- [ ] Controlar que la query generada sea la esperada.
- [ ] Sí es requerido controlar la existencia de indices.
- [ ] Controlar que las tablas tengan primary key. 
 
## Código

- [ ] Respetar las convenciones definidas en [back-end](../../back-end/documentacion/convenciones.md).
- [ ] Respetar las convenciones definidas en [front-end](../../front-end/README.md).

## Testing

- [ ] Incluir tests unitarios al agregar nueva funcionalidad.


