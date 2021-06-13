# Properties de SGH

Este documento detalla las propiedades configurables del sistema.

## Configuración del Back-end

El módulo de back-end se configura mediante el uso de un archivo `.properties`, el cual puede especificar las siguientes propiedades:

> **NOTA**: la columna *Necesidad* indica si es obligatorio definir una propiedad en un ambiente de producción. En tiempo de desarrollo puede alcanzar con el valor por defecto.

## Config de cada nodo

| Propiedad               | Parametro       | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
| server.port | SERVER_PORT | 8080 | Opcional |  |  v0.2.0  |
| api.domain | API_DOMAIN | /api | Opcional |  |  v0.2.0  |
| token.secret | TOKEN_SECRET | ultra_secret_token | Obligatorio |  | v0.2.0 |
| app.flavor  |  | 'minsal' | Opcional | Código del sabor de SGH. Opciones disponibles: `minsal`, `tandil`, `chaco` | v0.2.0 |
| app.default.language  | DEFAULT_LANGUAGE | 'es-AR' | Opcional | | v0.2.0 |
| app.other.languages  | OTHER_LANGUAGES | 'en-US' | Opcional | | v0.2.0 |
| internment.document.directory |DOCUMENT_ROOT_DIRECTORY | /temp | Obligatorio | Directorio donde se almacenan los archivos relacionados a la internación de un paciente | v0.2.0 |
| frontend.loginpage |  LOGIN_PAGE  | / | Opcional  |   | v0.2.0   |
| spring.profiles.active  |   | dev  |   | Valores posibles: dev, prod, tandil  | v0.2.0  |

## Config util para debug y monitoring 

| Propiedad               | Parametro       | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
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

# Config de login / auth / token 

| Propiedad               | Parametro       | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
| token.header | TOKEN_SECRET | X-Auth-Token | Obligatorio | Nombre del header que almacena el token de session | v0.2.0 |
| token.expiration | TOKEN_EXPIRATION | 120 | Obligatorio | Tiempo de expiración del token en segundos | v0.2.0 |
| validationToken.expiration | VALIDTOKEN_EXPIRATION | 1440 | Obligatorio |  | v0.2.0 | 
| refreshToken.expiration | | 0 | Obligatorio | Tiempo de expiración del refreshToken en segundos. Debe ser más grande que el tiempo del token |  |

## Config de bases de datos (SQL)

| Propiedad               | Parametro       | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
| spring.datasource.url |   |  jdbc:postgresql://localhost:5432}/hospitalDB | Obligatorio | Url de conexión a la base de datos | v0.2.0 |
| spring.datasource.driver-class-name | | org.postgresql.Driver | Obligatorio | Nombre de la clase que representa el driver de conexión a la base de datos | v0.2.0 |
| spring.datasource.username | DATABASE_USER | postgres | Obligatorio | Nombre de usuario utilizado para realizar la conexión a la base de datos | v0.2.0 |
| spring.datasource.password | DATABASE_PASS | Local123 | Obligatorio | Contraseña utilizada para realizar la conexión a la base de datos | v0.2.0  |
| spring.datasource.hikari.maximumPoolSize  | HICKARI_MAXIMUM_POOL_SIZE  |  4 | Opcional  |   | v0.2.0  |
| spring.jpa.database-platform |  | org.hibernate.dialect.PostgreSQLDialect | Obligatorio | Nombre de la clase que representa el dialecto del motor de base de datos a utilizar  |  v0.2.0 |
| spring.jpa.hibernate.ddl-auto |   | validate | Obligatorio | Acciones que realiza JPA al iniciar la aplicación. Valores posibles: none, validate, create, update, create-drop | v0.2.0 |

## Configuración de Mail

