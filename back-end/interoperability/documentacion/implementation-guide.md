![logo](back-end/interoperability/documentacion/images/logo.png)

El presente documento es una guía de implementación para el módulo `interoperability`. Su objetivo principal es definir algunos conceptos básicos, presentar las propiedades de ambiente necesarias, explicar los flujos y endpoints definidos para cada perfil — *emisor y visor* —  y detallar algunas consideraciones importantes al momento de interactuar con el módulo. 

## Índice General 

- [Acerca de](#acerca-de)
- [Configuración y propiedades de ambiente](#%EF%B8%8F-configuración-y-propiedades-de-ambiente)
- [Servidor FHIR — *perfil emisor* —](#-servidor-fhir-perfil-emisor-)
    - [Interceptores emisor](#interceptores-emisor)
        - [Cors](#-cors)
        - [Authorization](#-authorization)
        - [Validator](#-validator)
    - [Narrativas](#narrativas)
    - [Providers](#providers)
- [Documentos de historia clínica](#documentos-de-historia-clínica)
    - [Soporte de documentos clínicos](#soporte-de-documentos-clínicos)
    - [Generación de contenido](#generación-de-contenido)
        - [Acceso a la información](#-acceso-a-la-información)
        - [Recursos FHIR](#-recursos-fhir)
        - [Clases Utilitarias](#clases-utilitarias)
        - [Documento IPS](#-documento-ips)
- [Cliente FHIR — *perfil visor* —](#-cliente-fhir-perfil-visor-)
    - [Interceptores visor](#interceptores-visor)
    - [Solicitar HCE en otro dominio](#solicitar-hce-en-otro-dominio)


## Acerca de
La versión de la librería HAPI FHIR es la 5.0.1 con la especificación (estándar) FHIR R4 versión 4.0.1.

## ⚙️ Configuración y propiedades de ambiente
[Volver al índice](#índice-general)  

El módulo `interoperability` sólo estará disponible para operar cuando el servicio Federar se encuentre habilitado. Por ello, se diseñó una condición específica que aplica a los principales componentes del módulo. 

```java
@Conditional(InteroperabilityCondition.class)
```

Esta condición habilita a Spring la inyección de componentes si la propiedad `ws.federar.enabled` está configurada en valor True. En ambientes de desarrollo, se deberá activar el perfil `dev` para configurar todas las propiedades restantes necesarias. — *Ver archivo application-dev.properties* —

En ambientes productivos, la habilitación del módulo bajo perfil *emisor/servidor* requiere configurar la propiedad `ws.federar.claims.sub` que representa la URI del dominio local. Si la propiedad no está presente la aplicación emitirá la siguiente excepción:

```java
Caused by: java.lang.IllegalArgumentException: Could not resolve placeholder ws.federar.claims.iss in value "${ws.federar.claims.iss}"
```

Por otro lado, en ambientes productivos, la habilitación del módulo bajo perfil *visor/cliente* hará requeridas otras propiedades para realizar operaciones relacionadas con los pacientes (búsquedas de pacientes existentes, federación de nuevos pacientes, etc.) con previa autenticación ante el Federador.

| Nombre | descripción |
| ------ | ------ |
| ws.federar.claims.sub | Nombre del dominio |
| ws.federar.claims.name | Apellido y Nombres del Usuario que accede (no es necesario que hayan sido registrados ante la DNGISS)|
| ws.federar.claims.role | Especialidad del Usuario (no es necesario que hayan sido registrados ante la DNGISS) |
| ws.federar.claims.ident | Un identificador para el usuario (no es necesario que hayan sido registrados ante la DNGISS) |
| ws.federar.auth.signKey | Palabra secreta, única y cifrada asignada por la DNGISS para cada dominio |


**Otros datos de interés:**

1. Bus de interoperabilidad para desarrollo (pruebas): http://mhd.sisa.msal.gov.ar/fhir/
2. Para sistemas productivos, se debe solicitar acceso a la Red mediante `soporte@sisa.msal.gov.ar`. 


## 🍁 Servidor FHIR — *perfil emisor* — 

La integración de Fhir al proyecto se logró incorporando las bibliotecas HAPI-FHIR desarrolladas para el lenguaje Java y agregando un servlet para simular las interacciones de un servidor FHIR.

```java
@WebServlet( urlPatterns = "/fhir/*", displayName = "Fhir Server")
public class FhirServerR4 extends RestfulServer {
}
```

Este servlet capturará todas las peticiones bajo el patrón `{context}/api/fhir/`. Dichas peticiones deben configurarse implementando la interfaz `IResourceProvider` provista por Fhir [Ver más](#providers). Se espera que las peticiones sólo provengan del bus de interoperabilidad, cuando otro dominio requiera información de un paciente federado en el dominio local. El objetivo es que el bus actúe siempre como mediador en la comunicación entre dominios.

### Interceptores emisor

Todos los interceptores registrados en el Servidor se encuentran definidos en el paquete `net.pladema.hl7.supporting.security`. 

#### 📌 Cors  

Para el soporte de clientes Javascript es necesario la habilitacion de CORS — *Cross Origin Resource Sharing* —. El interceptor ApiCorsInterceptor cumple esta función añadiendo la configuración necesaria. 

```java
private CorsConfiguration configuration(){
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedHeader(HttpHeaders.ACCEPT);
    configuration.addAllowedHeader(HttpHeaders.CONTENT_TYPE);
    configuration.addAllowedHeader(HttpHeaders.ORIGIN);

    configuration.addAllowedOrigin("*");

    configuration.addExposedHeader("Location");
    configuration.addExposedHeader("Content-Location");
    configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
    return configuration;
}
```

#### 📌 Authorization  

Cada petición entrante será interceptada para validar su procedencia, por ello cada una debe especificar un parámetro llamado **Authorization** en el **HEADER** con un valor de **Token**. En ausencia de este parámetro la petición es rechazada. 
La validación del Token se logra a través del servicio `/bus-auth/tokeninfo` del **Federador**. 

```java
@Component
public class ServerAuthInterceptor extends AuthorizationInterceptor {
}
```

![Auth-Interceptor](https://hapifhir.io/hapi-fhir/docs/images/hapi_authorizationinterceptor_read_normal.svg)

#### 📌 Validator  

En cada respuesta del servidor, los recursos Fhir son validados respecto a las reglas de validación oficial de Fhir, éstas incluyen validación del contenido según la especificación de cada recurso, cardinalidad para cada propiedad, chequeo de referencias, codificación apropiada para cada dominio, etc. 

```java
@Interceptor
public class ApiResponseValidatingInterceptor extends ResponseValidatingInterceptor {
}
```

El interceptor opera bajo un módulo — *ApiFhirInstanceValidator.java* — el cuál fue creado como una cadena de otros módulos ya provistos por la librería:
- DefaultProfileValidationSupport
- SnapshotGeneratingValidationSupport
- InMemoryTerminologyServerValidationSupport
- CommonCodeSystemsTerminologyService

Además, se agregaron las definiciones oficiales de Fhir — *profiles* — expresadas bajo los recursos `StructureDefinition`, `CodeSystem` y `ValueSet`.Estas definiciones constituyen el soporte para la terminología empleada en los recursos en base a diferentes codificaciones estándares. Este soporte se encuentra definido en el paquete `net.pladema.hl7.supporting.terminology`. 

**Por cada recurso Fhir utilizado**:
- Bajo el sub-paquete `support` se debe crear la clase profile correspondiente. Ésta debe incluir al menos la definición de un recurso — *StructureDefinition, CodeSystem o ValueSet* —. Estas definiciones ya se encuentran implementadas en la clase `TerminologySupport`. 
- Bajo el sub-paquete `coding` se debe agregar la codificación correspondiente teniendo en cuenta:
    - Clase **CodingCode**: Valores de códigos permitidos para cada sistema. 
    - Clase **CodingProfile**: Datos requeridos para la definición del recurso `StructureDefinition`.
    - Clase **CodingSystem**: URI's de sistemas de codificación estándares — `CodeSystem` —, como **SNOMED CT** y **Loinc** y de sistemas involucrados en la interoperabilidad como **REFES**. 
    - Clase **CodingValueSet**: Datos requeridos para la definición del recurso `ValueSet`.
- Agregar el Perfil del recurso en el módulo de Validación. 

```java
private PrePopulatedValidationSupport terminologySuppport(FhirContext context){
}
```

### Narrativas

Cada recurso definido en Fhir — *con excepción de Bundle, Parameters y Binary* — debe incluir un texto legible llamado `narrative` que represente un resumen del contenido del recurso. Principalmente, una narrativa eberá reflejar el contenido necesario para que una persona comprenda la información esencial, clínica y/o comercial para el recurso. 
Para la generación de narrativas se utiliza la librería Thymeleaf ya que provee la sintaxys XHTML que se adapta muy bien a los objetos HAPI-FHIR.

Los templates XHTML fueron creados en la carpeta **Resources** del módulo, los cuales se especifican en el archivo `narratives.properties` usado por el servidor. 

Adicionalmente, se implementó un mecanismo genérico para las narrativas de secciones de un recurso **Composition** — *utilizado en Documentos Fhir* — 

```java
public class FhirNarrative {

    public static Narrative buildNarrative(List<Resource> resources){
    }
}
```

### Providers 
Como parte de la integración a la Red Nacional de Interoperabilidad fue requesito la implementación de dos endpoints como operaciones **REST** siguiendo el [estándar](https://www.hl7.org/fhir/http.html) de implementación definido por **FHIR**.

De esta manera, en el paquete `net.pladema.hl7.supporting.exchange.restful` fueron creadas dos clases cuyos nombres coinciden con el nombre del recurso y agrega como sufijo la palabra `Provider`. Cada clase, `DocumentReferenceProvider` y `BundleProvider` implementa el endpoint correspondiente. Un recurso Provider debe implementar la interfaz `IResourceProvider` y contendrá uno o más métodos marcados con anotaciones especiales que indican el tipo de operación RESTful que admite ese método.

1. 📌 GET fhir/DocumentReference — *operación Search* —

Este servicio permite conocer todos los documentos clínicos en los que se tiene información del paciente. Si bien la aplicación sólo permite ensamblar documentos del tipo [IPS](http://hl7.org/fhir/uv/ips/), a futuro pueden intercambiarse otros documentos de un mismo paciente, es decir que la aridad de la relación *paciente - documento* es uno a muchos.

El endpoint debe devolver un recurso Bundle — *puede pensarse como una lista* — de recursos **DocumentReference**. Un recurso *DocumentReference* es una referencia a un documento FHIR, contiene metadatos necesarios para tener información del documento, sin necesidad de acceder al documento por completo. 

```java
@GetMapping(value = "/DocumentReference")
@Search
public Bundle getExistingDocumentsReferences(
        @RequiredParam(name = DocumentReference.SP_SUBJECT) String subject,
        @RequiredParam(name = DocumentReference.SP_CUSTODIAN) String custodian,
        @OptionalParam(name = DocumentReference.SP_TYPE) String type){
    //build parameters appropriately
    TokenParam subjectParam = FhirParam.newTokenParam(subject);
    TokenParam custodianParam = FhirParam.newTokenParam(custodian);
    TokenParam typeParam = Objects.requireNonNullElseGet(
            FhirParam.newTokenParam(type), FhirDocument::defaultType);
    return bundleResource.getExistingDocumentsReferences(subjectParam, custodianParam, typeParam);
}
```

- **Subject**: URI del dominio que recibe la petición acompañado del identificador del paciente — *previamente federado en el dominio* —. 
- **Custodian**: URI del dominio que recibe la petición.
- **Type**: [Tipo](https://www.hl7.org/fhir/valueset-c80-doc-typecodes.html) de documento clínico que se solicita. El valor por defecto de este parámetro es `http://loinc.org|60591-5` — ***I**nternational **P**atient **S**ummary* —.

**Ejemplo de invocación del método**

```sh
curl -X GET 'http://sgh.pladema.net/api/fhir/DocumentReference?subject:identifier=http://www.msal.gov.ar%7C3&custodian=http://www.msal.gov.ar&type=http://loinc.org%7C60591-5' -H 'Authorization: HsC9%x-r?F'
```

Un ejemplo del mismo servicio a través del bus de interoperabilidad usado para desarrollo/pruebas:

```sh
curl --location --request GET 'http://mhd.sisa.msal.gov.ar/fhir/DocumentReference?subject:identifier=http://dummy.com.ar%7C1&custodian=https://conn24dummy.org.ar&type=http://loinc.org%7C60591-5' \
--header 'Authorization: Bearer jwt'
```

Los pacientes que pueden consultarse son:
- http://dummy.com.ar|1
- http://dummy.com.ar|2
- http://dummy.com.ar|3


2. 📌 GET fhir/Bundle — *operación Read* —

Tratándose de una operación Read, solamente se recibe un identificador de documento. Este identificador es interno y puede tener cualquier valor que el implementador considere conveniente, respetando siempre los lineamientos del estándar. Este **ID** es tomado desde el atributo `url` del objeto `content/attachment` del servicio `/DocumentReference`. 
En el módulo se utilizó el Identificador de paciente. 

El endpoint es responsable de ensamblar el documento clínico solicitado a través de una composición de diferentes recursos FHIR. [Ver más](#-recursos-fhir).

```java
@GetMapping(value = "/Bundle/{id}")
@Read
public Bundle assembleDocument(@IdParam IdType id){
    return bundleResource.assembleDocument(id);
}
```

**Ejemplo de invocación del método**

```sh
curl -X GET 'http://sgh.pladema.net/api/fhir/Bundle/16' -H 'Authorization: HsC9%x-r?F'
```

Para visualizar el documento IPS de manera más cómoda, el Ministerio de Salud provee esta [herramienta demo](https://salud-ar.github.io/IPS-Argentina/demo.html), en la cual basta con pegar la respuesta JSON obtenida del servicio anterior. 

## Documentos de historia clínica

Cualquier documento FHIR tiene la misma estructura: Un recurso ***Bundle*** del tipo ***document*** que tiene un recurso ***Composition*** como primer recurso del Bundle, seguido de una serie de otros recursos, a los que se hace referencia en el recurso ***Composition*** y que proporcionan evidencia de respaldo para el documento. 
El recurso Composition es la base del documento clínico, contiene información relevante — *autor, contexto, propósito, etc.* — y además divide el contenido en secciones cada una con su propia narrativa.

### Soporte de documentos clínicos 

Desde el módulo se creo un paquete específico para el manejo de documentos FHIR. 

```java
package net.pladema.hl7.supporting.exchange.documents.profile
```

Actualmente la aplicación sólo permite el intercambio de documentos [IPS](#documento-ips). Para dar soporte a un nuevo documento se debe crear una clase y extenderla de la interfaz `IDocumentProfile` para implementar los métodos allí definidos. Esta nueva clase debe llevar la anotación Spring `@Component` y `@Conditional`.

```java
@Component
@Conditional(InteroperabilityCondition.class)
public class XDocument extends IDocumentProfile {

    @Override
    public List<Bundle.BundleEntryComponent> getContent(String patientId) {
        //here are the rules for the specific composition document
    }

    @SafeVarargs
    @Override
    public final List<Composition.SectionComponent> sections(List<Bundle.BundleEntryComponent>... entries) {
        //for each entry add a new section using CompositionResource.newSection()
    }
}
```

Tras crear la clase con la especificación del nuevo documento, en la clase `FhirDocument` se debe agregar a la lista de documentos soportados y de esta manera, pueda ser solicitado por otros dominios. 

```java
static {
    //Add all fhir documents supported
    supported.put(PatientSummaryDocument.TYPE, PatientSummaryDocument.class);
}
```

### Generación de Contenido

La información del documento clínico se obtiene principalmente de la conformación de cada recurso FHIR y del uso de la clase `CompositionResource` implementada de manera genérica para cualquier documento. 


#### 📌 Acceso a la información 

El módulo `interoperability` está diseñado para obtener la información registrada del paciente accediendo directamente a la base de datos. De esta manera, el módulo se independiza de las entidades y repositorios gestionados en el módulo `hospital-api`.

La obtensión de datos del paciente y otros datos relevantes como la institución, son  obtenidos a través de consultas directas diseñadas en la clase `FhirPersistentStore`. 

```java
package net.pladema.hl7.supporting.exchange.database;

@Repository
public class FhirPersistentStore {

    private final EntityManager entityManager;
}
```
Los resultados de las consultas son capturados en clases del tipo VO con métodos y atributos necesarios para construir cada uno de los recursos FHIR requeridos. Todas estas clases han sido implementadas bajo el paquete `net.pladema.hl7.dataexchange.model.domain`.

#### 📌 Recursos Fhir 

Para generar cada uno de los recursos según la definición FHIR, se implementaron dos interfaces `ISingleResourceFhir` y `IMultipleResourceFhir`. Cada interfaz define el método `fetch(String id, Reference[] references)` responsable de tomar la información de la base de datos y construir a partir de ella, el recurso FHIR correspondiente. Cada clase creada para la especificación de un recurso debe implementar una de las dos interfaces descriptas, si el recurso Fhir presenta el carácter de unicidad se deberá implementar la interfaz `ISingleResourceFhir`, caso contrario, si un recurso se presenta generalmente como una lista, por ejemplo, medicamentos, alergias, etc. se deberá implementar la interfaz `IMultipleResourceFhir`. 

```java
@Component
public abstract class ISingleResourceFhir extends IResourceFhir {

    public abstract IBaseResource fetch(String id, Reference[] references);
}
```

```java
@Component
public abstract class IMultipleResourceFhir extends IResourceFhir {

    public abstract List<? extends IBaseResource> fetch(String id, Reference[] references);
}
```

Además, ambas interfaces implementan el método fetchEntries(String id, Reference[] references) para generar la/s entrada/s especificas en el recurso Bundle durante el ensamblado del documento clínico.

**Recursos Implementados:**

**ISingleResourceFhir**:

```java
package net.pladema.hl7.concept.administration;
```

- DeviceResource: recurso para resumir los datos del sistema generador del documento clínico.
- OrganizationResource: recurso para resumir los datos de la organización generadora del documento clínico — *dominio o institución REFES según corresponda* —.
- PatientResource: recurso para resumir los datos del paciente de quién se generó el documento clínico.

```java
package net.pladema.hl7.supporting.exchange.documents;
```

- CompositionResource: recurso específico para representar la estructura e información resumida de un documento clínico.

**IMultipleResourceFhir**:    

```java
package net.pladema.hl7.dataexchange.clinical;
```

- AllergyIntoleranceResource: recurso específico para el registro de alergias e intolerancias.
- ConditionResource: recurso específico para el registro de problemas de salud (problemas, antecedentes, diagnósticos).

```java
package net.pladema.hl7.dataexchange.medications;
```

- ImmunizationResource: recurso específico para el registro de vacunas administradas.
- MedicationResource: recurso específico para el registro de medicamentos.
- MedicationStatementResource: recurso específico para el registro de medicación que un paciente ha tomado, esté tomando o que tomará en el futuro. 

Si el paciente no tuviera registrada información respecto a alguna sección, también debe informarse con la codificación específica de cada recurso. A nivel implementación se definió el método `noInformationAvailable(Reference[] references)`  para cumplir con este propósito.  

**Nuevo recurso a implementar**:
- Crear una clase específica cuyo nombre tenga el nombre del recurso FHIR y la palabra **Resource** como sufijo.
- Extender la nueva clase a la interfaz correspondiente, ISingleResourceFhir o IMultipleResourceFhir según la cardinalidad común del recurso e implementar el método requerido `fetch(String id, Reference[] references)`.  
- Agregar a la clase las siguientes anotaciones: `@Service` y `@Conditional(InteroperabilityCondition.class)`.
- Implementar un constructor para la clase inyectando el bean FhirPersistentStore.


#### Clases Utilitarias 

El módulo cuenta con clases utilitarias que dan soporte a la generación de cada recurso. Dichas clases pueden encontrarse en el siguiente paquete:

```java
package net.pladema.hl7.dataexchange.model.adaptor;
```

- IResourceFhir: construye todas las estructuras o tipos que definen los recursos FHIR. 
- Cast: Mapeo entre la información obtenida de la base de datos y el tipo de datos requerido por el recurso FHIR.
- FhirAddres: construye un domicilio teniendo en cuenta que cierta información pudo haber sido omitida.
- FhirCode: representación de una codificación expresada en valor y descripción. 
- FhirDateMapper: Mapeo entre diferentes representaciones de fecha.
- FhirID: construye identificadores **UUID** aleatorios para los recursos incluidos en las entradas de un Bundle.
- FhirNarrative: construye los textos de las secciones de un recurso Composition. 
- FhirParam: representación y contrucción de parámetros. 
- FhirString: funciones con cadenas de texto comunes a cualquier recurso.


#### 📌 Documento IPS 

Un Resumen Internacional de Paciente — *IPS* — es un extracto de registro de salud electrónico que contiene información médica esencial para la atención de un paciente. La especificación define una serie de datos demográficos y secciones obligatorias y opcionales — *sólo las secciones obligatorias son incluidas en el recurso Composition* —. Estas secciones contienen un texto con el contenido clínico, y la codificación de este contenido en una entrada (entry).

![esquema](images/ips-esquema.png)


El soporte del documento se definió en la clase `PatientSummaryDocument`.


## 🍁 Cliente Fhir — *perfil Visor* —

El objetivo de incoporar un cliente FHIR en la aplicación es el de solicitar la historia clínica de un paciente a través del Bus de Interoperabilidad. Previamente, el paciente debe encontrarse federado con el dominio local.  

La configuración Cliente se definió en la clase `FhirClientR4` la cuál utiliza la interfaz `IFhirClient` que define todos los servicios que serán consumidos del Bus. 

### Interceptores visor

Cuando sos servidor, el otro dominio hizo auth... y el bus te pasa ese token a vos
Si vos sos cliente, vos usas auth para generar el token, se lo das al bus, y el bus se lo da al otro dominio quien hara tokeninfo
En el medio el bus tambien hace tokeninfo.. pero bueno. Nunca esta de mas la validacion

```java
package net.pladema.hl7.supporting.security;

@Component
public class ClientAuthInterceptor extends BearerTokenAuthInterceptor {
}
```

### Solicitar HCE en otro dominio

Se implementaron dos servicios en la clase PatientProvider...

📌 Patient-location
GET api/masterfile-federacion-service/Patient/patient-location

Este servicio realiza una búsqueda de dominios en los que un paciente está federado. De esta manera el profesional de la salud puede ver la lista de dominios y elegir uno en particular. 
Como respuesta, el servicio retornará un recurso **Bundle** con **N** entradas del recurso **Organization**. Cada una de estas entradas conforma un dominio en el cual el paciente ha sido federado. 

**Invocación del servicio en el ambiente de Test**:

```sh
curl -X GET 'https://bus-test.msal.gob.ar/masterfile-federacion-service/fhir/Patient/$patient-location?identifier=https://www.lanus.gob.ar|x999698' -H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJQaDdQQ3VqY1VVaXJQUGRxSldrbTNCSDYyS0k1Q1h0SmZpbnZKTWkxV2kwIn0.eyJqdGkiOiIzNDlkNjZkNi00YzM1LTQyNGQtOGRiYy1kMWIwMDNkYzMyYTYiLCJleHAiOjE1MzEzNDQ4NjEsIm5iZiI6MCwiaWF0IjoxNTMxMzQ0NTYxLCJpc3MiOiJodHRwOi8vNTQuMjA3LjQxLjE0Mjo4MDgwL2F1dGgvcmVhbG1zL2ZlZGVyYWRvciIsImF1ZCI6ImN1cy1tZW5kb3phIiwic3ViIjoiMGEzMzJjYjUtMDY5Zi00YWUyLTlkNDMtMmIzZDI1YTIxNTA1IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiY3VzLW1lbmRvemEiLCJhdXRoX3RpbWUiOjAsInNlc3Npb25fc3RhdGUiOiJhNDQxNTk0OS1hMzMxLTQ1NTQtOTBjMi0yMjZjM2U5YmU4MjciLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwiY2xpZW50SWQiOiJjdXMtbWVuZG96YSIsImNsaWVudEhvc3QiOiIyMDkuMTMuODYuMTAiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzZXJ2aWNlLWFjY291bnQtY3VzLW1lbmRvemEiLCJjbGllbnRBZGRyZXNzIjoiMjA5LjEzLjg2LjEwIiwiZW1haWwiOiJzZXJ2aWNlLWFjY291bnQtY3VzLW1lbmRvemFAcGxhY2Vob2xkZXIub3JnIn0.HhS48SC1B-EtJ65HmfKyLdqzQ0VVp2gp-LKnjXKT_aGpOj0XpvPCgvc3EckkybQjQ41FsM4e6c8_dCGolgSiaUzBRst_5Qz9A1D6iIra2q7VU1MbSUKMUHBCeirGiD-7OXHePo1OArJek09as1-DaPdNUL18SX9Kwz_7MIBz4N9OLpVajlE3KST8mZ4ZLFJoHFSzOkXqFu5004nS5kir4f1KnRxfuocFtfnhjo7_Fu6x66jQOdQMAd3_PMDJ7aMdtcyqOEWEJqsNcNL8gASXljZjyqfXCfAovTlgAgBgZmlewFHzwFnG1au-Ipibspg62F2Wm49ktJCSf93hOacIfA'
```


**Vista simplificada del resultado del servicio anterior**:

![respuesta](images/patient-location.png)

Según se observa en la imagen, el paciente federado en el dominio `https://www.lanus.gob.ar` bajo el identificador `x999698` también se encuentra federado en los dominios:
- http://salud.mendoza.gov.ar
- http://www.salud.lapampa.gov.ar
- https://www.hospitalitaliano.org.ar

📌 Find Patient
GET api/masterfile-federacion-service/Patient

- **Subject**: Identificador del paciente — *previamente federado en el dominio* —. 
- **Custodian**: URI del dominio en el que se busca información del paciente. Este valor es obtenido a través del servicio anterior.
- **Type**: [Tipo](https://www.hl7.org/fhir/valueset-c80-doc-typecodes.html) de documento clínico que se solicita. El valor por defecto de este parámetro es `http://loinc.org|60591-5` — ***I**nternational **P**atient **S**ummary* —.
