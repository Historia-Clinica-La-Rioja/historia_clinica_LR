![Historia Clinica](documentacion/images/HC-logo.png)

# HospitalApp

## Instalación
Se ofrecen dos alternativas para el despliegue — *y puesta en marcha* — de la aplicación: a través de contenedores Docker y mediante el uso de la herramienta Ansible. Esta última se describe en profundidad en su [apartado correspondiente](/sgh-ansible/README.md).

### Despliegue con Docker
Desde [aquí](https://hub.docker.com/r/historiaclinica/hsi) podrán accederse a todas las imágenes Docker de la aplicación desde la versión 1.7 en adelante. Además, se dispone de un instructivo completo para su puesta en marcha. 

#### Base de datos
Es requesito para el despliegue de la imagen Docker contar con una base de datos PostgreSQL versión 11. Tras iniciar el contenedor, la base de datos será migrada — *actualizada* — hasta la versión correspondiente. Tener en cuenta que el inicio del contenedor **NO CONFIGURA** la base, sólo aplica modificaciones incrementales.

#### Infraestructura mínima recomendada
Se recomienda desplegar la aplicación bajo la siguiente infraestrutura:
* Al menos 2 (dos) nodos de aplicación
* Balanceador de carga
* Un cluster para PostgreSQL
* Un nodo NFS para el almacenamiento y acceso compartido de documentos clínicos y fotos de pacientes. 

### Verificar integraciones con servicios externos

* Autenticación servicio Renaper

```shell
curl -X POST 'https://federador.msal.gob.ar/masterfile-federacion-service/api/usuarios/aplicacion/login' -d '{"nombre":"NOMBRE", "clave":"CLAVE", "codDominio":"DOMINIOSINAUTORIZACIONDEALTA"}' -H "Content-Type: application/json"
```

Reemplazar los valores **NOMBRE** y **CLAVE** por los proporcionados por el Ministerio de Salud.


* Autenticación servicio Federar 

Para verificar la correcta creación de un JWT (*Json Web Token*) ingresar a la [plataforma](http://jwtbuilder.jamiekurtz.com/) y completar el **claim** teniendo en cuenta la descripción de cada atributo:

```json
{
	"iss": "URI del dominio registrada ante la DNGISS",
	"iat": "Fecha de generación del Token", 
	"exp": "Fecha de expiración del Token",
	"aud": "https://federador.msal.gob.ar/bus-auth/auth",
	"sub": "Nombre del dominio, por ej. Ministerio de Salud de la Nación",
	"name": "Apellido y Nombres del Usuario que accede",
	"role": "Especialidad del usuario",
	"ident": "Un identificador para el usuario"
}
```

Completar el valor **KEY** con el proporcionado por el ministerio de Salud.

Una vez generado el **JWT**, utilizarlo en el siguiente comando — *reemplazando la palabra TOKEN* — para validar la autenticación en el servicio — *obtensión del accessToken* —.

```shell
curl -X POST 'https://federador.msal.gob.ar/bus-auth/auth' -d '{"grantType": "client_credentials","scope": "Patient/*.read,Patient/*.write", "clientAssertionType": "urn:ietf:params:oauth:client-assertion-type:jwt-bearer", "clientAssertion":"TOKEN"}' -H "Content-Type: application/json"
```

* Autenticación servicio Snowstorm

```shell
curl -X GET 'https://snowstorm.msal.gob.ar/MAIN/concepts?activeFilter=true&term=meningococo&limit=10&offset=0&ecl=%5E2281000221106' -H "Accept-Language: es" -H "app_id: APP_ID" -H "app_key: APP_KEY"
```

Reemplazar los valores **APP_ID** y **APP_KEY** por los proporcionados por el Ministerio de Salud.

## Contribución

Para el envío de propuestas, consultas técnicas o especificación funcional y responsable de la aprobación de las mismas, escribir al siguiente mail de contacto: historia_clinica@pladema.exa.unicen.edu.ar. 
    
Este código de Historia clínica, se encuentra  disponible en [gitlab-publico.pladema.net/historia-clinica/sgh](http://gitlab-publico.pladema.net/historia-clinica/sgh). 
    
Para subir aportes utilizar un fork y pedir un Merge Request a dicha URL.

## Proyecto

Sitio de demo: http://sgh.pladema.net/auth/login

El proyecto está formado por los siguientes tres proyectos:

1. front-end/backoffice: Single Page Application del panel de administración (backoffice) para manejo de los flujos básicos y creación de datos maestros. Implementada con ReactAdmin.
2. front-end/apps/projects/hospital: Single Page Application que contiene la funcionalidad principal del sistema. Implementada en Angular 11.
3. back-end/hospital-api: API REST de todo el sistema implementada con Spring Boot 2.3 y Java 11

## Licencia

Apache 2.0

Consulte el archivo de LICENCIA incluido para obtener más detalles.