| Propiedad               | Parametro       | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
|spring.mail.properties.mail.smtp.starttls.enable | |false | Opcional | Habilita el envío de mails | v0.39.0 | 
|spring.mail.properties.mail.smtp.auth | | true | Opcional |  |  v0.2.0 |
|spring.mail.properties.mail.smtp.starttls.required | | true | Opcional |  |  v0.2.0 |
|spring.mail.host | SMTP_HOST | smtp.gmail.co |Requerido si enabled = true | Dirección del servidor de mails | v0.2.0 |
|spring.mail.port | SMTP_PORT |587 | Requerido si enabled = true | Puerto del servidor de mails | v0.39.0 |
|spring.mail.username | SMTP_USERNAME | user | Requerido si enabled = true | Usuario del servidor de mails | v0.2.0 |
|spring.mail.password| SMTP_PASS | pass | Requerido si enabled = true | Password del servidor de mails | v0.2.0 |
|spring.mail.properties.mail.transport.protocol | | smtp | Opcional | Protocolo del servidor de mails | v0.2.0 |
| app.mail.activate  | ACTIVATE_SENDING_EMAIL  | false  | Opcional  |   | v0.2.0  |
| mail.from  | SMTP_EMAIL  | sgh@test.org  | Opcional  |   | v0.2.0  |

## Configuración de reset de password y recaptcha 

Para la configuracion de ReCaptcha seguir los siguientes pasos: 

1. Ingresar a https://www.google.com/recaptcha/admin/ 
2. Agregar un nuevo sitio eligiendo las opciones de *reCAPTCHA v2* y *Casilla No soy un robot*.
3. Agregar el/los dominio/s necesarios.
4. Copiar las site y secret key para utilizarlos en el archivo de configuracion del proyecto como se indica en el cuadro siguiente.
  


| Propiedad               | Parametro       | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
|google.recaptcha.secret.key | RECAPTCHA_SECRET_KEY | Ninguno - Completar con secret key de la consola administrativa. | Obligatorio | Clave secreta correspondiente al de la consola administrativa. | v0.2.0 |
|google.recaptcha.site.key | RECAPTCHA_PUBLIC_KEY | Ninguno - Completar con public key de la consola administrativa. | Obligatorio | Clave publica correspondiente al de la consola administrativa. | v0.2.0 |
|google.recaptcha.validator.url |   | https://www.google.com/recaptcha/api/siteverify | Obligatorio | Url de google para validar el recaptcha. No debería cambiar. https://www.google.com/recaptcha/api/siteverify | v0.2.0 |
|google.recaptcha.enable  | RECAPTCHA_ENABLE  | true | Obligatorio |  Flag para habilitar / deshabilitar la funcionalidad de recaptcha en la aplicación. | v0.2.0  |

## Habilitación de nueva consulta

| Propiedad               | Parametro       | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
| habilitar.boton.consulta | | false | tg-1910 | Es necesario que se defina una propiedad de configuración de aplicación que al estar en true haga que el botón Nueva consulta esté disponible siempre, aun cuando el paciente no tenga turnos. En este caso las validaciones de turnos no deberían considerarse.| v1.3.0

## Integración con terceros (Renaper, Federar, Snowstorm ... )

