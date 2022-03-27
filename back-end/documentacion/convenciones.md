### ![logo](../../front-end/apps/projects/hospital/src/assets/custom/icons/icon-72x72.png) Back-end | Convenciones de desarrollo

Para el desarrollo de backend se van a definir un conjunto de conveciones a respetar con el fin de tener un estandar de código. Las mismas pueden ser editadas si la situación lo amerita

### Lombok

Se dispuso de la libreria [lombok](https://projectlombok.org/) que nos permite crear clases sin la necesidad de escribir todos sus getters, setters, etc. De esta manera nos ahorramos la escritura de código.  Para configurarlo se puede hacer uso de la siguiente [guia](https://www.baeldung.com/lombok-ide)

#### Uso de entidades simples

Se descarta el uso de los hibernate mappings (ManyToOne, OneToOne, OneToMany, etc).

La decisión de usar entidades con atributos simples (Boolean, Short, Integer, etc) y evitar tener entidades con objetos a otras entidades, se debe a cuestiones de performance y testing.
El mal uso de esta anotaciones puede impactar de manera negativa en la performance, mientras que genera complejidad en la generación de datos mockeados para los test unitarios.

#### Nomeclatura de entidades

Se deben respetar las siguientes formas de definir las entidades:

1. **Primary key**: nombre **id** cuando es un atributo simple. Cuando es clave compuesta usar @Embbedable y ponerle nombre **pk** al atributo dentro de la entidad que represente la primary key.
2. **Foreign key**: cualquier atributo que represente una foreign key debe tener el siguiente formato <nombre_entidad>_id. Donde nombre_entidad es el nombre al cuál hace referencia.
3. **Nullable**: Aclarar cuando los atributos no son nullables.
4. **Length strings**: Definir el largo de los strings.

#### Fechas

Dado que estamos usando Java 11 se definen usar los nuevos tipos de [fecha](https://docs.oracle.com/javase/8/docs/api/java/time/package-summary.html) dejando de lado Date, Calendar, JodaTime. Basicamente usar

1. **LocalDateTime**: fechas con tiempo
2. **LocalDate**: solo fechas
3. **LocalTime**: solo horas

#### Base de datos

La idea es tener siempre la mayor compatibilidad entre motores de base de datos. Por lo cuál se deberíá seguir la siguiente lógica al momento de generar consultas de base de datos:

1. Usar las queries provista desde la interfaz de JPA
2. Usar @Query con con el lenguaje JPQL.
3. Usar queries nativas respetando el estandar SQL.
4. Usar queries nativas con funcionalidad especifica del lenguaje debe ser la **última opción** y en caso de usarse se debe hacer lo siguiente:
    1. Crear una interfaz de repositorio con el método particular
    2. Crear la implementación especifica de la interfaz para el motor de PostgreSql **obligatoriamente**
    3. En caso de usar otro motor entonces agregar su implementacion especifica.


Todas las queries que hacen lectura de datos en la base debe tener la siguiente anotación

```java
@Transactional(readOnly = true)

```

Perteneciente a la libreria de spring (no javax.transactional).



#### Estructura del proyecto

Se utiliza la combinación de paquetes por funcionalidad y por capas (formas de [estructura](http://www.javapractices.com/topic/TopicAction.do?Id=205)). El paquete principal debe responder a una division por funcionalidad, mientras que internamente se maneja por capas. Las capas deben seguir los conceptos de arquitectura hexagonal. Con esto queremos decir lo siguiente:

* **ar.lamansys.feature1**.
    * **infrastructure**: contiene los adapters de la aplicación encargado de adaptar el afuera para poder comunicarse con los casos de uso. Estos abarcan controllers, repositorios, etc.
        * **input**: Representa las comunicaciones del afuera hacia adentro
            * **rest**: Contiene los controllers, dtos, mappers de la aplicación.
            * **services**: Contiene los servicios externos que serán usados por otros módulos de la aplicación
        * **output**: Representa las comunicaciones del adentro hacia afuera
            * **repository**: Contiene los repositorios, entidades que se mapean con la base de datos.
    * **application**: Cada paquete representa un caso de uso del dominio.
    * **usecase1**: Contiene la clase que implementa el caso de uso. Preferentemente tener una clase con un único método publico, de esta forma aseguras single resposnability y facilita los test unitarios.
        * **exception**:  Contiene a su vez excepciones del dominio
    * **domain**: Contiene las clases y servicios que representan el dominio. Las clases son generalmente básicas mientras que los servicios de dominio contiene código que previamente estaban repetidos en lso casos de uso, es decir se generan por abstracción de caso de uso.

Preferentemente cuando se tiene bien definido un subdomino se espera desarrollar un [módulo maven](https://www.arquitecturajava.com/que-es-un-maven-module/) por cada uno de ellos.

####  Spring patterns DI

Se define utilizar la inyección de dependencias mediante el uso de [Constructor injection](https://dzone.com/articles/spring-di-patterns-the-good-the-bad-and-the-ugly).

```java
@Service
@Slfj
public class RegisterUser {

	private final UserStorage userStorage;

	public RegisterUser(UserStorage userStorage) {
		super();
		this.userStorage = userStorage;
	}
    
    public Integer run(UserBo userBo) {
        log.debug("Register new user {}", userBo);
        ....
    }
}
```

#### Logging

Hacer uso de los loggers para mostrar información, obligatoriamente uno de nivel debug por función. Los mismos nos ayudan a conocer ante una situación de error que esta sucediendo. Un ejemplo de uso es el siguiente:

Primer linea de cada función creada.
```java
log.debug("Input parameters id {}, parameter2 {}", id, parameter2);
```
Con esto si se activa el modo debug podemos ver que esta pasando.

Última linea de cada función
```java
log.debug("Output {}", objectOutput.toString());
```

Para saber que resultado genera.

Mostrar excepciones:

```java
log.error("Error-> {}", e); // e es de algún tipo de excepción.
```

#### Generales


Respetar los patrones [SOLID](https://www.youtube.com/watch?v=2X50sKeBAcQ&t=68s), principalmente el de **Single responsability**. Este se centra en que cada función, interfaz, etc tenga un solo mótivo por el cuál debe cambiar. Esto nos permite generar test unitarios con un scope alcanzable (testear una función que hace de todo es imposible e impractico).

Reducir el control de valores distinto de null. Reemplazar en lo posible con los [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html) de java. Los repositorios pueden devolver Optionals de objetos.



#### Testing

Realizar test unitarios principalmente del caso de uso.

Es importante entender el concepto de que si una función en su código **llama a otro método**, ya sea de la misma clase o de otra, estas deben ser **Mockeadas** (simular su respuesta). Esto es así porque el test unitario se centra en evaluar **solamente** las lineas de código que contiene (sus bifurcaciones, sus for, los caminos posibles que puede tomar un thread dentro del código).


Si respetan los principios SOLID, la responsabilidad de la clase, sus dependencias, etc, van a facilitar el testing. Una clase con mucha responsabilidad y dependencias termina generando un test demasiado complejo de realizar y mantener.

Ejemplos de testing,


**Controllers**

```java
@ExtendWith(MockitoExtension.class)
class ImmunizeControllerTest {

    private ImmunizeController immunizeController;

    @Mock
    private ImmunizePatient immunizePatient;

    @Mock
    private LocalDateMapper localDateMapper;

    @BeforeEach
    public void setUp() {
        immunizeController = new ImmunizeController(immunizePatient, localDateMapper);
    }

    @Test
    void successMapping() {
        when(localDateMapper.fromStringToLocalDate(any())).thenReturn(LocalDate.of(2020,12,12));
        var immunizePatientDto = new ImmunizePatientDto();
        immunizePatientDto.setClinicalSpecialtyId(4);
        immunizePatientDto.setImmunizations(List.of(createImmunization()));
        immunizeController.immunizePatient(1, 2, immunizePatientDto);

        ArgumentCaptor<ImmunizePatientBo> immunizePatientBoArgumentCaptor = ArgumentCaptor.forClass(ImmunizePatientBo.class);
        verify(immunizePatient, times(1)).run(immunizePatientBoArgumentCaptor.capture());
        Assertions.assertEquals(2, immunizePatientBoArgumentCaptor.getValue().getPatientId());
        Assertions.assertEquals(1, immunizePatientBoArgumentCaptor.getValue().getInstitutionId());
        Assertions.assertEquals(4, immunizePatientBoArgumentCaptor.getValue().getClinicalSpecialtyId());
        Assertions.assertEquals(1, immunizePatientBoArgumentCaptor.getValue().getImmunizations().size());
        Assertions.assertNull(immunizePatientBoArgumentCaptor.getValue().getImmunizations().get(0).getId());

        Assertions.assertEquals(LocalDate.of(2020,12,12), immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getAdministrationDate());

        Assertions.assertEquals(new VaccineDoseBo("dose0", (short)0), immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getDose());

        Assertions.assertEquals((short)3 , immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getConditionId());

        Assertions.assertEquals((short)1, immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getSchemeId());

        Assertions.assertEquals(new SnomedBo(null, "SNOMED_ID", "SNOMED_PT", null, null), immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getVaccine());

        Assertions.assertEquals("Nota de prueba", immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getNote());

        Assertions.assertEquals("BATCH", immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getLotNumber());
    }


    private ImmunizationDto createImmunization() {
        var result = new ImmunizationDto();
        result.setSnomed(new SnomedDto("SNOMED_ID", "SNOMED_PT"));
        result.setAdministrationDate("2020-05-10");
        result.setInstitutionId(null);
        result.setNote("Nota de prueba");
        result.setDose(new VaccineDoseInfoDto("dose0", (short)0));
        result.setConditionId((short)3);
        result.setSchemeId((short)1);
        result.setLotNumber("BATCH");
        return result;
    }


}
```

**Service**

```java
@ExtendWith(MockitoExtension.class)
class RegisterUserImplTest {

    private RegisterUser registerUser;

    @Mock
    private UserStorage userStorage;

    @Mock
    private  PasswordEncryptor passwordEncryptor;

    @BeforeEach
    public void setUp() {
        registerUser = new RegisterUserImpl(userStorage,
                "^[A-Za-z]\\\\w{5,29}$}",
                passwordEncryptor,
                "PRUEBA");
    }

    @Test
    @DisplayName("Username validations")
    void usernameValidations() {
        Exception exception = Assertions.assertThrows(RegisterUserException.class, () ->
                registerUser.execute(null, "EMAIL", "PASSWORD")
        );
        String expectedMessage = "El username es obligatorio";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);


        exception = Assertions.assertThrows(RegisterUserException.class, () ->
                registerUser.execute("12USERNAME", "EMAIL", "PASSWORD")
        );
        expectedMessage = "El username 12USERNAME no cumple con el patrón ^[A-Za-z]\\\\w{5,29}$} obligatorio";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
```

Las clases que se utilizan para el test deben respetar las ubicaciones en los paquetes que tiene sus pares en el código. Y su nombre debe ser igual a la de su par finalizado con la palabra Test.

### Manejo de errores

Evitar en lo posible capturar errores y arrastrarlo a través de las clases. Si es necesario capturarlos internamente y darle un tratamiento. De no ser posible lo mejor es lanzar la excepción y que lo manejen capas más altas.


Generalmente, si hay errores de validación en la API, se lanzan y se manipulan utilizando lo comentado en el siguiente [post](https://www.baeldung.com/global-error-handler-in-a-spring-rest-api). Se aconseja  tener una exception handler por subdominio.

```java
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = {"ar.lamansys.sgx.auth",
"net.pladema.user.infrastructure.input.rest"})
public class UserExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(UserExceptionHandler.class);

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ RegisterUserException.class })
	protected ApiErrorMessageDto handleRegisterUserException(RegisterUserException ex, Locale locale) {
		LOG.debug("RegisterUserException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(null, ex.getMessage());
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ PasswordResetTokenStorageException.class })
	protected ApiErrorMessageDto handlePasswordResetTokenStorageException(PasswordResetTokenStorageException ex, Locale locale) {
		LOG.debug("PasswordResetTokenStorageException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(null, ex.getMessage());
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ UserException.class })
	protected ApiErrorMessageDto handleUserException(UserException ex, Locale locale) {
		LOG.debug("UserException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(null, ex.getMessage());
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ UserStorageException.class })
	protected ApiErrorMessageDto handleUserStorageException(UserStorageException ex, Locale locale) {
		LOG.debug("UserStorageException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(null, ex.getMessage());
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ UserPasswordException.class })
	protected ApiErrorMessageDto handleUserPasswordException(UserPasswordException ex, Locale locale) {
		LOG.debug("UserPasswordException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(null, ex.getMessage());
	}
}
```



### Diseño de API

La API es una interfaz hacía el cliente y es importante definirla para que sea clara y cómoda de usar. Por eso es importante siempre coordinar con el frontend que necesita tener la API.

Los controladores que se crean tiene que manejar un único recurso. Los recursos pueden tomar muchas formas, no necesitan ser explicitamente una entidad, una tabla, etc. Por ejemplo:

Si tenemos la entidad persona esta puede ser un recurso el cuál es manejado por un controlador, pero a su vez se puede considerar un "sub-recurso" las personas con edad mayor a 50 años. De esta manera es valido crear un controlador que lo maneje.

Si bien se puede manejar ambos recursos en un controlador, y quizás es conveniente en un principio hacerlo el problema que presenta a futuro es que si hay demasiados endpoints en ese controlador se genera una super clase. Esta super clase posiblemente contiene muchas dependencias y hace que este muy acoplada rompiendo un poco los principios SOLID y haciéndola difícil de testear. En resumen, tener claro cuál es el recurso es clave para poder tener un controller bien desacoplado y manejable.

Cada controlador debería manejar los llamados básicos de una API: **GET, POST, PUT, PATCH, DELETE**; no es necesario que estén siempre todos. A su vez, es importante hacer usos de las **path variables** para tener un contexto claro en el cuál el recurso es manejado. No es costoso tener una URL larga si la misma te permite saber rápidamente que recurso y en que contexto estamos. Las path variables a su vez sirve como restricciones, dado que sus valores son obligatorios como parametros de entrada a los endpoints. Por ejemplo,


El siguiente es un controllador que tiene una url larga pero intenta contextualizar los alumnos que pertenecen a una jurisdicción, en una unidad de servicio, para un ciclo lectivo determinado.

Finalmente se le definió un endpoint para obtener los datos personales de dichos alumnos. Este endpoint contiene todas las pathvariables de la URL haciendo que el contexto y "filtro" usado sea conciso.

```java
@RestController
@Validated
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/immunize")
public class ImmunizeController {

    private final Logger logger;

    private final ImmunizePatient immunizePatient;

    private final LocalDateMapper localDateMapper;

    public ImmunizeController(ImmunizePatient immunizePatient,
                              LocalDateMapper localDateMapper) {
        this.immunizePatient = immunizePatient;
        this.localDateMapper = localDateMapper;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping
    public boolean immunizePatient(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId,
            @RequestBody @Valid ImmunizePatientDto immunizePatientDto) {
        logger.debug("Input parameters -> institutionId {}, patientId {}, vaccineDto {}",
                institutionId, patientId, immunizePatientDto);
        var immunizePatientBo = new ImmunizePatientBo(
                patientId, institutionId,
                immunizePatientDto.getClinicalSpecialtyId(),
                immunizePatientDto.getImmunizations().stream()
                        .map(i -> mapImmunization(i, institutionId)).collect(Collectors.toList()));
        immunizePatient.run(immunizePatientBo);
        return true;
    }
}
```


#### Data transfer objects (DTO)

Representan los datos de salida y de entrada para la API del sistema, por lo cuál tienen que ser lo más minimalista posible.

##### Typescript generator

Para facilitar el trabajo y tener una visión más clara acerca de los objetos que retorna y espera la API, se agrego un plugin que se encarga de generar para Angular las interfaces correspondientes.

Este plugin se corre durante la etapa de generación de código de **maven** y lo que hace es sobreescribir el archivo [api-model.ts](api-../../front-end/webapp/src/app/modules/api-rest/api-model.d.ts).

```javascript
/* tslint:disable */
/* eslint-disable */

export interface AAdditionalDoctorDto {
    fullName: string;
    generalPractitioner: boolean;
    id?: number;
    phoneNumber: string;
}

export interface APatientDto extends APersonDto {
    comments: string;
    generalPractitioner: AAdditionalDoctorDto;
    identityVerificationStatusId: number;
    medicalCoverageAffiliateNumber: string;
    medicalCoverageName: string;
    pamiDoctor: AAdditionalDoctorDto;
    typeId: number;
}
```

Esto nos agrega una validación adicional que permite saber si al cambiar un dto estamos impactando en alguna parte del frontend.

Para que los DTOs puedan ser detectados por el plugin se deben encontrar bajo un paquete dto y tienen que finalizar la palabra Dto o Enum. Esto es configurable desde el [pom.xml](./back-end/app/pom.xml)

```xml
<classPatterns>
    <pattern>ar.lamansys.**.dto.**Dto</pattern>
    <pattern>ar.lamansys.**.dto.**Enum</pattern>
</classPatterns>
```

### Conclusión

Estas son un conjunto de convenciones que ayudan a tener un desarrollo más estructurado para todos. La idea es mejorar paso a paso e ir incorporando nuevas prácticas.



### Links utiles

En esta sección se deja una lista de links utiles para aprender el uso de las tecnologías aplicadas en el backend


* [Video](https://www.youtube.com/watch?v=RnxzMVD3QsE&ab_channel=SpringFrameworkGuru) explicativo de como hacer una api rest sencilla con spring boot
* [Tutorial](https://www.baeldung.com/mapstruct) de [mapstruct](https://mapstruct.org/) libreria que usamos para mapear dtos a nuestros objetos del dominio.
* [Tutorial](https://www.baeldung.com/intro-to-project-lombok) de [lombok](https://projectlombok.org/) libreria usada para reducir la escritura de código repetitivo.
* [Baeldung](https://www.baeldung.com/) una muy buena referencia para encontrar ejemplos de uso de las librerias de spring boot combinadas con otras. En el siguiente [link](https://www.baeldung.com/rest-with-spring-series) hay un resumén de un conjunto de tutoriales que pueden seguir sobre spring boot.


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
    - [Exception Handling in Spring Boot](https://www.baeldung.com/global-error-handler-in-a-spring-rest-api)