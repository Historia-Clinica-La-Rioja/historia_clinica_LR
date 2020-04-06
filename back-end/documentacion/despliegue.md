## Despliegue

El proceso de despliegue se centra en definir los requerimientos minimos y pasos necesarios para poder tener la api **productiva**. Este proceso consta de una conjunto de pasos que  se deben realizar para cumplir con el objetivo.

### Diccionario de datos

1. Versión: Se refiere a la versión de la api.

### Configuración de ambiente

Esta sección define el hardware y sistema operativa en el cuál se espera correr el sistema:

#### Hardware

- Memoria: 4GB de RAM
- Disco: 10GB
- Procesador: 

#### Sistema operativo

- Ubuntu 19 LTS.

### Requerimientos

Para poder llevar adelante el despliegue de la api backend se debe tener instalado en el ambiente las siguientes aplicaciones:

- JRE : contiene la jvm para poder ejecutar aplicaciones java.

#### Instalación de JRE

La versión de java utilizada por la API es la **8**. Para poder instalarla se puede utilizar la siguiente [guia](https://openjdk.java.net/install/).


### Generación de artefacto

Este paso se centra en la generación del siguiente artefacto: **hospital-api-{version}.jar**. 

1. Ubicarse dentro de la carpeta **back-end**
2. Ejecutar el siguiente comando para generar el artefacto.
   1. **mvn clean package -pl hospital-api -am**

Una vez generado se encuentra en la siguiente ubicación

- back-end/hospital-api/target/**hospital-api-{version}**.jar

Para poder levantar el servicio se debe configurar las siguientes **variables de ambiente**

### Variables de ambientes

Son las variables requeridas para la ejecucion de los distintos microservicios. La misma permite configurar propiedades relevantes de cada microservicio.

#### Usuarios

##### Requeridos
1. **ADMIN_MAIL**: Mail del admin para acceder a la aplicación
2. **ADMIN_PASS**: Contraseña de acceso
3. **API_DOMAIN**: Dominio de la aplicación
4. **SERVER_PORT**: Puerto de la aplicaición 
5. **DATABASE_IP_PORT**: Ip y puerto de la base de datos
6. **DATABASE_SCHEMA**: Nombre de esquema
7. **DATABASE_USER**: Usuario de acceso a la base de datos
8. **DATABASE_PASS**: Contraseña de acceso a la base de datos
9.  **LOGIN_PAGE**: URL de la pagina de login (Por ejemplo, https://www.google.com/)

##### Opcionales

1. **TOKEN_SECRET**: Contraseña de encriptacion de JWT
2. **TOKEN_EXPIRATION** (en minutos): Es el tiempo de duración el token. Por defecto es: **20**
3. **VALIDTOKEN_EXPIRATION** (en minutos): Es el tiempo de duración el token. Por defecto es: **1440** (24horas)
4. **SMTP_HOST**: Host del servidor smtp (Ejemplo, email-smtp.us-west-2.amazonaws.com)
5. **SMTP_USERNAME**: Usuario de aws
6. **SMTP_PASS**: Contraseña de aws
7. **SMTP_EMAIL**: Email de envío
8. **DEFAULT_LANGUAGE**: Definir el lenguaje que maneja por default el frontend(Default: es-AR)
9. **OTHER_LANGUAGES**: Definir otros lenguajes que manipula el frontend(Default: en-US)

### Ejecución

Una vez configurada las variables de ambiente se puede ejecutar con el siguiente comando

- java -jar **hospital-api-{version}**.jar


### Validación

Una ejecutado el sistema se puede consultar el siguiente servicio para evaluar el estado de la api

- https://**{ip:port}**/api/actuator/health


Se espera que la respuesta de la consulta sea 

```json
{"status":"UP"}
```
