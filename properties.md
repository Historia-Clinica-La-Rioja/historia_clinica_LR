# ![logo](front-end/apps/projects/hospital/src/assets/custom/icons/icon-72x72.png) Propiedades de ambiente

Este documento detalla las propiedades configurables del sistema.

### Referencias

| Carácter | Interpretación |
| -	| --- |
| Opcional | Propiedad configurada con un valor por defecto que puede modificarse externamente.	|
| Único | Propiedad con valor único ya provisto por la aplicación. No debe modificarse.	|
| **Único** | Propiedad con valor fijo provisto por la aplicación según cada perfil. Es decir, al activar un perfil determinado, estas propiedades no podrán modificarse externamente.	|
| **Obligatorio** |	Propiedad que debe configurarse ***SIEMPRE*** en un ambiente productivo. |


## Configuración de cada nodo

| Propiedad               | Variable de ambiente       | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
| admin.password | ADMIN_PASS | admin123 | **Obligatorio** | Contraseña del usuario Administrador |  v0.2.0  |
| spring.profiles.active  |   | default  | **Único**  | Valores posibles: dev, qa, prod   | v0.2.0  |
| app.env.domain  |  APP_DOMAIN | localhost:4200  | **Opcional**  | Define el dominio. En caso de tener activo el FF HABILITAR_NOTIFICACIONES_TURNOS debe ser **obligatoria**   | v1.42.0  |

# Config de login / auth / token 

| Propiedad               | Variable de ambiente | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
| token.secret | TOKEN_SECRET |  | **Obligatorio** | La clave secreta de generación de token, usada para validar los tokens recibidos desde los request. | v0.2.0 |
| app.auth.domain | AUTH_DOMAIN | localhost | **Obligatorio** | Define el dominio a autenticar | v0.40.0 |
| app.auth.cookie.secure | AUTH_SECURE | false | **Obligatorio** | Activar en los ambientes productivos junto al uso de HTTPS | v0.40.0 |
## Configuración DDBB (SQL)

