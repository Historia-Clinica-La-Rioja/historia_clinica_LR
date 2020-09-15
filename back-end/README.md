# Backend

El proyecto esta desarrollado en Java en su versión 11. 

### Requerimientos

Se espera tener configurado una base de datos postgreSQL con la siguiente configuración inicial (se puede seguír la guía explicada en el [README Raíz](README.md):

* IP: localhost
* puerto: 5432
* usuario: postgres
* password: Local123
* esquema: hospitalDB

Por defecto las propiedades de desarrollo ya se encuentran configuradas con los valores definidos para la base de datos. Si algún valor cambia, entonces se pueden configurar mediante [propiedades](../properties.md) externalizadas:

* spring.datasource.url=jdbc:postgresql://**localhost:5432/hospitalDB**
* spring.datasource.username=postgres
* spring.datasource.password=Local123
 
O haciendo uso de variables de ambientes
  
* DATABASE_IP_PORT: ip y puerto de la base
* DATABASE_SCHEMA: nombre del esquema
* DATABASE_USER: nombre de usuario
* DATABASE_PASS: password del usuario

**Aclaración**: Controlar que no haya espacios de más en los valores de las propiedades.

### Iniciar backend

Una vez importado el backend en entorno local deberia estar en condiciones de levantar la aplicacion de algunas de las siguientes formas:

1. Eclipse: Buscar el App.java que esta dentro del paquete base (net.pladema) y hacer 'Run application'
2. Intellij: Buscar el App.java que esta dentro del paquete base (net.pladema) y 'Run'
3. Consola: mvn spring-boot:run dentro de la carpeta /back-end/hospital-api


Si todo es correcto se puede comprobar accediendo a la [url](http://localhost:8080/api/swagger-ui.html#/). De esta forma se debe poder visualizar en el buscador la página principal de Swagger para invocar la API con los endpoints definidos.

Haciendo uso del siguiente usuario y contraseña: admin@example.com/admin123 se pueden logear en el endpoint de authenticación y obtener un token valido.

### Profiles

Se hara uso de SpringProfile para determinar en que contexto se encuentra la aplicacion. 

Se encuentran 4 archivos de propiedades, uno para cada **Profile** :
* `application-dev.properties`: desarrollo
* `application-chaco.properties`: desarrollo
* `application-tandil.properties`: desarrollo
* `application-prod.properties`: ambiente productivo

La forma de activar un **Profile** es mediante la propiedad `spring.profiles.active=` 
> Ejemplo: `spring.profiles.active=dev` 

Para el desarrollo se debe usar `dev`, el cuál requiere activación.


### Desarrollo

Al levantar el ambiente de desarrollo por primera vez se tiene un conjunto de usuarios creados en el application-dev.properties para testear los distintos roles de la aplicación.


##### Administrativo

* usuario: administrativo@example.com
* contraseña: admin123

##### Especialista medico

* usuario: especialista@example.com
* contraseña: admin123

##### Profesional de la salud

* usuario: profesionalsalud@example.com
* contraseña: admin123


##### Administrador institucional

* usuario: administradorinstitucional@example.com
* contraseña: admin123


##### Administrador institucional

* usuario: administradorinstitucional@example.com
* contraseña: admin123


##### Enfermero

* usuario: enfermero@example.com
* contraseña: admin123

##### Administrador agenda

* usuario: administradoragenda@example.com
* contraseña: admin123


#### Renaper y federar

Estos servicios estan deshabilitados para el ambiente de desarrollo. Esto se debe a que no tenemos ambientes de pruebas de cada uno para poder usarlos localmente.