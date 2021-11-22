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
| server.port | SERVER_PORT | 8080 | Opcional |  |  v0.2.0  |
| api.domain | API_DOMAIN | /api | Opcional |  |  v0.2.0  |
| admin.mail | ADMIN_MAIL | admin@example.com | Opcional | Nombre de usuario del usuario Administrador |  v0.2.0  |
| admin.password | ADMIN_PASS | admin123 | **Obligatorio** | Contraseña del usuario Administrador |  v0.2.0  |
| app.flavor  |  | 'minsal' | **Obligatorio** | Código del sabor de SGH. Opciones disponibles: `minsal`, `tandil`, `chaco`, `pba` | v0.2.0 |
| app.default.language  | DEFAULT_LANGUAGE | 'es-AR' | Opcional | | v0.2.0 |
| app.other.languages  | OTHER_LANGUAGES | 'en-US' | Opcional | | v0.2.0 |
| internment.document.directory |DOCUMENT_ROOT_DIRECTORY | /temp | **Obligatorio** | Directorio donde se almacenan documentos clínicos y fotos de pacientes | v0.2.0 |
| frontend.loginpage |  LOGIN_PAGE  | / | Opcional  |   | v0.2.0   |
| spring.profiles.active  |   | default  | **Único**  | Valores posibles: dev, qa, prod   | v0.2.0  |



# Config de login / auth / token 

| Propiedad               | Variable de ambiente | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
| token.header |  | Authorization | Único | Nombre del header que almacena el token de session | v0.2.0 |
| token.secret | TOKEN_SECRET | ultra_secret_token | **Obligatorio** | La clave secreta de generación de token, usada para validar los tokens recibidos desde los request. | v0.2.0 |
| token.expiration | TOKEN_EXPIRATION | 30m | Opcional | Tiempo de expiración del token en m(inutos). Se puede elegir las siguientes medidas de duración: s(egundos), m(inutos), h(oras) | v0.2.0 |
| validationToken.expiration | VALIDTOKEN_EXPIRATION | 1440 | Opcional |  | v0.2.0 | 
| refreshToken.expiration | REFRESHTOKEN_EXPIRATION | 2880 | Opcional | Tiempo de expiración del refreshToken en segundos. Debe ser más grande que el tiempo del token |  |


## Configuración DDBB (SQL)

