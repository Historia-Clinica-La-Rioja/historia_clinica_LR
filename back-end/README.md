# ![logo](../front-end/apps/projects/hospital/src/assets/custom/icons/icon-72x72.png) HSI | Back-end

El proyecto se encuentra desarrollado bajo el framework [Spring](https://spring.io/) por lo que se recomienda leer información general a través de las [guías oficiales](https://spring.io/guides).

## Requisitos

* Java (*OpenJDK*) en la versión definida en el archivo [POM principal](../pom-parent.xml#L17) del proyecto. Se recomienda utilizar la siguiente [guia](https://docs.google.com/document/d/1ArrwLT-GIdwYon3vVDMIQ3uUSUpBgo6ZWzrNfdgdqn4/edit#heading=h.jr3s1c6kg6p3).
* Maven 3.6.
* Base de Datos Postgresql local de acuerdo al [documento específico](../dba/README.md).

## Puesta en Marcha

**Build**

Buildear el proyecto por línea de comandos desde la carpeta `/back-end`:

>```mvn clean install```

**Build whitout running tests** (faster): 

>```mvn clean install -DskipTests```

**Run application**

- Correr la aplicación por línea de comandos desde la carpeta `/back-end/app`: 
> `java -Dspring.profiles.active=dev -jar target/app*.jar`

## Desde un IDE de preferencia

1. Importar el módulo **backend** en el IDE.

2. Ubicar la clase principal del proyecto definida en el módulo [App](app/pom.xml#L20). 
3. Correr la Aplicación:

- **Eclipse**
    - Ejecutar `Run application` sobre la clase principal.
    - Agregar `-Dspring.profiles.active=dev` en el campo **"VM Arguments"** del Run Configuration. 
- **IntelliJ**
    - Ejecutar `Run` sobre la clase principal.
    - Agregar `-Dspring.profiles.active=dev` en el campo **"VM Options"** del Run Configuration.

Tras la ejecución de todos los pasos, podrá accederse a la [página principal](http://localhost:8080/api/swagger-ui.html#/) de Swagger para invocar la API con los endpoints definidos. 

>Haciendo uso del siguiente usuario y contraseña: admin@example.com/admin123 se pueden logear en el endpoint de authenticación y obtener un token valido.


## Ambiente de desarrollo

Características relevantes sobre el entorno de desarrollo:

- Propiedades de ambiente para configuración del entorno. Por defecto, las propiedades necesarias para desarrollo se encuentran definidas en el archivo `application-dev.properties`. Para consultar todas las propiedades definidas leer el [documento específico](../properties.md).  

Configurar archivo de propiedades externalizado por línea de comandos o desde el IDE:

>```--spring.config.additional-location=file:/$PATH```

- Des/habilitación de funcionalidad a través de [Features Flags](hospital-api/src/main/java/net/pladema/sgx/featureflags/AppFeature.java).
- Manejo de [Roles](hospital-api/src/main/java/net/pladema/permissions/repository/enums/ERole.java). 


Ver también:

- [Carga de usuarios de prueba](documentacion/sample-data.md)
- [Tareas programadas](documentacion/scheduled-jobs.md)
- [Feature flags](documentacion/feature-flags.md)
- [Convenciones](documentacion/convenciones.md)

---- 

## Material de Lectura

- Spring Boot
    - [Modules](https://reflectoring.io/spring-boot-modules/)
    - [Profiles](https://docs.spring.io/spring-boot/docs/1.2.0.M1/reference/html/boot-features-profiles.html)
- Java Modules
    - [Java 9 Modularity](https://www.baeldung.com/java-9-modularity)
    - [Java 9 Modules](https://www.arquitecturajava.com/java-9-modules/)
    - [Understanding Java 9 Modules](https://www.oracle.com/corporate/features/understanding-java-9-modules.html)
- Architecture
    - [Clean architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
    - [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- Tests
    - [Unit Tests](https://reflectoring.io/unit-testing-spring-boot/)
    - [Controller Tests](https://thepracticaldeveloper.com/guide-spring-boot-controller-tests/#strategy-1-spring-mockmvc-example-in-standalone-mode)
    - [Web controller Tests](https://reflectoring.io/spring-boot-web-controller-test/)
- Exceptions
    - [Exception Handling in Spring Boot](https://stackoverflow.blog/2019/09/30/how-to-make-good-code-reviews-better/)