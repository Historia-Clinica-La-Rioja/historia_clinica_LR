![Historia Clinica](documentacion/images/HC-logo.png)

# HSI | Historia de Salud Integrada

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

### Integraciones con servicios externos

- [Google Recaptcha](#configuración-de-google-recaptcha)
- [Renaper](#validación-servicio-renaper)
- [Federar](#validación-servicio-federar)
- [Snowstorm](#validación-servicio-snowstorm)

#### Configuración de Google Recaptcha

Configurar el servicio siguiendo los pasos descriptos a continuación:

1. Ingresar a la [página oficial](https://www.google.com/recaptcha/admin/) de Google Recaptcha. 
2. Agregar un nuevo sitio eligiendo las opciones de *reCAPTCHA v2* y *Casilla No soy un robot*.
3. Agregar el/los dominio/s necesarios.
4. Copiar el valor `site` y `secret key` para utilizarlos en el archivo de configuracion externalizado.


#### Validación servicio Renaper

Ejecutar el siguiente request por línea de comandos reemplazando los valores **NOMBRE** y **CLAVE** por los provistos por el Ministerio de Salud.

```shell
curl -X POST 'https://federador.msal.gob.ar/masterfile-federacion-service/api/usuarios/aplicacion/login' -d '{"nombre":"NOMBRE", "clave":"CLAVE", "codDominio":"DOMINIOSINAUTORIZACIONDEALTA"}' -H "Content-Type: application/json"
```

> El request deberá retornar el atributo `token` para comprobar que las credenciales son correctas.


#### Validación servicio Federar 

Para verificar el acceso al servicio Federar primero deberá generarse un Token JWT (*Json Web Token*) y utilizar dicho token en el request de autenticación. El token puede crearse desde la plataforma [JWTBuilder](http://jwtbuilder.jamiekurtz.com/). 

Una vez en la plataforma, debe configurarse el **claim** teniendo en cuenta la descripción de cada atributo:

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

Una vez generado el **JWT**, utilizarlo en el siguiente comando — *reemplazando la palabra TOKEN* — para validar la autenticación en el servicio.

```shell
curl -X POST 'https://federador.msal.gob.ar/bus-auth/auth' -d '{"grantType": "client_credentials","scope": "Patient/*.read,Patient/*.write", "clientAssertionType": "urn:ietf:params:oauth:client-assertion-type:jwt-bearer", "clientAssertion":"TOKEN"}' -H "Content-Type: application/json"
```

> El request deberá retornar el atributo `accessToken` para comprobar que las claves son correctas.

#### Validación servicio Snowstorm

Ejecutar el siguiente request por línea de comandos reemplazando los valores **APP_ID** y **APP_KEY** por los proporcionados por el Ministerio de Salud.

```shell
curl -X GET 'https://snowstorm.msal.gob.ar/MAIN/concepts?activeFilter=true&term=meningococo&limit=10&offset=0&ecl=%5E2281000221106' -H "Accept-Language: es" -H "app_id: APP_ID" -H "app_key: APP_KEY"
```

> El request deberá retornan un estado 200 para comprobar que las claves son correctas.

## Contribución

Para el envío de propuestas, consultas técnicas o especificación funcional y responsable de la aprobación de las mismas, escribir al siguiente mail de contacto: historia_clinica@pladema.exa.unicen.edu.ar. 
    
Este código de Historia clínica, se encuentra  disponible en [gitlab-publico.pladema.net/historia-clinica/sgh](http://gitlab-publico.pladema.net/historia-clinica/sgh). 
    
Para subir aportes utilice esta [guía de contribución](CONTRIBUTING.md).

## Proyecto

Sitio de demo: http://sgh.pladema.net/auth/login

El proyecto está formado por los siguientes tres proyectos:

1. front-end/backoffice: Single Page Application del panel de administración (backoffice) para manejo de los flujos básicos y creación de datos maestros. Implementada con ReactAdmin.
2. front-end/apps/projects/hospital: Single Page Application que contiene la funcionalidad principal del sistema. Implementada en Angular.
3. back-end/app: API REST de todo el sistema implementada con Spring Boot y Java.


### Documentación (desarrollo)

- [Architecture](documentacion/arquitectura.md)
- [DDBB](dba/README.md)
- [Desarrollo](documentacion/desarrollo.md)
- [Back-end](back-end/README.md) 
- [Front-end](front-end/README.md)

## Licencia

Apache 2.0

Consulte el archivo de [LICENCIA](LICENSE.md) incluido para obtener más detalles.


