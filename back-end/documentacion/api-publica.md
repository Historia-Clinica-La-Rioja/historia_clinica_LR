# ### ![logo](../../front-end/apps/projects/hospital/src/assets/custom/icons/icon-72x72.png) Back-end | API Pública

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

## Recetas

Permite validar y dispensar recetas.

En swagger los endpoints están agrupados como **PublicApi Recetas** y tienen el prefijo:

`/api/public-api/prescriptions/prescription`

Rol `API_RECETAS` asignado a nivel global.

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

