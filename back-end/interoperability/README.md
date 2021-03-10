![enter image description here](https://hl7.org/implement/standards/fhir/assets/images/fhir-logo-www.png)
# Interoperabilidad

La interoperabilidad se define como la capacidad de los sistemas de información para intercambiar información, y utilizarla como si se hubiera generado en el mismo sistema. El intercambio de documentos electrónicos basados en estándares permite lograr este objetivo, donde la información compartida puede descomponerse hasta obtener códigos de diagnósticos, valores y rangos de resultados, cantidades de prescripciones, etc.

Los estándares de información en salud permiten integrar la información de toda la vida de un paciente, obtenida de los sistemas de información de cada una de las jurisdicciones, , construyendo para cada paciente una historia clínica nacional, longitudinal, y completa, de utilidad clínica, estadística y de gestión. Contar con información completa, precisa y actualizada resulta en una atención médica más segura y de mejor calidad.

### Estándares seleccionados por el Ministerio de Salud de la Nación
Los estándares de información en salud se aplican a todas las comunicaciones de información clínica entre el Ministerio de Salud de la Nación y todas las jurisdicciones. Algunos servicios:

 1. Renaper 
 2. Coberturas
 3. Federador Nacional de Pacientes
 4. Inmunizaciones para Nomivac
 5. Resumen de Historia Clínica (IPS)
 6. Consulta REFEPS
 7. Receta Digital Interoperable
 8. Consulta REFES 
 9. Padrón SUMAR 

**Para solicitar acceso a la red**: soporte@sisa.msal.gov.ar

- *Estándares de terminología*:  

Definen la codificación que se utilizará para representar diagnósticos, síntomas, procedimientos, alergias, productos farmacéuticos, etc.

[SNOMED CT](https://www.argentina.gob.ar/salud/snomed): es el estándar definido para la representación primaria de la información clínica del paciente, es decir, es el vocabulario con mayor nivel de detalle, y que se considera un requisito obligatorio para el intercambio de documentos clínicos interoperables. 

Clasificaciones y catálogos: son vocabularios de utilidad estadística, administrativa o de gestión, que se encuentran vinculados a **SNOMED CT**. Códigos de estas clasificaciones y catálogos pueden obtenerse a partir de códigos de SNOMED CT siguiendo reglas de mapeo, o dependiendo del caso de uso pueden enviarse acompañando a los códigos de SNOMED CT como una codificación adicional.
Algunas clasificaciones y catálogos: 
 1. CIE-10
 2. Códigos de Medicamentos (CUS Medicamentos)
 3. Códigos de prestaciones SUMAR

 - *Estándares de documentos clínicos*: 

Definen la estructura que representan los campos y valores requeridos para los documentos clínicos que implementan los diferentes conjuntos mínimos de datos básicos (CMDB).

[Recursos FHIR](https://www.hl7.org/fhir/resourcelist.html): Para las interacciones con APIs FHIR, en muchas ocasiones los datos intercambiados se transmitirán utilizando los mismos recursos FHIR.
 - *Estándares de comunicación de información*: 

Definen las tecnologías que se utilizan para todas las interacciones entre sistemas de información. 

[HL7 FHIR](https://www.hl7.org/fhir/): Define un conjunto de recursos y operaciones, basadas en estándares Web ampliamente utilizados (XML, JSON, HTTP, OAuth, etc.)

### Estándar HL7 FHIR
FHIR (*Fast Healthcare International resources*) es en términos generales un estándar desarrollado y promovido por HL7 International. HL7 o _**Health Level Seven**_  es una "Organización de Desarrollo de Estándares" para el ámbito de salud. Fundada en 1987, sin fines de lucro, opera a nivel internacional y su misión es proveer estándares globales para los dominios: clínico, asistencial, administrativo y logístico, con el fin de facilitar el intercambio electrónico de información sanitaria. En definitiva, su misión es lograr una interoperabilidad clínica real entre los diferentes sistemas de información presentes en las organizaciones de salud. 

La palabra "Health" (*salud*) refiere al área de trabajo de la organización y las palabras "Level seven" (*nivel siete*) hacen referencia al último nivel del modelo de comunicaciones para sistemas abiertos (OSI) de la Organización Internacional para la Estandarización (ISO). Por lo tanto, se puede afirmar que FHIR es el último estándar de interoperabilidad clínico desarrollado por HL7. 

### Otros estándares
Los estándares de interoperabilidad clínicos existentes (como HL7 v2, v3 o CDA) tenían ciertas limitaciones, entre las que se destacan:
-   HL7 v2 está ampliamente extendido, pero la tecnología es antigua, por lo que no se adecua a los requerimientos actuales.
-   HL7 v3 está basado en un modelo robusto, pero no ha sido muy aceptado debido a su difícil implementación.
-   HL7 CDA fue diseñado como un estándar de documentos y no encaja en otros escenarios.

Por lo tanto, en respuesta a estas y otras cuestiones se desarrolló el estándar FHIR. 

### Fundamentos de FHIR
FHIR parte del concepto fundamental de **RECURSOS**, donde un recurso es la unidad básica de interoperabilidad, la unidad más pequeña que tiene sentido intercambiar. Los recursos son representaciones de conceptos del mundo sanitario: paciente, médico, problema de salud, etc.  FHIR está diseñado específicamente para la web. Los recursos se basan en estructuras XML o JSON que utilizarán un protocolo **REST**.

FHIR se organiza en un [conjunto de "módulos"](https://www.hl7.org/fhir/modules.html) organizados en niveles que representan cada una de las áreas funcionales diferentes de la especificación. En esta estructura se basó la implementación del módulo *interoperability* del proyecto. 

## 

### Contexto

### Bus de interoperabilidad

Según el siguiente fragmento de texto extraído del boletín oficial del Ministerio de Salud y Desarrollo Social de la nación, se define y resuelve la interoperabilidad como: 

>Que por Resolución de la *SECRETARIA DE GOBIERNO DE SALUD* N° 189 de fecha 25 de octubre de 2018 se aprobó la Estrategia Nacional de Salud Digital, la cual establece que la arquitectura de interoperabilidad requiere un rol central de la *SECRETARIA DE GOBIERNO DE SALUD*, actuando como nexo y facilitando la comunicación entre las jurisdicciones y entre los subsistemas de salud y esto requiere la implementación de un Bus de Interoperabilidad que permita la articulación de los contenidos y la comunicación de los registros médicos en el país.
...
Que el Bus de Interoperabilidad otorgará la posibilidad de dar funciones para la indexación y localización de documentos clínicos en una arquitectura nacional de repositorios clínicos distribuidos, a cargo de cada una de las instituciones que genera >el dato.

### Dominios
La Red está compuesta por Dominios que se conectan a través de la infraestructura central llamada Bus de Interoperabilidad mencionada anteriormente.

![dominios](back-end/interoperability/documentacion/images/dominios.png)

Un dominio representa un componente discreto del sistema de salud, puede ser una provincia, municipio, prestador o financiador. Tiene autonomía tecnológica y no envía toda la información que registran, sino que la mantienen en guarda y la comparten solo cuando el paciente lo necesita.

#### ¿Cómo se comunican los dominios entre sí?
![comunicacion-dominios](back-end/interoperability/documentacion/images/comunicacion-dominios.png)

#### ¿Qué se comparte?

![flow](back-end/interoperability/documentacion/images/ips.png)

Un <font color='teal'>***Resumen Internacional de Paciente***</font> (*IPS*) es un extracto de registro de salud electrónico que contiene información médica esencial para la atención de un paciente. Está específicamente dirigido a respaldar el escenario de caso de uso para “atención transfronteriza no planificada”, pero no se limita a ello. Se pretende que sea internacional, es decir, que proporcione soluciones genéricas para aplicaciones globales más allá de una región o país en particular. El conjunto de datos IPS es mínimo y no exhaustivo; agnóstica a especialidad y condición clínica; pero clínicamente relevante.

<font color='teal'>**IPS-AR**</font> es una restricción sobre IPS, sólo restringe el vocabulario con los valores locales (*extensiones de SNOMED y tablas locales*) y sugiere en forma especifica la manera de incluir el texto de las secciones requeridas.

### Implementación
Como patrón de adopción del estándar FHIR al sistema actual se optó por agregar las bibliotecas necesarias que facilitan crear una especie de fachada. Un ejemplo de ello es a través del uso de la biblioteca ***HAPI-FHIR*** para el lenguaje Java. Usando estas bibliotecas se pueden simular las interacciones de un *servidor FHIR* y “mapearlas” a recursos locales.

El módulo ***interoperability*** creado pare este fin utiliza SpringBoot para inicializar un servlet y simular así, un servidor FHIR. De esta manera, la aplicación adopta el perfil de <font color='teal'>**EMISOR**</font>, ya que permite aceptar solicitudes provenientes exclusivamente del bus respondiendo a las mismas con la información clínica de un paciente federado en el dominio local.
Por otro lado, el módulo también adopta el perfil de <font color='teal'>**VISOR**</font> para poder realizar solicitudes a otros dominios a través del bus de interoperabilidad y de esta forma, obtener historias clínicas de pacientes federados en otros dominios.

En el siguiente diagrama de flujo se puede observar de manera completa la interacción entre dominios (*ante cada perfil de adopción*) y el bus actuando como intermediario entre ambos.  

![flow](back-end/interoperability/documentacion/images/flow.png)

<*agregar breve descripción del diagrama*>

## Bibliografía de interés

- [Estrategia de Salud Digital](https://www.argentina.gob.ar/salud/digital) de la República Argentina.
- [Biblioteca de Materiales](https://www.argentina.gob.ar/salud/snomed/biblioteca). 
- [Proyectos Open Source](https://github.com/SALUD-AR) del Ministerio de Salud.
- [Listado de establecimientos de Salud](https://datos.gob.ar/vi/dataset/salud-listado-establecimientos-salud-asentados-registro-federal-refes) (REFES).
- [SISA](https://sisa.msal.gov.ar/sisa/) Sistema integrado de Información Sanitaria Argentino.
- [HL7](https://confluence.hl7.org/display/HL7/Welcome+to+the+Confluence+Pages+of+Health+Level+7+%28HL7%29+International) Confluence Pages.
- [HAPI FHIR](https://github.com/hapifhir/hapi-fhir) - Java API Open Source for HL7 FHIR Clients and Servers. 
- [Resumen de Paciente (IPS)](https://simplifier.net/guide/resumendepacienteips-argentina/home) Guía de implementación.
- [SNOMED CT](https://browser.ihtsdotools.org/?) Navegación y búsqueda de conceptos.
- [SNOMED Argentina](https://www.argentina.gob.ar/salud/snomed/biblioteca) 
- [LOINC FHIR](https://loinc.org/fhir/dev/) Servidor de terminología.
- [SNOWSTORM ](https://github.com/IHTSDO/snowstorm) Proyecto Open Source. 
- [SNOWSTORM ](https://snowstorm.msal.gov.ar/MAIN/concepts) Servidor del ministerio de Salud.
- [SNOWSTORM ](https://snowstorm.msal.gov.ar/swagger-ui.html) Swagger API. 