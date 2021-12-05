# Snowstorm ecl

La idea es explicar brevemente como usar las ECLs en desarrollo.


## Frontend

Desde el frontend al momento de llamar al [SnomedService](../../front-end/apps/projects/hospital/src/app/modules/historia-clinica/services/snomed.service.ts), se debe pasar en el atributo de eclFilter un valor del enumerado SnomedECL que se encuentra en el archivo [api-model.ts](../../front-end/apps/projects/hospital/src/apps/projects/hospital/src/app/modules/api-rest/api-model.d.ts). Dicho enumerado es actualizado desde el backend cuando se agreguen o eliminen nuevas ecls. Por ejemplo, el siguiente [componente](../../front-end/apps/projects/hospital/src/app/modules/historia-clinica/modules/ambulatoria/services/medicaciones-nueva-consulta.service.ts) usa el SnomedECL.MEDICINE para buscar medicamentos.


## Backend

El archivo [snomed-semantis.properties](../../back-end/interoperability/src/main/resources/snomed-semantics.properties) tiene los valores por defectos de las propiedades a cargar. Se usa combinación de propiedades para simplificar la asignación de valores y reducir el código repetido. De todas se puede reemplazar su valor desde otro archivo de propiedades como se hace normalmente. Las propiedades que se usan están definidas en el archivo de [propiedades](../../properties.md)

Se creó el enumerado [SnomedECL](../../back-end/interoperability/src/main/java/net/pladema/snowstorm/services/domain/semantics/SnomedECL.java), que contiene todas las ECLs que hoy por hoy el sistema puede usar desde el frontend. Si se desean agregar o eliminar tipos de ecls se debe modificar este enumerado y actualizar el api-model.ts. Un cambio en este archivo puede borrar un tipo de ECL que el frontend utiliza.


