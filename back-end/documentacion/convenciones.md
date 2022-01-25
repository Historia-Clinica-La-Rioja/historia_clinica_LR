### ![logo](../../front-end/apps/projects/hospital/src/assets/custom/icons/icon-72x72.png) Back-end | Convenciones de desarrollo

Para el desarrollo de backend se define un conjunto de conveciones a respetar con el fin de tener un código estándar. Las convenciones están sujetas a cambio si la situación lo amérita.

[[_TOC_]]


## Estructura del proyecto

Se utiliza la combinación de paquetes por funcionalidad y por capas (formas de [estructura](http://www.javapractices.com/topic/TopicAction.do?Id=205)). El paquete principal debe responder a una division por funcionalidad, mientras que internamente se maneja por capas. Con esto queremos decir lo siguiente:


* **net.pladema**.
  * **feature1**
    * **controller**: contiene las clases controller de los endpoints. Cada clase debería tener el manejo de un recurso especifico. No tener una superclase. Definirlas con **@RestController**
      * **dto**:los DTO existen en esta capa y se generen como formato de salida
      * **constraints**: validaciones sobre los parametros de entradas a cada endpoint ([springvalidation](https://www.baeldung.com/spring-boot-bean-validation))
      * **mappers**: necesarios para mapear los outputs de los servicios a los outputs de la api. Ocultar estructura interna al cliente.
      * **Servicios externos**: usados para comunicación entre paquetes de distinta funcionalidad. La idea es tener un control sobre en parte del código tenemos comunicación entre paquetes.
    * **services**: Interfaces claras y concisas de servicios, tampoco super interfaces que hacen de todo. Muy importante su definición! evitar acoplamiento. 
      * **impl**: Clases que implementen las interfaces. Mientras menos dependencías tengan la implementación mejor, más facil de testear y menos acople. Definirlas con **@Service**.
      * **domain**: businessobjects usados en los servicios. Clases propias y necesarias para el servicio.
    * **repositories**: Interfaces de los repositorios (extienden a jpa) o interfaces
        custom. Definirlas como **@Repository**
      * **entity**: entidades del módulo, claves compuestas, vistas. Definir con **@Entity**
      * **projections**: proyecciones para respuestas de repositorios
      * **domain**: value objects para respuestas de consultas en los repositorios
      * **impl**: implementaciones de interfaces customs.
  * **feature2**
    * **controller**
      * **dto**
      * **...**
    * **service**
      * **impl**
      * **...**
    * **...**

Se permite anidamientos de funcionalidad siempre y cuando haya una dependencia fuerte (una no existe sin la otra)


*   **net.pladema.**
    *   **feature1**
        *   **controller**
        *   **service**
        *   **repositorios**
        *   **subfeatures**
            *   **feature1.1**: feature 2 no puede existire sin feature 1
                *   **controller**
                *   **service**
                *   **repositories**
                *   **subfeatures**
            *   **feature1.2**
            **...**
            

El feature debe ser reemplazado por un nombre representativo.

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
@RequestMapping("jur/{idJur}/us/{idUs}/ciclo/{cCiclo}/alumnos")
@Validated
public class AlumnosDatosPersonalesController {

	private static final String VALID_OPERATION = "Valid operation";

	private final Logger logger;

	private final AlumnoDatosPersonalesService alumnoDatosPersonalesService;

	public AlumnosDatosPersonalesController(AlumnoDatosPersonalesService alumnoDatosPersonalesService) {
		super();
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.alumnoDatosPersonalesService = alumnoDatosPersonalesService;
	}

	@SinideAudit(value = "Get datos asistencia toma", logBody = false)
	@GetMapping
	public ResponseEntity<List<AlumnosDatosPersonalesResponse>> getDatosPersonales(@PathVariable("idJur") Short idJur,
			@PathVariable("idUs") Integer idUs, @PathVariable("cCiclo") Short cCiclo,
			@RequestParam(name = "idAlumnos", required = false) Set<Integer> idAlumnos) {
		logger.debug("{}", VALID_OPERATION);
		List<AlumnosDatosPersonales> result = alumnoDatosPersonalesService.getDatosPersonales(idUs, cCiclo, idAlumnos);

		List<AlumnosDatosPersonalesResponse> salida = result.stream()
				.map(adp -> new AlumnosDatosPersonalesResponse(adp.getId(), adp.getNombre(), adp.getApellido(),
						adp.getNroDocumento(), adp.getcTipoDocumento(), adp.getcSexo()))
				.collect(Collectors.toList());
		return ResponseEntity.ok(salida);
	}

}
```

###  Inyección de dependencias

Se define utilizar la inyección de dependencias mediante el uso de [Constructor injection](https://dzone.com/articles/spring-di-patterns-the-good-the-bad-and-the-ugly). 

```java
@Service
public class PersonServiceImpl implements PersonService {

	private static final Logger LOG = LoggerFactory.getLogger(PersonServiceImpl.class);

	private final PersonRepository personRepository;

	private final GeocodingService geocodingService;

	private final PersonLocationRepository personLocationRepository;

	public PersonServiceImpl(PersonRepository personRepository, PersonLocationRepository personLocationRepository,
							 GeocodingService geocodingService) {
		super();
		this.personRepository = personRepository;
		this.personLocationRepository = personLocationRepository;
		this.geocodingService = geocodingService;
	}
}
```

### Generales


Respetar los patrones [SOLID](https://www.youtube.com/watch?v=2X50sKeBAcQ&t=68s), principalmente el de **Single responsability**. Este se centra en que cada función, interfaz, etc tenga un solo mótivo por el cuál debe cambiar. Esto nos permite generar test unitarios con un scope alcanzable (testear una función que hace de todo es imposible e impractico). 

Reducir el control de valores distinto de null. Reemplazar en lo posible con los [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html) de java. Los repositorios pueden devolver Optionals de objetos. 


### DTO (Data transfer objects)

Representan los datos de salida y de entrada para la API del sistema, por lo cuál tienen que ser lo más minimalista posible. 

#### Typescript generator

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

Para que los DTOs puedan ser detectados por el plugin se deben encontrar bajo un paquete controller y tienen que finalizar la palabra Dto. Esto es configurable desde el [pom.xml](../hospital-api/pom.xml)

```xml
<classPatterns>
	<pattern>net.pladema.**.controller.**Dto</pattern>
</classPatterns>
```

### Fechas

Dado que estamos usando Java 11 se definen usar los nuevos tipos de [fecha](https://docs.oracle.com/javase/8/docs/api/java/time/package-summary.html) dejando de lado Date, Calendar, JodaTime. Basicamente usar

1. **LocalDateTime**: fechas con tiempo
2. **LocalDate**: solo fechas
3. **LocalTime**: solo horas


### Lombok

Se dispuso de la libreria [lombok](https://projectlombok.org/) que nos permite crear clases sin la necesidad de escribir todos sus getters, setters, etc. De esta manera nos ahorramos la escritura de código.  Para configurarlo se puede hacer uso de la siguiente [guia](https://www.baeldung.com/lombok-ide)


### Base de datos

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

#### Uso de entidades simples

Se descarta el uso de los hibernate mappings (ManyToOne, OneToOne, OneToMany, etc). 

La decisión de usar entidades con atributos simples (Boolean, Short, Integer, etc) y evitar tener entidades con objetos a otras entidades, se debe a cuestiones de performance y testing. 
El mal uso de esta anotaciones puede impactar de manera negativa en la performance, mientras que genera complejidad en la generación de datos mockeados para los test unitarios.

#### Nomenclatura de entidades

Se deben respetar las siguientes formas de definir las entidades:

1. **Primary key**: nombre **id** cuando es un atributo simple. Cuando es clave compuesta usar @Embbedable y ponerle nombre **pk** al atributo dentro de la entidad que represente la primary key.
2. **Foreign key**: cualquier atributo que represente una foreign key debe tener el siguiente formato <nombre_entidad>_id. Donde nombre_entidad es el nombre al cuál hace referencia.
3. **Nullable**: Aclarar cuando los atributos no son nullables.
4. **Length strings**: Definir el largo de los strings.


#### Datos maestros

Los datos maestros hacen referencia a aquella información que es utilizada por el sistema para representar estados, tipos, categorías, etc. Esta información principalmente se puede representar de dos formas distintas: tabla en la base de datos o enumerados en el código.

La primera representación consiste en una **tabla** que generalmente contiene una **clave** y una **descripción**. Algunas ventajas y desventajas de la misma son:

**Ventajas**

* Son dinamicas en su uso (se puede agregar en tiempo de ejecución nuevos valores, actualizar descripciones etc, siempre y cuando no requiera cambios en la lógica del backend)
* Menos limitados en cuanto a cantidad de información
  
**Desventajas**

* Generación extras de scripts sql tanto para su creación como para la generación de claves extranjeras.
* Generación extras de código en java (Repositorios, entidades).
* Nos obliga realizar consultas de base de datos cuando queremos chequear alguna lógica de negocio en el código.
* El esquema de base de datos se vuelve más complejos al igual que las consultas.


Por otra parte, la segunda representación consiste en hacer uso de los **enumerados** de java. Para este escenario las ventajas y desventajas serían:

**Ventajas**

* Más sencillo de implementar.
* Menor cantidad de llamados a base de datos para validaciónes.
* Facilmente accesible desde cualquier parte del código.
* Reduce joins en consultas de base de datos (impacta en performance)
  
**Desventajas**

* No se pueden cambiar en tiempo de ejecución.
* Limitaciones en la cantidad de datos que puede almacenar el enumerado.
  
Es importante aclarar, que para el uso de enumerados si bien nos ahorra la generación de una tabla en la base no descarta la necesidad de generar una columna en la tabla que lo use. 

La mejor opción de tipo de columna es usar el **varchar** que lo representa porque se puede saber facilmente en la tabla que valor tiene. Sin embargo, si el uso del tipo **varchar** impacta negativamente en la performance de la tabla (consultas sobre dicha columna, espacio que termina ocupando en la base en general), entonces se podria generar un id númerico a la clase enumerada para cada opción. 

Por otra parte, es importante mencionar que los distintos motores de base de datos tienen representaciones especificas para los datos de tipo enúmerados. Hay que tener en cuenta las ventajas y desventajas (portabilidad del código) que puede tener utilizarlas. Algunos de estos tipos contiene restricciones que pueden generar bugs si no se usa correctamente. Por ejemplo, el uso del tipo **ORDINAL** requiere que se agregue siempre al final.

**Conclusión**

Teniendo en cuenta lo mencionado anteriormente, se procede a dejar algunas reglas a tener en cuenta a la hora de decidir si utilizar un tipo de representación u otra.

1. Sí la información es abundante y puede cambiar dinamicamente en tiempo de ejecución entonces la mejor representación probablemente sea la **Tabla** (por el contrario enumerado)
2. Sí la información es usada constantemente en código y un cambio en los posibles valores obligue a una nueva compilación entonces un **enumerado** resolvería el problema.


Cuando no se tiene certeza del escenario siempre se puede empezar con un **enumerado** al menos que funcionalmente se requiera una tabla. El enumerado va a ahorrar tiempo al principio y no presenta dificultad a la hora de cambiar su representación por una tabla.


### Logging

Hacer uso de los loggers para mostrar información, obligatoriamente uno de nivel debug por función. Los mismos nos ayudan a conocer ante una situación de error que esta sucediendo. Un ejemplo de uso es el siguiente:

Primer linea de cada función creada.
```java
LOG.debug("Input parameters id {}, parameter2 {}", id, parameter2);
```
Con esto si se activa el modo debug podemos ver que esta pasando.

Última linea de cada función
```java
LOG.debug("Output {}", objectOutput.toString());
```

Para saber que resultado genera.
        
Mostrar excepciones:

```java
LOG.error("Error-> {}", e); // e es de algún tipo de excepción.
``` 


### Manejo de errores

Evitar en lo posible capturar errores y arrastrarlo a través de las clases. Si es necesario capturarlos internamente y darle un tratamiento. De no ser posible lo mejor es lanzar la excepción y que lo manejen capas más altas. 


Generalmente, si hay errores de validación en la API, se lanzan y se manipulan utilizando lo comentado en el siguiente [post](https://www.baeldung.com/global-error-handler-in-a-spring-rest-api)


### Testing

Realizar test unitarios principalmente en funcionalidad compleja que sea propensa a error.

Es importante entender el concepto de que si una función en su código **llama a otro método**, ya sea de la misma clase o de otra, estas deben ser **Mockeadas** (simular su respuesta). Esto es así porque el test unitario se centra en evaluar **solamente** las lineas de código que contiene (sus bifurcaciones, sus for, los caminos posibles que puede tomar un thread dentro del código). 


Si respetan los principios SOLID, la responsabilidad de la clase, sus dependencias, etc, van a facilitar el testing. Una clase con mucha responsabilidad y dependencias termina generando un test demasiado complejo de realizar y mantener.

Ejemplos de testing,


**Controllers**

```java
@RunWith(SpringRunner.class)
@WebMvcTest(BackofficeHealthController.class)
public class BackofficeHealthControllerTest extends BaseControllerTest {
	@MockBean
	private HealthRepository repository;

	@MockBean
	private HealthService healthService;

	@Before
	public void setup() {
		HealthState healthState = HealthStateBean.newHealthState(5, 9);
		healthState.setAudit(AuditBean.newAudit(4));

		when(repository.findAll(
			any(Example.class),
			any(Pageable.class)
		)).thenReturn(new PageImpl<>(
			Arrays.asList(healthState)
		));

	}

	@Test @WithMockUser
	public void getList_datesWithTZ() throws Exception {
		mockMvc.perform(get("/backoffice/healths"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value(1))
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(jsonPath("$.content[0].id").value(5))
				.andExpect(jsonPath("$.content[0].severityId").value(9))
				.andExpect(jsonPath("$.content[0].createdOn").value("2004-04-04T04:04:04.000Z"))
				.andExpect(jsonPath("$.content[0].updatedOn").value("2004-04-04T04:04:04.000Z"));
	}
}
```

**Service**

```java
@RunWith(SpringRunner.class)
public class RoleServiceImplTest {

	@MockBean
	private UserRoleRepository userLicenseRepository;

	@MockBean
	private RoleRepository licenseRepository;

	private RoleServiceImpl licenseServiceImpl;

	@Before
	public void setUp() {
		licenseServiceImpl = new RoleServiceImpl(userLicenseRepository, licenseRepository);
	}

	@Test(expected = EntityNotFoundException.class)
	public void createUserLicense_notExistLicense() {
		when(licenseRepository.findByDescription(any())).thenReturn(Optional.empty());

		User user = createUser("username9@mail.com");
		licenseServiceImpl.createUserRole(user.getId(), ERole.PATIENT_USER);
	}

	@Test
	public void createUserLicence() {
		Short roleId = 1;
		Role role = new Role();
		role.setId(roleId);
		role.setDescription("PRUEBA");

		User user = createUser("username9@mail.com");

		when(licenseRepository.findById(roleId)).thenReturn(Optional.of(role));
		when(userLicenseRepository.save(any())).thenReturn(new UserRole(user.getId(), role.getId()));

		assertThat(licenseServiceImpl.createUserRole(user.getId(), roleId)).isNotNull();

	}
}
```

**Repositorio**
```java
@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class HealthRepositoryTest extends BaseRepositoryTest {

	private final static Pageable UNPAGED = Pageable.unpaged();

	@Autowired
	private HealthRepository repository;

	private BackofficeHealthQueryAdapter queryAdapter = new BackofficeHealthQueryAdapter();

  @Before
	public void setUp() throws Exception {
		save(HealthStateBean.newHealthState(1, 1, HealthStateBean.BREATHMISSING));
		save(HealthStateBean.newHealthState(2, 2, HealthStateBean.BREATHMISSING, HealthStateBean.FEVER));
		save(HealthStateBean.newHealthState(3, 0));
		save(HealthStateBean.newHealthState(4, 2, HealthStateBean.BREATHMISSING, HealthStateBean.FEVER, HealthStateBean.MUCUS));
	}

  @Test
	public void findAll_byPersonId() {
		HealthState healthState = new HealthState();
		healthState.setId(3);

		assertThat(queryByExample(healthState))
				.isNotNull()
				.hasSize(1)
				.anySatisfy(healthStateData -> assertThat(healthStateData.getId()).isEqualTo(3));
	}
```

Las clases que se utilizan para el test deben respetar las ubicaciones en los paquetes que tiene sus pares en el código. Y su nombre debe ser igual a la de su par finalizado con la palabra Test.

### Conclusión

Estas son un conjunto de convenciones que ayudan a tener un desarrollo más estructurado para todos. La idea es mejorar paso a paso e ir incorporando nuevas prácticas. 



## Material de lectura

En esta sección se deja una lista de links utiles para aprender el uso de las tecnologías aplicadas en el backend


* [Video](https://www.youtube.com/watch?v=RnxzMVD3QsE&ab_channel=SpringFrameworkGuru) explicativo de como hacer una api rest sencilla con spring boot
* [Tutorial](https://www.baeldung.com/mapstruct) de [mapstruct](https://mapstruct.org/) libreria que usamos para mapear dtos a nuestros objetos del dominio.
* [Tutorial](https://www.baeldung.com/intro-to-project-lombok) de [lombok](https://projectlombok.org/) libreria usada para reducir la escritura de código repetitivo. 
* [Baeldung](https://www.baeldung.com/) una muy buena referencia para encontrar ejemplos de uso de las librerias de spring boot combinadas con otras. En el siguiente [link](https://www.baeldung.com/rest-with-spring-series) hay un resumén de un conjunto de tutoriales que pueden seguir sobre spring boot.