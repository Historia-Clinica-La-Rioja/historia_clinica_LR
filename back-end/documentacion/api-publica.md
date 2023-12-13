# ![logo](../../front-end/apps/projects/hospital/src/assets/custom/icons/icon-72x72.png) Back-end | API Pública

La API Pública es el conjunto de todos los endpoints que están expuestos en:

*  `/api/public-api/`

Implementados en grupos de endpoints en el módulo [hospital-public-api](../hospital-public-api/).

## Autenticación

El manejo de sesión se realiza a través de API Keys.

## Authorización

Se permite que cualquier API Key asignada a un usuario con rol API_CONSUMER tenga acceso completo al API Pública.

## Facturación 

Permite obtener información de actividades realizadas que se pueden facturar.

En swagger los endpoints están agrupados como **PublicApi Facturacion** y tienen el prefijo:

`/api/public-api/institution/refset/{refsetCode}`

Rol `API_FACTURACION` asignado en la [institución](../hospital-api/src/main/java/net/pladema/establishment/repository/entity/Institution.java) con el código sisa dado (`refsetCode`).

## Turnos

Permite obtener datos maestros para el pedido de la reserva, realizar reservas de turnos a usuarios temporales y obtener los turnos en una institución para un paciente dado su dni.

En swagger los endpoints están agrupados como **PublicApi Turnos** y tienen los prefijos:

`/api/public-api/institution/{institutionId}/appointment`
`/api/public-api/institution/{institutionId}/appointment/booking`
`/api/public-api/institution/{institutionId}/appointment/booking/professional`
`/api/public-api/appointment/booking`

Rol `API_TURNOS` asignado en la [institución](../hospital-api/src/main/java/net/pladema/establishment/repository/entity/Institution.java). En los endpoints donde no se requiere el `institutionId`, solo se chequea que el usuario tenga asignado el rol `API_TURNOS` en cualquier institución.

## Pacientes

Permite crear pacientes en el sistema, cargar historia clinica externa y cargar encuentros de pacientes en una institución.

En swagger los endpoints están agrupados como **PublicApi Pacientes** y tienen los prefijos:

`/api/public-api/patient`
`/api/public-api/external-clinical-history`
`/api/public-api/patient/{externalId}/institution/{institutionId}/external-encounters`

Rol `API_PACIENTES` asignado a nivel global.

## Sip+

Integración con el SISTEMA INFORMÁTICO PERINATAL.

En swagger los endpoints están agrupados como **PublicApi Sip** y tienen el prefijo:

`/api/public-api/sip-plus`

Rol `API_SIPPLUS` asignado a nivel global.

## Usuarios

Permite obtener, mediante un token de sesión enviado como header, la información del usuario y sus roles. Útil para compartir la autentificación con otros sistemas, especialmente [Extensiones](./extensiones.md).

En swagger los endpoints están agrupados como **PublicApi Usuarios** y tienen el prefijo:

`/api/public-api/user`


Rol `API_USERS` asignado a nivel global.

## Recetas

> En swagger los endpoints están agrupados como **PublicApi Recetas**

> Paquete Java [prescription en hospital-public-api](../hospital-public-api/src/main/java/ar/lamansys/sgh/publicapi/prescription).

Usado principalmente desde los sistemas de las farmacias para validar y dispensar Receta Digital. Personas responsables de esos sitemas deberán tener el rol "API Recetas".

### [PrescriptionRequest](../hospital-public-api/src/main/java/ar/lamansys/sgh/publicapi/prescription/infrastructure/input/rest/PrescriptionAccessController.java)

Permite obtener la Receta Digital a partir de su ID y el DNI.

`GET /api/public-api/prescriptions/prescription/{prescriptionId}/identification/{identificationNumber}`

Posibles errores:
1. BadPrescriptionIdFormatException `BAD_REQUEST`: `code` "bad-id", `text` "El id de receta no tiene el formato correcto."
2. PrescriptionNotFoundException `NOT_FOUND`: `code` "not-found", `text` "No se encontró información sobre ese dni o id de receta"
3. PrescriptionRequestException `BAD_REQUEST`: `code` "request-error", `text` "No se encontró la receta."

### [PrescriptionDispense](../hospital-public-api/src/main/java/ar/lamansys/sgh/publicapi/prescription/infrastructure/input/rest/PrescriptionAccessController.java)

Permite marcar un renglón de la Receta Digital como dispensado.

`POST /api/public-api/prescriptions/prescription/{prescriptionId}/identification/{identificationNumber}`

Posibles errores:
1. BadPrescriptionIdFormatException `BAD_REQUEST`: `code` "bad-id", `text` "El id de receta no tiene el formato correcto."
2. PrescriptionIdMatchException `BAD_REQUEST`: `code` "prescription-id-match", `text` "El identificador de receta no coincide con los de los renglones."
3. PrescriptionDispenseException `BAD_REQUEST`: `code` "dispense-error", `text` "Error dispensando"

## Red de Imágenes

> En swagger los endpoints están agrupados como **PublicApi ImageNetwork** 

> Paquete Java [imagenetwork en hospital-public-api](../hospital-public-api/src/main/java/ar/lamansys/sgh/publicapi/imagenetwork).

Usado principalmente desde servidores PACS. Personas responsables de servidores PACS deberán tener el rol "API Red de Imágenes".

🪪 Roles autorizados en [AppImageNetworkPublicApiPermissions](../app/src/main/java/ar/lamansys/sgh/publicapi/imagenetwork/infrastructure/input/service/AppImageNetworkPublicApiPermissions.java).

### [CheckStudyToken](../hospital-public-api/src/main/java/ar/lamansys/sgh/publicapi/imagenetwork/infrastructure/input/rest/CheckStudyTokenController.java)

Permite verificar los accesos de profesionales a los estudios:

`GET /api/public-api/imagenetwork/{studyInstanceUID}/permission/check`

Posibles errores:
1. BadStudyTokenException `BAD_REQUEST`: `code` "bad-token", `text` "{{explicación}}"

## Centro de Imágenes

> En swagger los endpoints están agrupados como **PublicApi ImageCenter** 

> Paquete Java [imagecenter en hospital-public-api](../hospital-public-api/src/main/java/ar/lamansys/sgh/publicapi/imagecenter).

Usado principalmente desde servidores PACS. Personas responsables de servidores PACS deberán tener el rol "API Centro de Imágenes".

🪪 Roles autorizados en [AppImageCenterPublicApiPermissions](../app/src/main/java/ar/lamansys/sgh/publicapi/imagecenter/infrastructure/input/service/AppImageCenterPublicApiPermissions.java).

### [UpdateResult](../hospital-public-api/src/main/java/ar/lamansys/sgh/publicapi/imagecenter/infrastructure/input/rest/OrchestratorController.java)

Permite modificar el resultado y el estado de un estudio.

`POST /api/public-api/orchestrator/update-result`

Posibles errores:
1. UpdateResultException `BAD_REQUEST`: `code` "update-result-failed", `text` "{{explicación}}"

### [UpdateSize](../hospital-public-api/src/main/java/ar/lamansys/sgh/publicapi/imagecenter/infrastructure/input/rest/OrchestratorController.java)

Permite modificar el tamaño y el `imageId`.

`POST /api/public-api/orchestrator/set-size-study`

Posibles errores:
1. UpdateSizeException `BAD_REQUEST`: `code` "update-size-failed", `text` "{{explicación}}"

