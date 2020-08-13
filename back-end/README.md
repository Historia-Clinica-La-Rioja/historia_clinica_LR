# Backend

El proyecto esta desarrollado en Java en su versión 11. 

### Lombok

Se dispuso de la libreria [lombok](https://projectlombok.org/) que nos permite clases sin la necesidad de escribir todos sus getters, setters, etc. De esta manera nos ahorramo la escritura de código.  Para configurarlo se puede hacer uso de la siguiente [guia](https://www.baeldung.com/lombok-ide)


### Iniciar backend


Una vez importado el backend en entorno local deberia estar en condiciones de levantar la aplicacion de algunas de las siguientes formas:

1. Eclipse: Buscar el App.java que esta dentro del paquete base (net.pladema) y hacer 'Run application'
2. Intellij: Buscar el App.java que esta dentro del paquete base (net.pladema) y 'Run'
3. Consola: mvn spring-boot:run dentro de la carpeta /back-end/hospital-api


Si todo es correcto se puede comprobar accediendo a la siguiente url http://localhost:8080/api/swagger-ui.html#/ y se podría ver la pantalla de swagger con los endpoints definidos.

Haciendo uso del siguiente usuario y contraseña: admin@example.com/admin123 se pueden logear en el endpoint de authenticación y obtener un token valido.

### Profiles

Se hara uso de SpringProfile para determinar en que contexto se encuentra la aplicacion. 

Se encuentran 3 arhivos de propiedades, uno para cada **Profile** :
* `application-dev.properties`: desarrollo
* `application-test.properties`: testing
* `application-prod.properties`: ambiente productivo

La forma de activar un **Profile** es mediante la propiedad `spring.profiles.active=` 
> Ejemplo: `spring.profiles.active=dev` 

Para el desarrollo se debe usar `dev` (se encuentra por defecto)