| Propiedad               | Parametro       | Valor por defecto       | Condición | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
| ws.renaper.enabled   | | false | Obligatorio | Determina si se utiliza la integracion con Renaper (se necesita completar la configuracion) | v0.2.0  |
| ws.renaper.url.base |   |  https://federador.msal.gob.ar/masterfile-federacion-service/api | Obligatorio (si Renaper esta activado)  | URL base donde se van a consumir los servicios Renaper  | v0.2.0  |
| ws.renaper.url.cobertura |   | /personas/cobertura | Único | URL relativa para consumir el servicio de Cobertura Medica | v0.2.0 |
| ws.renaper.url.persona |   | /personas/renaper | Único | URL relativa para consumir el servicio de Datos de Persona | v0.2.0 |
| ws.renaper.nombre |  | - | Obligatorio (si Renaper esta activado)  | Nombre que provee Renaper para cada dominio que utilice la integracion | v0.2.0  |
| ws.renaper.clave |  | - | Obligatorio (si Renaper esta activado)  | Clave que provee Renaper para cada dominio que utilice la integracion | v0.2.0  |
| ws.renaper.dominio |  | DOMINIOSINAUTORIZACIONDEALTA | Único  | Código de dominio | v0.2.0  |
| ws.federar.enabled | | false | No obligatorio | Determina si se utiliza la integracion con Federar (se necesita completar la configuración) | v0.2.0  |
| ws.federar.claims.iss  |  | - | Obligatorio (si Federar esta activado) | URI del dominio (registrada previamente ante la DNGISS) | v0.2.0  |
| ws.federar.claims.sub  |  | - | Obligatorio (si Federar esta activado) | Nombre del dominio | v0.2.0  |
| ws.federar.claims.aud  |  | - | No obligatorio | URL de autenticación del Federador | v0.2.0  |
| ws.federar.claims.name  |  | - | Obligatorio (si Federar esta activado) | Apellido y Nombres del Usuario que accede (no es necesario que hayan sido registrados ante la DNGISS) | v0.2.0  |
| ws.federar.claims.role |  | - | Obligatorio (si Federar esta activado) | Especialidad del Usuario (no es necesario que hayan sido registrados ante la DNGISS) | v0.2.0  |
| ws.federar.claims.ident  |  | - | Obligatorio (si Federar esta activado) | Un identificador para el usuario (no es necesario que hayan sido registrados ante la DNGISS) | v0.2.0  |
| ws.federar.auth.grantType |   | client_credentials  | Único | Propiedades definidas por Federar | v0.2.0  |
| ws.federar.auth.scope |   | Patient/\*.read,Patient/\*.write  | Único | Propiedades definidas por Federar | v0.2.0  |
| ws.federar.auth.clientAssertionType |   | urn:ietf:params:oauth:client-assertion-type:jwt-bearer  | Único | Propiedades definidas por Federar | v0.2.0  |
| ws.federar.auth.signKey |   | - | Obligatorio (si Federar esta activado)  | A cada dominio se le asignará una palabra secreta única y cifrada por la DNGISS. | v0.2.0  |
| ws.federar.url.validateToken |   | /bus-auth/tokeninfo | Único |  URL relativa del servicio para validar el token obtenido | v0.2.0  |
| ws.federar.url.localIdSearch |   | /masterfile-federacion-service/fhir/Patient | Único |  URL relativa del servicio para buscar por id paciente nacional  | v0.2.0  |
| ws.federar.url.federate |   | /masterfile-federacion-service/fhir/Patient | Único |  URL relativa del servicio para federar un paciente  | v0.2.0  |
| ws.snowstorm.params.preferredOrAcceptableIn |   | 450828004 | No obligatorio |  Parametros para consulta a servicio Conceptos que indica que la descripción debe ser preferida o aceptable en al menos uno de estos parametros  | v1.2.0  |
| ws.snowstorm.params.limit |   | 30 | No obligatorio |  Parametro para consulta a servicio Conceptos que indica limite de resultados  | v1.2.0  |
| ws.snowstorm.params.termActive |   | true | No obligatorio |  Parametro para consulta a servicio Conceptos que indica si el termino a buscar debe estar activo  | v1.2.0  |
| ws.snowstorm.auth.language |   | es-AR;q=0.8,en-GB;q=0.6 | No obligatorio |  Header que indica el lenguaje de los resultados  | v1.2.0  |
| ws.snowstorm.url.concepts |   | /MAIN/concepts | No obligatorio |  URL relativa para consumir el servicio de Conceptos a buscar  | v1.2.0  |
| ws.snowstorm.url.base |   | https://snowstorm-test.msal.gob.ar (test) y https://snowstorm.msal.gov.ar (prod) | No obligatorio |  URL base donde se van a consumir los servicios de Snowstorm  | v1.2.0  |
| ws.snowstorm.appKey |   | - | Obligatorio(si Snowstorm lo requiere) |  Key de la aplicación utilizada para autorización de request  | v1.2.0  |
| ws.snowstorm.appId |   | - | Obligatorio(si Snowstorm lo requiere) |  Id de la aplicación utilizada para autorización de request  | v1.2.0  |

## Integración con sistemas relacionados
| Propiedad               | Parametro       | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
| integration.covid.encryption.key | | WillGriggIsOnFir | Opcional | Clave para la encriptación de datos provistos por endpoints de SGH a otras aplicaciones (ej: covid) | v1.9.0 |

## Propiedades específicas de flavors 

### Chaco

| Propiedad               | Parametro       | Valor por defecto       | Necesidad | Descripcion | Desde |
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

| Propiedad               | Parametro       | Valor por defecto       | Necesidad | Descripcion | Desde |
| ----------------------- | ----------------| ----------------------- | --------- | ----------- | ----- |
|test.stress.disable.validation   |   | false  | Opcional  | Desactiva validaciones en el sistema para facilitar las pruebas  | v1.2.0   |