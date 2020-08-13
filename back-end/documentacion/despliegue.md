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

### Propiedades requeridas

Las siguientes propiedades son obligatorias a configurar para poder levantar el ambiente productivo.

- [x] **spring.datasource.url**: La url y esquema de la base de datos. Ejemplo, jdbc:postgresql://localhost:5432/hospitalDB
- [x] **spring.datasource.username**: El usuario para conectarse a la base.
- [x] **spring.datasource.password**: La password asociada al usuario de conexión
- [x] **internment.document.directory**: ubicación de los documentos generados por la aplicación.
- [x] **token.secret**: La clave secreta de generación de token, usada para validar los tokens recibidos desde los request.

Todas estas propiedades pueden ser configuradas de diversas formas. 

#### Usando un archivo con propiedades externalizadas

Este archivo puede ser ubicado en cualquier parte del sistema, y lo que se necesita es que al momento de ejecutar la aplicación se tiene que pasar como argumento en el comando usado para arrancar el sistema.

Ejemplo de archivo con propiedades externalizadas

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hospitalDB
spring.datasource.username=postgres 
spring.datasource.password=qkjsdo
internment.document.directory=tmp
token.secret=qiweK3la
```



#### Configurando variables de ambientes

La otra opción disponible es configurar usando variables de ambiente. Esta opción puede ser util sí se usa docker-compose file donde podes declarar variables de ambiente. 

Las variables de ambiente evitan tener que pasarlas como argumentos al momento de ejecutar la aplicación, pero tienen que controlar que en la terminal, consola o lugar donde ejecutan el comando las variables de ambientes esten configuradas (En linux usar el comando **echo $VARIABLE**, para ver su valor). 

Las variables a configurar son:


- [x] **DATABASE_IP_PORT**: La ip y puerto de la base de datos. Ejemplo, localhost:5432
- [x] **DATABASE_SCHEMA**: el esquema de la base de datos. Ejemplo, hospitalDB
- [x] **DATABASE_USER**: El usuario para conectarse a la base.
- [x] **DATABASE_PASS**: La password asociada al usuario de conexión
- [x] **DOCUMENT_ROOT_DIRECTORY**: ubicación de los documentos generados por la aplicación.
- [x] **TOKEN_SECRET**: La clave secreta de generación de token, usada para validar los tokens recibidos desde los request.

### Profiles

Dado que el sistema hace uso de los profiles para determinar el contexto de la aplicación, es necesario configurarlo al momento de iniciar el entorno productivo. Los mismos se pueden activar agregandolo como argumento al llamado de la ejecución o usando el archivo de propiedades externalizadas.

#### Propiedades externalizadas
```properties
spring.profiles.active=prod,tandil
```

#### Argumento en la ejecución
```bash
- java -jar **hospital-api-{version}**.jar --spring.profiles.active=prod,tandil
```


### Ejecución

Una vez generado el archivo con propiedades externalizadas o configuradas las variables de ambiente se puede ejecutar algunos de los siguientes comandos según corresponda:


##### Propiedades externalizadas
```bash
- java -jar **hospital-api-{version}**.jar --spring.config.additional-location=file:/home/user/externalizadas.properties
```


##### Propiedades externalizadas con profiles
```bash
- java -jar **hospital-api-{version}**.jar --spring.config.additional-location=file:/home/user/externalizadas.properties --spring.profiles.active=prod,tandil
```

##### Variables de ambiente
- java -jar **hospital-api-{version}**.jar --spring.profiles.active=prod,tandil


### Validación

Una ejecutado el sistema se puede consultar el siguiente servicio para evaluar el estado de la api

- https://**{ip:port}**/api/actuator/health


Se espera que la respuesta de la consulta sea 

```json
{"status":"UP"}
```