| Propiedad | Variable de ambiente | Valor por defecto | Necesidad | Descripcion | Desde |
| ---- | ---- | ---- | ------ | ---------------- | ---- |
| spring.datasource.url | DATABASE_IP_PORT y DATABASE_SCHEMA | [JDBC](back-end/app/src/main/resources/application.properties#L63) | **Obligatorio** | Url de conexión a la base de datos | v0.2.0 |
| spring.datasource.driver-class-name | | org.postgresql.Driver | Opcional | Nombre de la clase que representa el driver de conexión a la base de datos | v0.2.0 |
| spring.datasource.username | DATABASE_USER | postgres | **Obligatorio** | Nombre de usuario utilizado para realizar la conexión a la base de datos | v0.2.0 |
| spring.datasource.password | DATABASE_PASS | Local123 | **Obligatorio** | Contraseña utilizada para realizar la conexión a la base de datos | v0.2.0  |
| spring.datasource.hikari.maximumPoolSize  | HICKARI_MAXIMUM_POOL_SIZE  |  4 | Opcional  |   | v0.2.0  |
| spring.jpa.database-platform |  | [Dialect](back-end/app/src/main/resources/application.properties#L66) | Opcional | Nombre de la clase que representa el dialecto del motor de base de datos a utilizar  |  v0.2.0 |
| spring.jpa.hibernate.ddl-auto |   | validate | Opcional | Acciones que realiza JPA al iniciar la aplicación. Valores posibles: none, validate, create, update, create-drop | v0.2.0 |

## Configuración de Mail

| Propiedad | Variable de ambiente | Valor por defecto | Necesidad | Descripcion | Desde |
| ------ | ---- | ---- | ---- | ------------ | ----- |
|spring.mail.host | SMTP_HOST | smtp.gmail.co | Opcional | Dirección del servidor de mails | v0.2.0 |
|spring.mail.port | SMTP_PORT |587 | Opcional | Puerto del servidor de mails | v0.2.0 |
|spring.mail.username | SMTP_USERNAME | user | Opcional | Usuario del servidor de mails | v0.2.0 |
|spring.mail.password| SMTP_PASS | pass | Opcional | Password del servidor de mails | v0.2.0 |
|spring.mail.properties.mail.transport.protocol | | smtp | Opcional | Protocolo del servidor de mails | v0.2.0 |
|spring.mail.properties.mail.smtp.auth | | true | Opcional |  |  v0.2.0 |
|spring.mail.properties.mail.smtp.starttls.required | | true | Opcional |  |  v0.2.0 |
|spring.mail.properties.mail.smtp.starttls.enable | |false | Opcional | Habilita el envío de mails |v0.2.0 |
| app.mail.activate  | ACTIVATE_SENDING_EMAIL  | false  | Opcional  |   | v0.2.0  |
| mail.from  | SMTP_EMAIL  | sgh@test.org  | Opcional  |   | v0.2.0  |


## Configuración util para debug y monitoring 

| Propiedad	| Variable de ambiente       | Valor por defecto       | Necesidad | Descripcion | Desde |
| ------------ | -------- | ------ | --------- | ---------- | ---- |
| logging.level.net.pladema.sgx.restclient  |   | DEBUG  | Opcional  |   | v0.2.0  |
| logging.level.net.pladema.federar  |   |  DEBUG | Opcional  |   | v0.2.0   |
| management.endpoint.metrics  |   | false  | Opcional  |   | v0.2.0  |
| management.endpoints.web.exposure.include | ACTUATOR_ENABLED_ENDPOINTS  | health,info,env  | Opcional  |   | v0.2.0  |
| management.endpoints.jmx.exposure.include | ACTUATOR_ENABLED_ENDPOINTS  | health,info,env  | Opcional  |   | v0.2.0  |
| management.endpoint.prometheus.enabled  |  | false | Opcional |   |  v0.2.0  |
| management.metrics.export.prometheus.enabled  |   | false  | Opcional  |   | v0.2.0  |
| management.health.mail.enabled  |   |  false | Opcional  |   | v0.2.0  |
| management.endpoint.health.show-details  |   |   | Opcional  |   | v0.2.0  |
| management.info.git.mode |   | full  | Opcional  |   | v0.2.0  |
| actuator.configuration.whitelist | ACTUATOR_WHITELIST  | 0:0:0:0:0:0:0:1  | Opcional  |   | v0.2.0  |
| actuator.configuration.authenticated | ACTUATOR_REQUIRED_AUTHENTICATION  | false  | Opcional  |   | v0.2.0  |

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
| habilitar.boton.consulta | | false | Opcional | Propiedad de configuración de aplicación para des/habilitar el botón Nueva consulta.| v1.3.0

## Integración con servicios de terceros (Renaper, Federar, Snowstorm, Nomivac ... )

### Bus de interoperabilidad
| Propiedad | Variable de ambiente | Valor por defecto | Condición | Descripcion | Desde |
| -------- | ------ | -------- | ------ | -------------- | ---- |
| ws.bus.url.base   | | - | **Obligatorio** | URL productiva del BUS de interoperabilidad utilizada al solicitar historia clínica de pacientes federados en otros dominios.| v1.21.0  |


### Renaper
| Propiedad | Variable de ambiente | Valor por defecto | Condición | Descripcion | Desde |
| -------- | ------ | -------- | ------ | -------------- | ---- |
| ws.bus.url.base   | | - | **Obligatorio** | URL productiva del BUS de interoperabilidad utilizada al solicitar historia clínica de pacientes federados en otros dominios.| v1.21.0  |


| Propiedad | Variable de ambiente | Valor por defecto | Condición | Descripcion | Desde |
| -------- | ------ | -------- | ------ | -------------- | ---- |
| ws.renaper.enabled   | | - | **Único** | Determina si se utiliza la integracion con Renaper (se necesita completar la configuracion) | v0.2.0  |
| ws.renaper.nombre |  | - | **Obligatorio**  | Nombre que provee Renaper para cada dominio que utilice la integracion | v0.2.0  |
| ws.renaper.clave |  | - | **Obligatorio** | Clave que provee Renaper para cada dominio que utilice la integracion | v0.2.0  |


### Federar
| Propiedad | Variable de ambiente | Valor por defecto | Condición | Descripcion | Desde |
| ---------- | ------ | -------- | -------- | ------------ | ---- |
| ws.federar.enabled | | - | **Único** | Determina si se utiliza la integracion con Federar (se necesita completar la configuración) | v0.2.0  |
| ws.federar.url.base | | - | **Único** | URL de autenticación del Federador  | v0.2.0 |
| ws.federar.sisaCode |	| 10002001110000 | **Obligatorio** |	Código SISA para el dominio | v1.20.0 |
| ws.federar.claims.iss  |  | http://www.msal.gov.ar | **Obligatorio** | URI del dominio (registrada previamente ante la DNGISS) | v0.2.0  |
| ws.federar.claims.sub  |  | Ministerio de Salud de la Nación  | **Obligatorio** | Nombre del dominio | v0.2.0  |
| ws.federar.claims.aud  |  | - | **Único** | URL de autenticación usada en la generación del token JWT **[NO MODIFICAR]** | v0.2.0  |
| ws.federar.claims.name  |  | Prueba Jose | **Obligatorio** | Apellido y Nombres del Usuario que accede (no es necesario que hayan sido registrados ante la DNGISS) | v0.2.0  |
| ws.federar.claims.role |  | Project Manager | **Obligatorio** | Especialidad del Usuario (no es necesario que hayan sido registrados ante la DNGISS) | v0.2.0  |
| ws.federar.claims.ident  |  | 0001 | **Obligatorio** | Un identificador para el usuario (no es necesario que hayan sido registrados ante la DNGISS) | v0.2.0  |
| ws.federar.auth.signKey |   | federar ***[TESTING]*** | **Obligatorio** | A cada dominio se le asignará una palabra secreta única y cifrada por la DNGISS. | v0.2.0  |

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
| ws.snowstorm.request.timeout |   | 15000 (*milisegundos*) | Opcional |  Valor de TimeOut para el servicio — *se recomienda no utilizar valores inferiores a los 15 segundos* —  | v1.2.0  |

### Nomivac sincronización de datos

| Propiedad | Variable de ambiente | Valor por defecto | Condición | Descripcion | Desde |
| ---------- | ------ | -------- | -------- | ------------ | ---- |
| ws.federar.enabled | - | - | **Obligatorio** | Determina si se utiliza la integracion con Federar (se necesita completar la configuración) | v0.2.0  |
| ws.nomivac.synchronization.url.base  | - | localhost | **Obligatorio** | URL del dominio donde se sincroniza las vacunas | v1.22.0  |
| ws.nomivac.synchronization.cron.config  | - | - | **Obligatorio** | Cron que determina la periodicidad con la que se envia los datos a nomivac | v1.22.0  |
| app.feature.HABILITAR_BUS_INTEROPERABILIDAD  | - | false | **Obligatorio** | Define si se va a realizar una comunicación con el bus de interoperabilidad | v1.22.0  |

## Integración con sistemas relacionados
| Propiedad | Variable de ambiente   | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
| integration.covid.encryption.key | | WillGriggIsOnFir | Opcional | Clave para la encriptación de datos provistos por endpoints de SGH a otras aplicaciones (ej: covid) | v1.9.0 |

## Propiedades específicas de flavors 

### Chaco

| Propiedad    | Variable de ambiente | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
|oauth.enabled   |   | false  | Opcional  |   | v0.2.0   |
|oauth.tokenUrl   |   | http://stage.ventanillaunica.chaco.gov.ar/oauth/v2/token  |  Opcional |    | v0.2.0  |
|oauth.tokenHeader   |   | Authorization  | Opcional  |   | v0.2.0  |
|oauth.apiData   |   |  http://stage.ventanillaunica.chaco.gov.ar/api/v1/persona | Opcional  |   | v0.2.0  |
|oauth.redirectUri   |   | http://localhost:8080/oauth/chaco  | Opcional  |   |  v0.2.0 |
|oauth.loginUrl   |   | http://stage.ventanillaunica.chaco.gov.ar  | Opcional  |    | v0.2.0  |
|oauth.appLaunchUrl   |   | /frontpanel/aplicacion/59/launch  | Opcional  |   | v0.2.0  |


## Pruebas de estrés

Se crearon las siguientes propiedades para ser usado en las pruebas de estrés.

| Propiedad | Variable de ambiente  | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
|test.stress.disable.validation   |   | false  | Opcional  | Desactiva validaciones en el sistema para facilitar las pruebas  | v1.2.0   |


## Scheduled Jobs
| Propiedad | Variable de ambiente | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
| scheduledjobs.enabled  |   | true  | Opcional  | Des/habilitar la ejecución general de trabajos automáticos | v1.13.0   |
| scheduledjobs.federatepatients.enabled  |   | true  | Opcional  | Des/habilitar la federación de pacientes en estado validado  | v1.13.0   |
| scheduledjobs.federatepatients.seconds  |   | 0 | Opcional |   | v1.13.0   |
| scheduledjobs.federatepatients.minutes |   | 0 | Opcional |   | v1.13.0   |
| scheduledjobs.federatepatients.hours  |   | 3 | Opcional |   | v1.13.0   |
| scheduledjobs.federatepatients.dayofmonth  |  | * | Opcional |   | v1.13.0   |
| scheduledjobs.federatepatients.month  |   | * | Opcional |  | v1.13.0   |
| scheduledjobs.federatepatients.dayofweek  |  | * | Opcional |   | v1.13.0   |
| scheduledjobs.updateethnicities.enabled  |   | true  | Opcional  | Des/habilitar la actualización de etnias desde el servicio de Snowstorm | v1.15.0 |
| scheduledjobs.updateethnicities.seconds  |   | 0 | Opcional  |   | v1.15.0 |
| scheduledjobs.updateethnicities.minutes  |   | 0 | Opcional  |   | v1.15.0 |
| scheduledjobs.updateethnicities.hours  |   | 0 | Opcional  |   | v1.15.0 |
| scheduledjobs.updateethnicities.dayofmonth  |  | 15 | Opcional  |  | v1.15.0 |
| scheduledjobs.updateethnicities.month  |   | * | Opcional |   | v1.15.0 |
| scheduledjobs.updateethnicities.dayofweek  |  | * | Opcional  |   | v1.15.0 |