| Propiedad | Variable de ambiente | Valor por defecto | Necesidad | Descripcion                                                                                | Desde |
| ---- | ---- | ---- | ------ |--------------------------------------------------------------------------------------------| ---- |
| spring.datasource.url | DATABASE_IP_PORT y DATABASE_SCHEMA | [JDBC](back-end/app/src/main/resources/application.properties#L63) | **Obligatorio** | Url de conexión a la base de datos                                                         | v0.2.0 |
| spring.datasource.username | DATABASE_USER | postgres | **Obligatorio** | Nombre de usuario utilizado para realizar la conexión a la base de datos                   | v0.2.0 |
| spring.datasource.password | DATABASE_PASS | Local123 | **Obligatorio** | Contraseña utilizada para realizar la conexión a la base de datos                          | v0.2.0  |
| spring.datasource.hikari.maximumPoolSize  | HICKARI_MAXIMUM_POOL_SIZE  |  4 | Opcional  | Tamaño del pool de conexiones abiertas que estan en la escucha de request | v0.2.0  |
| spring.jpa.database-platform |  | [Dialect](back-end/app/src/main/resources/application.properties#L66) | Opcional | Nombre de la clase que representa el dialecto del motor de base de datos a utilizar        |  v0.2.0 |

## Configuración de Mail

| Propiedad | Variable de ambiente | Valor por defecto | Necesidad | Descripcion | Desde |
| ------ | ---- | ---- | ---- | ------------ | ----- |
|spring.mail.host | SMTP_HOST | | Opcional | Dirección del servidor de mails | v0.38.0 |
|spring.mail.port | SMTP_PORT | | Opcional | Puerto del servidor de mails | v0.38.0 |
|spring.mail.username | SMTP_USERNAME | | Opcional | Usuario del servidor de mails | v0.38.0 |
|spring.mail.password| SMTP_PASS | | Opcional | Password del servidor de mails | v0.38.0 |
|spring.mail.properties.mail.smtp.auth | SMTP_AUTH | true | Opcional | Habilita autentificación |  v0.38.0 |
|spring.mail.properties.mail.transport.protocol | | smtp | Opcional | Protocolo del servidor de mails | v0.38.0 |
|spring.mail.properties.mail.smtp.ssl.enable | SMTP_SSL | true | Opcional | Habilita el envío de mails |v0.38.0 |
|spring.mail.properties.mail.smtp.starttls.required | SMTP_SSL | true | Opcional |  |  v0.38.0 |
|spring.mail.properties.mail.smtp.starttls.enable | SMTP_SSL | true | Opcional | Habilita el envío de mails |v0.38.0 |
| app.notification.mail.from  | | hsi@pladema.net  | Opcional  |   | v0.38.0  |
| app.notification.mail.fromFullname  | | HSI  | Opcional  |   | v0.38.0  |
| app.notification.mail.replyTo  | | no-reply@pladema.net  | Opcional  |  Define la dirección de mail a responder | v1.41.0  |


## Configuración util para debug y monitoring 

| Propiedad	| Variable de ambiente       | Valor por defecto       | Necesidad | Descripcion | Desde |
| ------------ | -------- | ------ | --------- | ---------- | ---- |
| logging.level.net.pladema.sgx.restclient  |   | DEBUG  | Opcional  | Define el nivel de log del paquete net.pladema.sgx.restclient | v0.2.0  |
| logging.level.net.pladema.federar  |   |  DEBUG | Opcional  |  Define el nivel de log del paquete net.pladema.federar | v0.2.0   |
| management.endpoint.metrics  |   | false  | Opcional  | Habilita metricas del servidor  | v0.2.0  |
| management.endpoints.web.exposure.include | ACTUATOR_ENABLED_ENDPOINTS  | Define que endpoints de la libreria actuator se pueden consultar  | Opcional  |   | v0.2.0  |
| management.endpoints.jmx.exposure.include | ACTUATOR_ENABLED_ENDPOINTS  | Define que endpoints de la libreria actuator se pueden consultar  | Opcional  |   | v0.2.0  |
| management.endpoint.prometheus.enabled  |  | false | Opcional |  Define si esta habilitado los endpoints de prometheus (para monitoreo de la aplicación) |  v0.2.0  |
| management.metrics.export.prometheus.enabled  |   | false  | Opcional  | Define si esta habilitado los endpoints de prometheus (para monitoreo de la aplicación) | v0.2.0  |
| management.health.mail.enabled  |   |  false | Opcional  |  Habilita información sobre el estado de tu sistema | v0.2.0  |
| management.endpoint.health.show-details  |   |   | Opcional  |  Habilita  información detallada sobre el estado de tu sistema | v0.2.0  |
| management.info.git.mode |   | full  | Opcional  |  Incorpora información de git relacionada al sistema | v0.2.0  |
| actuator.configuration.whitelist | ACTUATOR_WHITELIST  | 0:0:0:0:0:0:0:1  | Opcional  |  Define la lista de ips habilitada a acceder a los endpoints de la libreria actuator | v0.2.0  |
| actuator.configuration.authenticated | ACTUATOR_REQUIRED_AUTHENTICATION  | false  | Opcional  |  Define si se requiere autenticación para acceder a los endpoints de la libreria actuator | v0.2.0  |

## Configuración recaptcha 

| Propiedad               | Variable de ambiente | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
|google.recaptcha.enable  | RECAPTCHA_ENABLE  | true | **Único** |  Flag para habilitar / deshabilitar la funcionalidad de recaptcha en la aplicación. | v0.2.0  |
|google.recaptcha.secret.key | RECAPTCHA_SECRET_KEY |  | **Obligatorio** | Clave secreta correspondiente al de la consola administrativa. | v0.2.0 |
|google.recaptcha.site.key | RECAPTCHA_PUBLIC_KEY |  | **Obligatorio** | Clave publica correspondiente al de la consola administrativa. | v0.2.0 |
|google.recaptcha.validator.url |   | [Site Verify](https://www.google.com/recaptcha/api/siteverify) | Opcional | Url de google para validar el recaptcha. No debería cambiar. | v0.2.0 |

## Habilitación de nueva consulta

| Propiedad     | Variable de ambiente | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
| habilitar.boton.consulta | | false | Opcional | Propiedad de configuración de aplicación para des/habilitar el botón Nueva consulta.| v1.3.0 |

## Integración con servicios de terceros (Renaper, Federar, Snowstorm, Nomivac ... )

### Conexiones HTTP salientes
| Propiedad | Variable de ambiente | Valor por defecto | Condición | Descripcion | Desde |
| -------- | ------ | -------- | ------ | -------------- | ---- |
| app.http.client.trustInvalidCertificate | | false | Opcional | Ignora certificados inválidos (habilitar esta propiedad sólo en casos extremos)| v1.40.0  |
| app.http.client.timeout | | 15000 | Opcional | Timeout en milisegundos| v1.40.0  |
| app.http.client.proxy | | | Opcional | Host del proxy (ejemplo: http://127.0.0.1:3128) | v1.40.0  |

### Bus de interoperabilidad
| Propiedad | Variable de ambiente | Valor por defecto | Condición | Descripcion | Desde |
| -------- | ------ | -------- | ------ | -------------- | ---- |
| ws.bus.url.base   | | - | **Obligatorio** | URL productiva del BUS de interoperabilidad utilizada al solicitar historia clínica de pacientes federados en otros dominios.| v1.21.0  |


### Renaper
| Propiedad | Variable de ambiente | Valor por defecto | Condición | Descripcion | Desde |
| -------- | ------ | -------- | ------ | -------------- | ---- |
| ws.renaper.enabled   | | - | **Único** | Determina si se utiliza la integracion con Renaper (se necesita completar la configuracion) | v0.2.0  |
| ws.renaper.nombre |  | - | **Obligatorio**  | Nombre que provee Renaper para cada dominio que utilice la integracion | v0.2.0  |
| ws.renaper.clave |  | - | **Obligatorio** | Clave que provee Renaper para cada dominio que utilice la integracion | v0.2.0  |


### Federar
| Propiedad | Variable de ambiente | Valor por defecto                | Condición | Descripcion                                                                                           | Desde   |
| ---------- | ------ |----------------------------------| -------- |-------------------------------------------------------------------------------------------------------|---------|
| ws.federar.enabled | | -                                | **Único** | Determina si se utiliza la integracion con Federar (se necesita completar la configuración)           | v0.2.0  |
| ws.federar.url.base | | -                                | **Único** | URL de autenticación del Federador                                                                    | v0.2.0  |
| ws.federar.sisaCode |	| 10002001110000                   | **Obligatorio** | 	Código SISA para el dominio                                                                          | v1.20.0 |
| ws.federar.claims.iss  |  | http://www.msal.gov.ar           | **Obligatorio** | URI del dominio (registrada previamente ante la DNGISS)                                               | v0.2.0  |
| ws.federar.claims.sub  |  | Ministerio de Salud de la Nación | **Obligatorio** | Nombre del dominio                                                                                    | v0.2.0  |
| ws.federar.claims.aud  |  | -                                | **Único** | URL de autenticación usada en la generación del token JWT **[NO MODIFICAR]**                          | v0.2.0  |
| ws.federar.claims.name  |  | Prueba Jose                      | **Obligatorio** | Apellido y Nombres del Usuario que accede (no es necesario que hayan sido registrados ante la DNGISS) | v0.2.0  |
| ws.federar.claims.role |  | Project Manager                  | **Obligatorio** | Especialidad del Usuario (no es necesario que hayan sido registrados ante la DNGISS)                  | v0.2.0  |
| ws.federar.claims.ident  |  | 0001                             | **Obligatorio** | Un identificador para el usuario (no es necesario que hayan sido registrados ante la DNGISS)          | v0.2.0  |
| ws.federar.auth.signKey |   | federar ***[TESTING]***          | **Obligatorio** | A cada dominio se le asignará una palabra secreta única y cifrada por la DNGISS.                      | v0.2.0  |
| ws.federar.requestTimeOut |   | 5000                             | Opcional | Determina el tiempo que se esperará la respuesta del lado del servicio al ejecutar un request         | v1.33.1 |
| ws.federar.token.expiration |   | 10s                              | Opcional | Determina el tiempo de vencimiento del token usado en la comunicación (login, federar)                | v1.46.0 |

### Snowstorm
| Propiedad | Variable de ambiente | Valor por defecto | Condición | Descripcion | Desde |
| ---------- | ------ | -------- | -------- | ------------ | ---- |
| ws.snowstorm.url.base |   | https://snowstorm.msal.gob.ar | **Obligatorio** |  URL base donde se van a consumir los servicios de Snowstorm  | v1.2.0  |
| ws.snowstorm.appKey |   | - | **Obligatorio** |  Key de la aplicación utilizada para autorización de request  | v1.2.0  |
| ws.snowstorm.appId |   | - | **Obligatorio** |  Id de la aplicación utilizada para autorización de request  | v1.2.0  |
| ws.snowstorm.params.preferredOrAcceptableIn |   | 450828004 | Único |  Parametros para consulta a servicio Conceptos que indica que la descripción debe ser preferida o aceptable en al menos uno de estos parametros  | v1.2.0  |
| ws.snowstorm.params.limit |   | 30 | Único |  Parametro para consulta a servicio Conceptos que indica limite de resultados  | v1.2.0  |
| ws.snowstorm.params.termActive |   | true | Único |  Parametro para consulta a servicio Conceptos que indica si el termino a buscar debe estar activo  | v1.2.0  |
| ws.snowstorm.auth.language |   | es-AR;q=0.8,en-GB;q=0.6 | Único |  Header que indica el lenguaje de los resultados  | v1.2.0  |
| ws.snowstorm.url.concepts |   | /MAIN/concepts | Único |  URL relativa para consumir el servicio de Conceptos a buscar  | v1.2.0  |

#### Snomed Ecls
| Propiedad                                  | Variable de ambiente | Valor por defecto | Condición | Descripcion | Desde |
|--------------------------------------------|----------------------| -------- | -------- | ------------ | ---- |
| snomed-semantics.diagnosis.ecl             |                      | << 404684003 &#124;hallazgo clínico (hallazgo)&#124; OR << 243796009 &#124;situación con contexto explícito (situación)&#124; OR << 272379006 &#124; evento (evento)&#124; | Opcional |  Ecl para búsqueda de diagnósticos | v1.27.0  |
| snomed-semantics.bloodType.ecl             |                      | < 112143006 &#124;ABO group phenotype (finding)&#124; | Opcional |  Ecl para búsqueda de tipos de sangre | v1.27.0  |
| snomed-semantics.personalRecord.ecl        |                      | << 404684003 &#124;hallazgo clínico (hallazgo)&#124; OR <<  243796009 &#124;situación con contexto explícito (situación)&#124; OR << 272379006 &#124; evento (evento)&#124; | Opcional |  Ecl para búsqueda de antecedentes personales | v1.27.0  |
| snomed-semantics.familyRecord.ecl          |                      | << 404684003 &#124;hallazgo clínico (hallazgo)&#124; OR <<  243796009 &#124;situación con contexto explícito (situación)&#124; OR << 272379006 &#124; evento (evento)&#124; | Opcional |  Ecl para búsqueda de antecedentes familiares | v1.27.0  |
| snomed-semantics.allergy.ecl               |                      | < 609328004 &#124;disposición alérgica (hallazgo)&#124; | Opcional |  Ecl para búsqueda de alergias | v1.27.0  |
| snomed-semantics.hospitalizationReason.ecl |                      | < 404684003 &#124;hallazgo clínico (hallazgo)&#124; OR 272379006 &#124; evento (evento)&#124; OR 243796009 &#124;situación con contexto explícito (situación)&#124; OR 48176007 &#124;contexto social&#124; | Opcional |  Ecl para búsqueda de motivos de internación | v1.27.0  |
| snomed-semantics.vaccine.ecl               |                      | ^ 2281000221106 &#124;conjunto de referencias simples de inmunizaciones notificables (metadato fundacional)&#124; | Opcional |  Ecl para búsqueda de vacunas | v1.27.0  |
| snomed-semantics.medicine.ecl              |                      | < 763158003: 732943007 &#124;tiene base de sustancia de la potencia (atributo)&#124;=\*, [0..0] 774159003 &#124;tiene proveedor (atributo)&#124;=\* | Opcional |  Ecl para búsqueda de medicamentos | v1.27.0  |
| snomed-semantics.procedureGroup.ecl        |                      | < 71388002 &#124;procedimiento (procedimiento)&#124; | Opcional |  Ecl para búsqueda de procedimientos | v1.27.0  |
| snomed-semantics.consultationReason.ecl    |                      | << 404684003 &#124;hallazgo clínico (hallazgo)&#124; OR << 71388002 &#124;procedimiento (procedimiento)&#124; OR << 243796009 &#124;situación con contexto explícito (situación)&#124; OR << 272379006 &#124; evento (evento)&#124;  | Opcional |  Ecl para búsqueda de motivos de consulta | v1.27.0  |
| snomed-semantics.eventGroup.ecl            |                      | <272379006 &#124; evento (evento)&#124;  | Opcional |  Ecl para búsqueda de eventos | v1.54.0  |

#### Actualización de caché de Snomed
| Propiedad | Variable de ambiente | Valor por defecto | Condición | Descripcion | Desde |
| ---------- | ------ | -------- | -------- | ------------ | ---- |
| snomed-cache-update.batch-size |   | 1000 | Opcional |  Tamaño de batch de guardado de conceptos Snomed para la actualización de caché mediante archivo .CSV  | v1.38.3  |

### Nomivac sincronización de datos

| Propiedad | Variable de ambiente | Valor por defecto | Condición | Descripcion | Desde |
| ---------- | ------ | -------- | -------- | ------------ | ---- |
| ws.federar.enabled | - | - | **Obligatorio** | Determina si se utiliza la integracion con Federar (se necesita completar la configuración) | v0.2.0  |
| ws.nomivac.synchronization.url.base  | - | localhost | **Obligatorio** | URL del dominio donde se sincroniza las vacunas | v1.22.0  |
| ws.nomivac.synchronization.cron.config  | - | - | **Obligatorio** | Cron que determina la periodicidad con la que se envia los datos a nomivac | v1.22.0  |
| app.feature.HABILITAR_BUS_INTEROPERABILIDAD  | - | false | **Obligatorio** | Define si se va a realizar una comunicación con el bus de interoperabilidad | v1.22.0  |
| ws.nomivac.rest-client.config.trust-invalid-certificate  | - | false | **Opcional** | Propiedad para ignorar certificados https vencidos | v1.29.0  |


### Sisa reporte epimediológico (snvs)

La funcionalidad para reporte epimediológico se activa solamente si el feature flag HABILITAR_REPORTE_EPIDEMIOLOGICO se encuentra activo.

| Propiedad | Variable de ambiente | Valor por defecto | Condición | Descripcion | Desde |
| ---------- | ------ | -------- | -------- | ------------ | ---- |
| ws.sisa.snvs.enabled | - | false | **Obligatorio** | Determina sí esta activa la funcionalidad para hacer reportes epidmediológicos  | v1.28.0  |
| ws.sisa.snvs.url.base | - | https://ws400-qa.sisa.msal.gov.ar | **Obligatorio** | Determina el dominio donde se encuentran el servicio de reporte epidmediológico  | v1.28.0  |
| ws.sisa.snvs.appId.value  | - | PruebasWSQA_SNVS_ID | **Obligatorio** | Id de identificación para consulta de servicio | v1.28.0  |
| ws.sisa.snvs.appKey.value  | - | PruebasWSQA_SNVS_KEY | **Obligatorio** | Key para la consulta del servicio | v1.28.0  |
| ws.sisa.snvs.environment  | - | QA | **Obligatorio** | Define el set de datos a utilizar para el evento, grupoevento, clasificación manual. Los valores posibles son (QA, PROD) | v1.28.0  |
| ws.sisa.snvs.rest-client.config.trust-invalid-certificate  | - | false | **Opcional** | Define si valida certificado https | v1.29.0  |

### OAuth2
| Propiedad | Variable de ambiente | Valor por defecto | Condición | Descripción | Desde |
| ---------- | ------ | -------- | -------- | ------------ | ---- |
| ws.oauth.enabled | | false | Opcional | Determina si se utiliza la autenticación en un servidor de OAuth externo (se necesita completar la configuración) | v1.32.0  |
| ws.oauth.url.base | | - | Opcional | URL base del servidor de OAuth  | v1.32.0 |
| ws.oauth.url.issuer |	| - | Opcional |	URL del issuer para obtener los *access tokens* de OAuth | v1.32.0 |
| ws.oauth.realm  |  | - | Opcional | Nombre del *realm* donde se van a estar almacenando los usuarios del sistema | v1.32.0  |
| ws.oauth.client-id  |  | - | Opcional | *Client-id* del cliente público en el *realm* | v1.32.0  |
| ws.oauth.token-expiration  |  | 1800 | Opcional | Tiempo de expiración en segundos de los *access tokens* | v1.34.0  |
| ws.oauth.user-admin.username  |  | - | Opcional | Username del usuario con rol **realm-admin** en el *realm* | v1.32.0  |
| ws.oauth.user-admin.password  |  | - | Opcional | Contraseña del usuario con rol **realm-admin** en el *realm* | v1.32.0  |
| ws.oauth.url.userinfo  |  | /auth/realms/REALM_NAME/protocol/openid-connect/userinfo | Opcional | Ruta relativa de obtención de datos del usuario. No debería cambiar | v1.32.0  |
| ws.oauth.url.accesstoken  |  | /auth/realms/REALM_NAME/protocol/openid-connect/token | Opcional | Ruta relativa de obtención de *access tokens*. No debería cambiar | v1.32.0  |
| ws.oauth.url.createuser  |  | /auth/admin/realms/REALM_NAME/users | Opcional | Ruta relativa de obtención/creación/modificación de datos de usuario. No debería cambiar | v1.32.0  |

### CLAP - Sistema informático perinatal (SIP)
| Propiedad                    | Variable de ambiente | Valor por defecto                                        | Condición | Descripción                                                                          | Desde   |
|------------------------------| ------ |----------------------------------------------------------| -------- |--------------------------------------------------------------------------------------|---------|
| ws.sip.plus.url.base         | | -                                                        | Opcional | Dominio desde el cual se van a consumir los servicios de SIP                         | v1.53.0 |
| ws.sip.plus.embed-system     | | -                                                        | Opcional | Nombre que se le asocia a hsi como recurso embebedor dentro del archivo .conf de SIP | v1.53.0 |
| ws.sip.plus.username         |	| -                                                        | Opcional | Usuario provisto por SIP para consumir sus recursos                                  | v1.53.0 |
| ws.sip.plus.password         |  | -                                                        | Opcional | Contraseña provista por SIP para consumir sus recursos                               | v1.53.0 |

### CIPRES
| Propiedad                    | Variable de ambiente | Valor por defecto                                        | Condición | Descripción                                                      | Desde   |
|------------------------------| ------ |----------------------------------------------------------| -------- |------------------------------------------------------------------|---------|
| ws.cipres.url.base           | | -                                                        | Opcional | URL donde se encuentran alojados los recursos de CIPRES          | v1.57.0 |
| ws.cipres.username           | | -                                                        | Opcional | Nombre de usuario otorgado para acceder a los recursos de CIPRES | v1.57.0 |
| ws.cipres.password           |	| -                                                        | Opcional | Contraseña otorgada para acceder a los recursos de CIPRES        | v1.57.0 |
| ws.cipres.realusername       |  | -                                                        | Opcional | Nombre real de usuario para acceder a los recursos de CIPRES     | v1.57.0 |

### Plataforma de Firma Digital Remota
| Propiedad | Variable de ambiente | Valor por defecto                       | Condición       | Descripcion                                                                                                                                                         | Desde  |
| -------- | ------ |-----------------------------------------|-----------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------|
| app.feature.HABILITAR_FIRMA_DIGITAL   | | false                                   | **Obligatorio** | Define si se va a habilitar el módulo de Firma digital                                                                                                              | v2.2.0 |
| digital.signature.url.redirect | APP_DOMAIN | localhost:4200/firma-digital/documentos | **Único**       | Url necesaria para enviarle al servicio del firmador. Dicho servicio redirige al usuario desde su sistema a la url una vez firmado un documento. **[NO MODIFICAR]** | v2.2.0 |
| ws.firmador.url.base |  | -                                       | Opcional | Url del servicio externo del firmador. **Obligatoria** si el FF de HABILITR_FIRMA_DIGITAL esta habilitado                                                           | v2.2.0 |
| ws.firmador.api.key |  | -                                       | Opcional | Clave que provee el firmador. **Obligatoria** si el FF de HABILITR_FIRMA_DIGITAL esta habilitado                                                                    | v2.2.0 |
| ws.firmador.api.token |  | -                                       | Opcional | Token que provee el firmador. **Obligatoria** si el FF de HABILITR_FIRMA_DIGITAL esta habilitado                                                                    | v2.2.0 |
| ws.firmador.path.login |  | -                                       | Opcional | Path que provee el firmador para autenticarse al mismo. **Obligatoria** si el FF de HABILITR_FIRMA_DIGITAL esta habilitado                                          | v2.2.0 |
| ws.firmador.path.multiple.sign |  | -                                       | Opcional | Path que provee el firmador para firmar multiples documentos. **Obligatoria** si el FF de HABILITR_FIRMA_DIGITAL esta habilitado                                    | v2.2.0 |


### Addons: Prefacturación (billing)
| Propiedad                    | Variable de ambiente | Valor por defecto | Condición | Descripción | Desde   |
|------------------------------| ------ |----------------------------------------------------------| -------- |------------------------------------------------------------------|---------|
| ws.addons.billing.enabled| | false | Opcional | Indica si se debe solicitar información de prefacturación al servidor Addons. | v2.17.0 |
| ws.addons.billing.url| | - | Obligatorio si enabled=true | Url base donde se encuentra el servidor Addons. Ejemplo: https://addons.prefacturacion-soma.dev-env.lamansys.ar/api/addons/external | v2.17.0 |
| ws.addons.billing.appKey| | - | Obligatorio si enabled=true | App key para poder acceder a los endpoints de Addons. | v2.17.0 |
| ws.addons.billing.appKeyHeader| | external-token | Obligatorio si enabled=true | Nombre del header donde se envía el app key. | v2.17.0 |
| ws.addons.billing.encounterUrl| | encounter | Obligatorio si enabled=true | Sub-path donde se encuentra el servicio de prefacturación. La url completa se arma como url + '/' + encounterUrl. | v2.17.0 |


## Integración con sistemas relacionados
| Propiedad | Variable de ambiente   | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
| integration.covid.encryption.key | | WillGriggIsOnFir | Opcional | Clave para la encriptación de datos provistos por endpoints de SGH a otras aplicaciones (ej: covid) | v1.9.0 |


## Propiedades específicas de flavors 


## Pruebas de estrés

Se crearon las siguientes propiedades para ser usado en las pruebas de estrés.

| Propiedad | Variable de ambiente  | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
|test.stress.disable.validation   |   | false  | Opcional  | Desactiva validaciones en el sistema para facilitar las pruebas  | v1.2.0   |


## Configuración de Tableros

Se crearon las siguientes propiedades para configurar los tableros de la aplicación

| Propiedad | Variable de ambiente  | Valor por defecto | Necesidad   | Descripcion                                                                                                                                                          | Desde   |
| ----------------------- | ----------------|-------------------|-------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
|app.gateway.cubejs.apiUrl   | CUBEJS_API_URL  |                   | Obligatoria | Define la url donde se encuentra el contenedor de cubejs. Por ejemplo, http://localhost:4000/cubejs-api/v1                                                           | v1.43.0 |
|app.gateway.cubejs.token.secret   | CUBEJS_API_SECRET  | | Obligatoria | Define la clave de encriptación del token usado en la comunicación backend-cubejs. Este valor debe coincidir con la variable de ambiente CUBEJS_API_SECRET de cubejs | v1.43.0 |


## Scheduled Jobs
| Propiedad                                                  | Variable de ambiente | Valor por defecto | Necesidad | Descripcion                                                                                                                                        | Desde   |
|------------------------------------------------------------| ----------------|------------------| --------- |----------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| scheduledjobs.enabled                                      |   | true             | Opcional  | Des/habilitar la ejecución general de trabajos automáticos                                                                                         | v1.13.0 |
| scheduledjobs.federatepatients.enabled                     |   | true             | Opcional  | Des/habilitar la federación de pacientes en estado validado                                                                                        | v1.13.0 |
| scheduledjobs.federatepatients.seconds                     |   | 0                | Opcional | Configura los segundos del cron usado para la federación de pacientes.                                                                             | v1.13.0 |
| scheduledjobs.federatepatients.minutes                     |   | 0                | Opcional | Configura los minutos del cron usado para la federación de pacientes.                                                                              | v1.13.0 |
| scheduledjobs.federatepatients.hours                       |   | 3                | Opcional | Configura las horas del cron usado para la federación de pacientes.                                                                                | v1.13.0 |
| scheduledjobs.federatepatients.dayofmonth                  |  | *                | Opcional | Configura el dia del mes del cron usado para la federación de pacientes.                                                                           | v1.13.0 |
| scheduledjobs.federatepatients.month                       |   | *                | Opcional | Configura el mes del cron usado para la federación de pacientes.                                                                                   | v1.13.0 |
| scheduledjobs.federatepatients.dayofweek                   |  | *                | Opcional | Configura el dia de la semana del cron usado para la federación de pacientes.                                                                      | v1.13.0 |
| scheduledjobs.updateethnicities.enabled                    |   | true             | Opcional  | Des/habilitar la actualización de etnias desde el servicio de Snowstorm                                                                            | v1.15.0 |
| scheduledjobs.updateethnicities.seconds                    |   | 0                | Opcional  | Configura los segundos del cron usado para la actualización de las etnias.                                                                         | v1.15.0 |
| scheduledjobs.updateethnicities.minutes                    |   | 0                | Opcional  | Configura los minutos del cron usado para la actualización de las etnias.                                                                          | v1.15.0 |
| scheduledjobs.updateethnicities.hours                      |   | 0                | Opcional  | Configura las horas del cron usado para la actualización de las etnias.                                                                            | v1.15.0 |
| scheduledjobs.updateethnicities.dayofmonth                 |  | 15               | Opcional  | Configura el dia del mes del cron usado para la actualización de las etnias.                                                                       | v1.15.0 |
| scheduledjobs.updateethnicities.month                      |   | *                | Opcional | Configura el mes del cron usado para la actualización de las etnias.                                                                               | v1.15.0 |
| scheduledjobs.updateethnicities.dayofweek                  |  | *                | Opcional  | Configura el dia de la semana del cron usado para la actualización de las etnias.                                                                  | v1.15.0 |
| scheduledjobs.updatesnomedcache.enabled                    |   | false            | Opcional  | Des/habilitar la actualización de grupos de conceptos Snomed                                                                                       | v1.32.0 |
| scheduledjobs.updatesnomedcache.eclkeys                    |   | -                | Opcional  | Claves de los grupos de conceptos Snomed, separadas por comas (ej. _BLOOD_TYPE,FAMILY_RECORD_)                                                     | v1.32.0 |
| scheduledjobs.updatesnomedcache.seconds                    |   | -                | Opcional  | Configura los segundos del cron usado para la actualización de grupos de conceptos Snomed.                                                         | v1.32.0 |
| scheduledjobs.updatesnomedcache.minutes                    |   | -                | Opcional  | Configura los minutos del cron usado para la actualización de grupos de conceptos Snomed.                                                          | v1.32.0 |
| scheduledjobs.updatesnomedcache.hours                      |   | -                | Opcional  | Configura las horas del cron usado para la actualización de grupos de conceptos Snomed.                                                            | v1.32.0 |
| scheduledjobs.updatesnomedcache.dayofmonth                 |  | -                | Opcional  | Configura el día del mes del cron usado para la actualización de grupos de conceptos Snomed.                                                       | v1.32.0 |
| scheduledjobs.updatesnomedcache.month                      |   | -                | Opcional | Configura el mes del cron usado para la actualización de grupos de conceptos Snomed.                                                               | v1.32.0 |
| scheduledjobs.updatesnomedcache.dayofweek                  |  | -                | Opcional  | Configura el día de la semana del cron usado para la actualización de grupos de conceptos Snomed.                                                  | v1.32.0 |
| scheduledjobs.updateappointmentsstate.enabled              |   | true             | Opcional  | Des/habilitar la actualización de estado de turnos.                                                                                                | v1.44.2 |
| scheduledjobs.updateappointmentsstate.seconds              |   | 0                | Opcional  | Configura los segundos del cron usado para la actualización de estado de turnos.                                                                   | v1.44.2 |
| scheduledjobs.updateappointmentsstate.minutes              |   | 0                | Opcional  | Configura los minutos del cron usado para la actualización de estado de turnos.                                                                    | v1.44.2 |
| scheduledjobs.updateappointmentsstate.hours                |   | 0                | Opcional  | Configura las horas del cron usado para la actualización de estado de turnos.                                                                      | v1.44.2 |
| scheduledjobs.updateappointmentsstate.dayofmonth           |  | *                | Opcional  | Configura el dia del mes del cron usado para la actualización de estado de turnos.                                                                 | v1.44.2 |
| scheduledjobs.updateappointmentsstate.month                |   | *                | Opcional | Configura el mes del cron usado para la actualización de estado de turnos.                                                                         | v1.44.2 |
| scheduledjobs.updateappointmentsstate.dayofweek            |  | *                | Opcional  | Configura el dia de la semana del cron usado para la actualización de estado de turnos.                                                            | v1.44.2 |
| scheduledjobs.updateappointmentsstate.hourssincelastchange |  | 24               | Opcional  | Configura la mínima cantidad pasados desde el momento de la ejecución respecto a la fecha correspondiente al turno.                                | v1.44.2 |
| scheduledjobs.updateappointmentsstate.limit                |  | 10               | Opcional  | Configura la máxima cantidad de turnos seleccionados para actualizar.                                                                              | v1.44.2 |
| scheduledjobs.cipresconsultations.enabled                 |   | false            | Opcional  | Des/habilita la creación de consultas en cipres.                                                                                                   | v1.57.0 |
| scheduledjobs.cipresconsultations.seconds                 |   | 0                | Opcional  | Configura los segundos del cron utilizado para la creación de consultas en cipres.                                                                 | v1.57.0 |
| scheduledjobs.cipresconsultations.minutes                 |  | 5                | Opcional  | Configura los minutos del cron utilizado para la creación de consultas en cipres.                                                                  | v1.57.0 |
| scheduledjobs.cipresconsultations.hours                   |   | 0-5              | Opcional  | Configura las horas del cron utilizado para la creación de consultas en cipres.                                                                    | v1.57.0 |
| scheduledjobs.cipresconsultations.dayofmonth              |  | *                | Opcional  | Configura el dia del mes del cron utilizado para la creación de consultas en cipres.                                                               | v1.57.0 |
| scheduledjobs.cipresconsultations.month                   |   | *                | Opcional | Configura el mes del cron utilizado para la creación de consultas en cipres.                                                                       | v1.57.0 |
| scheduledjobs.cipresconsultations.dayofweek               |  | *                | Opcional  | Configura el dia de la semana del cron utilizado para la creación de consultas en cipres.                                                          | v1.57.0 |
| scheduledjobs.updatepatienttype.enabled                    |   | true             | Opcional  | Des/habilitar la actualización de estado de pacientes.                                                                                             | v2.4.0  |
| scheduledjobs.updatepatienttype.cron                       |   | 0 20 0 * * *                 | Opcional  | Configura los segundos, minutos, horas, dia y mes del cron usado para la actualización de estado de pacientes.                                     | v2.4.0  |
| scheduledjobs.updatepatienttype.pastdays                   |  | 180              | Opcional  | Configura la mínima cantidad pasados desde el momento de la ejecución respecto a la fecha correspondiente al ultimo cambio de estado del paciente. | v2.4.0  |
| scheduledjobs.updatepatienttype.limit                      |  | 10               | Opcional  | Configura la máxima cantidad de pacientes seleccionados para actualizar.                                                                           | v2.4.0  |


## Monitoring

Se crearon las siguientes propiedades para el monitoreo del sistema

| Propiedad                             | Variable de ambiente | Valor por defecto | Necesidad | Descripcion                                                                                                                                             | Desde   |
|---------------------------------------|----------------------|-------------------| --------- |---------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| monitoring.rest-client.storage.enable | -                    | true              | Opcional  | Activa el almacenamiento de la información medida cuando se realizan request a servicios externos | v1.30.0 |




## Configuración de Tableros

Se crearon las siguientes propiedades para configurar los tableros de la aplicación

| Propiedad | Variable de ambiente          | Valor por defecto       | Necesidad   | Descripcion                                                                                                                                                                                                                                                                                                                                    | Desde   |
| ----------------------- |-------------------------------|-------------------------|-------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
|internment.document.directory | DOCUMENT_ROOT_DIRECTORY       | /temp                   | Obligatorio | Directorio donde se almacenan documentos clínicos, resultados de estudios y fotos de pacientes                                                                                                                                                                                                                                                 | v0.2.0  |
|app.files.folder.documents.location | DOCUMENT_ROOT_DIRECTORY       | java.io.tmpdir | Obligatorio | Directorio donde se almacenan documentos clínicos, resultados de estudios y fotos de pacientes. Toma el mismo valor que la propiedad internment.document.directory                                                                                                                                                                                               | v1.45.0 |
|app.files.folder.multipart.location   | MULTIPART_ABSOLUTE_LOCATION, MULTIPART_RELATIVE_LOCATION | /temp/tmp/multipartfiles | Obligatorio | Define donde se van a almancenar los archivos temporales cuando se suben archivos. Con la variable de ambiente MULTIPART_ABSOLUTE_LOCATION se puede definir una ubicación absoluta, mientras que con MULTIPART_RELATIVE_LOCATION se puede definir una ubicación relativa a la ubicación definida en la propiedad internment.document.directory | v1.45.0 |
|app.files.folder.freespace.minimum   | MULTIPART_ABSOLUTE_LOCATION, MULTIPART_RELATIVE_LOCATION | /temp/tmp/multipartfiles | Obligatorio | Define donde se van a almancenar los archivos temporales cuando se suben archivos. Con la variable de ambiente MULTIPART_ABSOLUTE_LOCATION se puede definir una ubicación absoluta, mientras que con MULTIPART_RELATIVE_LOCATION se puede definir una ubicación relativa a la ubicación definida en la propiedad internment.document.directory | v1.45.0 |

## Broker MQTT

Se crearon las siguientes propiedades para configurar la [funcionalidad de mqtt](back-end/sgx-mqtt/src/main/resources/sgx-mqtt.properties) de la aplicación:

| Propiedad                              | Variable de ambiente | Valor por defecto     | Necesidad | Descripcion                                                                                           | Desde   |
|----------------------------------------|----------------------|-----------------------|-----------|-------------------------------------------------------------------------------------------------------|---------|
| mqtt.client_connection | MQTT_CLIENT_CONNECTION | false                | Opcional | Activa el uso del Broker MQTT. | v2.1.0  |
| mqtt.server_address    | MQTT_SERVER_ADDRESS    | tcp://127.0.0.1:1883 | Opcional | Define la dirección del Broker MQTT. | v2.1.0  |
| mqtt.client_username   | MQTT_CLIENT_USERNAME   | -                    | Opcional | Usuario para la conexión | v2.1.0  |
| mqtt.client_password   | MQTT_CLIENT_PASSWORD   | -                    | Opcional | Clave para la conexión | v2.1.0  |


## Red de imágenes

Se crearon las siguientes propiedades para configurar la funcionalidad de red de imágenes de la aplicación

| Propiedad                              | Variable de ambiente | Valor por defecto     | Necesidad | Descripcion                                                                                           | Desde   |
|----------------------------------------|----------------------|-----------------------|-----------|-------------------------------------------------------------------------------------------------------|---------|
| app.imagenetwork.permission.expiration | -                    | 60m                   | Opcional  | Define cuanto tiempo de validez posee un token desde que se genera, para acceder a un estudio médico. | v2.1.0  |
| app.imagenetwork.viewer.web.url        | VIEWER_WEB_URL       | http://localhost:3000 | Opcional  | Establece la URL donde se encuentra el visualizador web definido para ver estudios médicos.           | v2.1.0  |

## Problemas en HC

| Propiedad                              | Variable de ambiente | Valor por defecto | Necesidad | Descripcion                                                                                                | Desde    |
|----------------------------------------|----------------------|-------------------|-----------|------------------------------------------------------------------------------------------------------------|----------|
| app.problems.set-incorrect.time-window | -                    | 24h               | Opcional  | Define la ventana de tiempo dentro de la cual un problema puede ser marcado como cargado por error (en hs) | v.2.11.0 |
