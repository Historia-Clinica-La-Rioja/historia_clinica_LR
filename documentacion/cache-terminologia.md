# ![logo](apps/projects/hospital/src/assets/custom/icons/icon-72x72.png) HSI | Caché de terminología

Como lo indica la [GUÍA CACHÉ SNOMED](https://hsi.pladema.net/guia-cache-snomed/) se debe utilizar el endpoint `http://localhost:8080/api/snowstorm/load-concepts-csv` que se encuentra definido en [SnowstormLoadConceptsController](../back-end/interoperability/src/main/java/net/pladema/snowstorm/controller/SnowstormLoadConceptsController.java).

Incluyendo los parámetros `file` con el archivo CSV adjunto y el parámetro `ecl` que puede tomar los valores de un [SnomedECL](../back-end/interoperability/src/main/java/net/pladema/snowstorm/services/domain/semantics/SnomedECL.java) como:

1. ALLERGY
2. BLOOD_TYPE
3. CONSULTATION_REASON
4. ... otras ...

El llamado al endpoint adjunta el archivo CSV, que queda guardado en [espacio temporal](../back-end/documentacion/multipart.md), para luego leerlo y cargar la terminología en la base de datos. Una vez que se carga todo, el endpoint responde con [la cantidad de conceptos cargados y cuantos fallaron](../back-end/interoperability/src/main/java/net/pladema/snowstorm/controller/dto/UpdateConceptsResultDto.java).

Para algunas ECL el archivo CSV es tan grande que por el tiempo que lleva su procesamiento, alguno de los proxies o balanceadores de carga responden con timeout. Siempre se puede revisar en el backoffice (el punto 2 de la [guía](https://hsi.pladema.net/guia-cache-snomed/)) cómo van apareciendo los **Conceptos** dentro del **Grupo de terminología**.

# Mejoras

Se recomienda no aumentar mucho el tamaño de los adjuntos soportados que no sería un problema para HSI excepto para esta funcionalidad ya que los CSV es lo mas grande que se adjunta. Se sugiere que sea el backend quien descargue el CSV a partir de una URL provista. También sería deseable usar el [Almacenamiento](../back-end/documentacion/almacenamiento.md) en lugar del [espacio temporal](../back-end/documentacion/multipart.md).

Dado que esta funcionalidad consume mucha base de datos para hacer la importación de tantos términos, se agregó un chequeo que funciona *por nodo* para no realizar mas de una importación a la vez. Se sugiere tener una cola de espera de terminología a migrar y que se realice por nodo en ventanas de horarios definidos (fácil como Job con Shedlock). 


