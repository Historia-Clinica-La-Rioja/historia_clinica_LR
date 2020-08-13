# Backend

El proyecto esta desarrollado en Java en su versión 11. 

### Requerimientos

Se espera tener configurado una base de datos postgreSQL con la siguiente configuración inicial:

* IP: localhost
* puerto: 5432
* usuario: postgres
* password: Local123
* esquema: hospitalDB


Todos estos datos pueden ser configurados mediante [propiedades](../properties.md) externalizadas:

* spring.datasource.url
* spring.datasource.username
* spring.datasource.password
  
O haciendo uso de variables de ambientes
  
* DATABASE_IP_PORT: ip y puerto de la base
* DATABASE_SCHEMA: nombre del esquema
* DATABASE_USER: nombre de usuario
* DATABASE_PASS: password del usuario


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

Para el desarrollo se debe usar `dev` (se encuentra por defecto)

