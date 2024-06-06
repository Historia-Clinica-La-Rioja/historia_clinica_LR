# **Guía de configuración de caché de Snomed**

El objetivo de esta guía es explicar las alternativas y pasos para cargar los grupos de terminología para usar la caché de Snomed.

En la [**sección 1**](#1-cargar-conceptos-en-caché) se explican las diferentes alternativas que existen para cargar los grupos de conceptos en la base de datos. En la [**sección 2**](#2-visualizar-grupos-de-terminología-desde-backoffice-de-hsi) se muestra la visualización de los grupos cargados desde el Backoffice de HSI. Por último, en la [**sección 3**](#3-habilitar-búsqueda-en-caché-de-snomed) se muestra cómo activar las búsquedas que utilicen la caché previamente cargada.

- [1. Cargar conceptos en caché](#1-cargar-conceptos-en-caché)
    - [1.1 Actualizar con tarea programada (Cron)](#11-actualizar-con-tarea-programada-cron)
    - [1.2 Actualizar mediante archivo CSV](#12-actualizar-mediante-archivo-csv)
        - [1.2.1 Generar archivo CSV con base de datos MySQL](#121-generar-archivo-csv-con-base-de-datos-mysql)
            - [1.2.1.1 Puesta en marcha de la base de datos MySQL](#1211-puesta-en-marcha-de-la-base-de-datos-mysql)
            - [1.2.1.2 Generar archivo CSV a partir de ECL en MySQL](#1212-generar-archivo-csv-a-partir-de-ecl-en-mysql)
        - [1.2.2 Generar archivo CSV con script de Python](#122-generar-archivo-csv-con-script-de-python)
- [2. Visualizar grupos de terminología desde Backoffice de HSI](#2-visualizar-grupos-de-terminología-desde-backoffice-de-hsi)
- [3. Habilitar búsqueda en caché de Snomed](#3-habilitar-búsqueda-en-caché-de-snomed)

## **1. Cargar conceptos en caché**

En esta sección se explicará el proceso y las distintas alternativas a la hora de cargar y actualizar los grupos de terminología Snomed.

Como introducción, es necesario aclarar que los grupos de terminología identifican los tipos de conceptos que se usan en las distintas búsquedas desde el sistema. Por ejemplo, cuando se busca en el campo &quot;Procedimientos&quot; de consulta ambulatoria, esta búsqueda se realiza según la expresión ECL asociada a la clave ECL **PROCEDURE** , cuando se buscan medicamentos se hace según la ECL **MEDICINE,** y así con los demás. Para poder hacer uso de la búsqueda local usando la caché de Snomed, hay que cargar cada uno de estos grupos con los conceptos que engloba. Los grupos son los siguientes:

- ALLERGY
- BLOOD\_TYPE
- CONSULTATION\_REASON
- DIAGNOSIS
- FAMILY\_RECORD
- HOSPITAL\_REASON
- MEDICINE
- PERSONAL\_RECORD
- PROCEDURE
- VACCINE

En el proceso de carga o actualización, se estará creando o modificando un registro en la tabla de la base de datos **snomed\_group** , que tendrá como _description_ la clave del grupo y _last\_update_ la fecha actual. Este dato de la fecha es importante, debido a que en la búsqueda sólo se tendrán en cuenta los conceptos agregados al grupo en una fecha igual o posterior a la última actualización del grupo. Esto permite descartar a los conceptos que alguna vez pertenecieron al grupo pero ya no lo hacen más.

En las [**secciones 1.1**](#11-actualizar-con-tarea-programada-cron) y [**1.2**](#12-actualizar-mediante-archivo-csv) se explicarán las dos formas por las que se pueden cargar los conceptos de los grupos: con tareas programadas o mediante archivos CSV.

**Recomendaciones**:

Si bien ambas formas de cargar la caché son válidas, la forma recomendada es la que se describe en **1.2 Actualizar mediante archivo CSV**. Esto es debido a que suele ser la opción más rápida y la que genera menor carga de trabajo en el sistema de HSI.

Actualizar con **tarea programada** implica que:

- Se puede configurar para iniciar y ejecutar en un horario específico y/o con una frecuencia definida.
- Se pueden actualizar varios o todos los grupos de terminología a la vez.
- Se actualiza con los conceptos del servicio de Snowstorm del momento.
- En comparación, se genera mayor carga en el sistema HSI debido a las sucesivas consultas al servicio Snowstorm.
- Frente a la caída del servicio de Snowstorm no se puede realizar la actualización.
- La actualización de un único grupo de terminología con más de 100 mil conceptos puede tardar varias horas (dependiendo del servidor de Snowstorm).

Actualizar mediante **archivo CSV** implica que:

- La ejecución de la actualización debe iniciarse de forma manual.
- Se actualiza de a un grupo de terminología por vez.
- Se actualiza con los conceptos con los que se genera archivo CSV (puede o no coincidir con los conceptos del servicio Snowstorm del momento).
- En comparación, se genera una carga menor en el sistema HSI debido a que el proceso se enfoca en guardar los conceptos de forma local sin interactuar con Snowstorm.
- Se puede realizar la actualización incluso si Snowstorm no está disponible.
- La actualización de un único grupo de terminología con 180 mil conceptos lleva cerca de 20 minutos según las pruebas realizadas.

## **1.1 Actualizar con tarea programada (Cron)**

Una de las alternativas para cargar/actualizar los grupos de terminología es configurar una tarea programada (Cron) para actualizar mediante el servicio de Snowstorm configurado en el sistema.

Para ello se deben configurar las siguientes propiedades:

```
(1) scheduledjobs.enabled
(2) scheduledjobs.updatesnomedcache.enabled
(3) scheduledjobs.updatesnomedcache.eclkeys
(4) scheduledjobs.updatesnomedcache.seconds
(5) scheduledjobs.updatesnomedcache.minutes
(6) scheduledjobs.updatesnomedcache.hours
(7) scheduledjobs.updatesnomedcache.dayofmonth
(8) scheduledjobs.updatesnomedcache.month
(9) scheduledjobs.updatesnomedcache.dayofweek
```

(1) Habilita/deshabilita la ejecución general de todas las tareas programadas del sistema. Está habilitada por defecto. No debería modificarse ese valor porque se perderían las tareas programadas para otras cuestiones del HSI, por ejemplo el federador.

(2) Habilita/deshabilita la ejecución del cron para actualizar los grupos de Snomed. Está deshabilitada por defecto.

(3) Son las claves de los grupos de conceptos Snomed, separadas por comas sin espacios (ej. _BLOOD\_TYPE,FAMILY\_RECORD_). Puede ser una única clave.

(4) Configura los segundos del cron usado para la actualización de grupos de conceptos Snomed.

(5) Configura los minutos del cron usado para la actualización de grupos de conceptos Snomed.

(6) Configura las horas del cron usado para la actualización de grupos de conceptos Snomed.

(7) Configura el día del mes del cron usado para la actualización de grupos de conceptos Snomed.

(8) Configura el mes del cron usado para la actualización de grupos de conceptos Snomed.

(9) Configura el día de la semana del cron usado para la actualización de grupos de conceptos Snomed.

Los parámetros de tiempo (4) a (9) se configuran según las [pautas de las expresiones cron](https://docs.oracle.com/cd/E12058_01/doc/doc.1014/e12030/cron_expressions.htm).

Un ejemplo de configuración de todas las propiedades sería el siguiente:

```
scheduledjobs.enabled=true
scheduledjobs.updatesnomedcache.enabled=true
scheduledjobs.updatesnomedcache.eclkeys=ALLERGY,BLOOD_TYPE,CONSULTATION_REASON,DIAGNOSIS,FAMILY_RECORD,HOSPITAL_REASON,MEDICINE,PERSONAL_RECORD,PROCEDURE,VACCINE
scheduledjobs.updatesnomedcache.seconds=0
scheduledjobs.updatesnomedcache.minutes=0
scheduledjobs.updatesnomedcache.hours=17
scheduledjobs.updatesnomedcache.dayofmonth=15
scheduledjobs.updatesnomedcache.month=*/2
scheduledjobs.updatesnomedcache.dayofweek=*
```

Con ello se actualizarán todos los grupos de terminología, a las 17 hs. UTC, el día 15 del mes, cada 2 meses.

## **1.2 Actualizar mediante archivo CSV**

Como alternativa a la actualización de los grupos de terminología con una tarea programada, también existe la posibilidad de cargar un archivo con extensión .csv el cual contenga los conceptos Snomed del grupo. Este archivo debe tener en la primera línea las definiciones de las columnas **conceptId** y **term** , y a partir de la línea siguiente cada fila corresponderá a un concepto Snomed. De cada concepto se debe especificar su ID de Snomed y su _preferred term_, separados por una coma y el _preferred term_ entre comillas dobles. Un ejemplo sería el siguiente:

```
conceptId,term
16224591000119103,"alergia a veneno de abeja melífera"
16224471000119103,"alergia a veneno de avispón chaqueta amarilla"
16067291000119109,"alergia a veneno de himenóptero"
16067171000119102,"alergia a aditivo alimentario"
```

Si bien este archivo se puede generar de cualquier manera deseada, en las [**secciones 1.2.1**](#121-generar-archivo-csv-con-base-de-datos-mysql) y [**1.2.2**](#122-generar-archivo-csv-con-script-de-python) se explican dos formas de hacerlo mediante expresiones ECL. Por último, en la [**sección 1.2.3**](#123-cargar-archivo-csv-a-través-de-endpoint-del-backend) se especifica cómo cargar este archivo a través de un endpoint provisto en el Backend.

**Recomendaciones**:

Entre las alternativas propuestas para generar los archivos CSV, la opción recomendada es la descrita en **1.2.2 Generar archivo CSV con script de Python**. Esto se debe a que implica menos pasos para la puesta en marcha (sólo hay que instalar el intérprete de Python). Además, es un proceso más automático y se pueden generar varios o todos los archivos CSV necesarios en una única ejecución. Por último, se puede generar a partir del mismo servidor de Snowstorm que usa HSI, por lo que los conceptos obtenidos serían los mismos que se obtienen en la búsqueda de conceptos sin caché habilitada (cosa que puede variar según la release de SNOMED cargada en la base de datos MySQL, además que no se asegura que la ECL retorne los mismos resultados que en Snowstorm).

## **1.2.1 Generar archivo CSV con base de datos MySQL**

Los conceptos se pueden obtener consultando una base de datos MySQL la cual contiene la terminología de Snomed. En esta sección se explica cómo ponerla en marcha y cómo realizar las consultas para poder generar el archivo CSV.

## **1.2.1.1 Puesta en marcha de la base de datos MySQL**

Para poder obtener conceptos según ECLs, se necesita una base de datos capaz de realizar dichas consultas. SNOMED International provee [esta guía](https://confluence.ihtsdotools.org/display/DOCSQLPG/SNOMED+CT+-+SQL+Practical+Guide) para configurar y usar una base de datos MySQL ya preparada con tablas, vistas, stored procedures útiles para ese tipo de consultas. Además, indica cómo configurar MySQL Workbench para conectarse a la base.

Las instrucciones que hay que seguir para la puesta en marcha están en el [Apéndice A](https://confluence.ihtsdotools.org/display/DOCSQLPG/Appendix+A%3A+Building+the+SNOMED+CT+Example+Database) de la guía.

Si bien en el [paso A.1](https://confluence.ihtsdotools.org/display/DOCSQLPG/A.1+Download+the+SNOMED+CT+Example+Database+Package) se provee un archivo comprimido con los scripts para cargar la creación de la base de datos y cargar la Release de Snomed, para que funcione con la Release argentina de mayo de 2021, los scripts se modificaron debido a que los nombres de los archivos a cargan no coincidían. Estos scripts modificados se pueden encontrar en [este otro archivo comprimido](./resources/SnomedRfsMySql-modified.rar). El resto de los pasos se siguen como indica la guía.

## **1.2.1.2 Generar archivo CSV a partir de ECL en MySQL**

Como ya se mencionó anteriormente, la base de datos ya viene preparada con stored procedures útiles para consultar y filtrar por términos. Uno de ellos se explica en [esta parte](https://confluence.ihtsdotools.org/display/DOCSQLPG/4.9.4.+Search+Procedures) de la guía. Ese recibe un término de búsqueda y una expresión para el filtrado de conceptos.

Sin embargo, como lo que se desea es obtener todos los conceptos según una ECL sin buscar por un término particular, se puede utilizar el procedure `snomedct.eclQuery()`, que recibe una expresión ECL como parámetro. Por ejemplo, para obtener todos los conceptos de alergias hay que ejecutar `call snomedct.eclQuery('< 609328004 |disposición alérgica (hallazgo)|');`. En la siguiente imagen se puede observar el resultado de ejecutarlo en MySQL Workbench.

![call eclQuery](images/mysql-snomed-1-ejecutar-consulta-ECL.jpg)

Los resultados de los términos son en inglés, por lo que hay que traducirlos. Para ello se va a crear una tabla temporal para albergar los resultados de la consulta y después obtener las traducciones de cada uno.

En primer lugar hay que exportar el resultado haciendo click sobre el botón _**Export**_ (marcado en rojo en la imagen anterior), y seleccionar la forma de guardado _SQL INSERT statements (*.sql)_, como se muestra en la imagen:

![save inserts](images/mysql-snomed-2-guardar-inserts.jpg)

Con esto se generará un archivo con las sentencias para insertar cada fila del resultado en una tabla, que se puede llamar por ejemplo `temp_1`. Si abrimos este archivo vamos a ver algo como lo siguiente:

```
INSERT INTO `` (`conceptId`,`term`) VALUES (91931000,'Allergy to erythromycin (finding)');
INSERT INTO `` (`conceptId`,`term`) VALUES (91932007,'Allergy to fruit (finding)');
INSERT INTO `` (`conceptId`,`term`) VALUES (91934008,'Allergy to nut (finding)');
…
```

Como falta el nombre de la tabla donde se van a insertar, con un editor de texto podemos reemplazar todas las ocurrencias de **``** por **snomedct.temp_1**. Además debemos agregar al principio las sentencias de creación de la tabla. De esta manera, el archivo va a quedar de la siguiente manera:

```
DROP TABLE IF EXISTS snomedct.temp_1;
CREATE TABLE snomedct.temp_1 (conceptId bigint, term varchar(255));

INSERT INTO snomedct.temp_1 (`conceptId`,`term`) VALUES (91931000,'Allergy to erythromycin (finding)');
INSERT INTO snomedct.temp_1 (`conceptId`,`term`) VALUES (91932007,'Allergy to fruit (finding)');
INSERT INTO snomedct.temp_1 (`conceptId`,`term`) VALUES (91934008,'Allergy to nut (finding)');
…
```

Al cargar el archivo en MySQL Workbench y ejecutar todas las sentencias, se creará la tabla que sólo tiene todos los conceptos de alergias.

Ahora queda hacer la query para obtener las traducciones de los conceptos. Las traducciones se pueden encontrar en la tabla `snap_description`. La query a ejecutar queda como la siguiente:

```
SELECT DISTINCT t1.conceptId, REPLACE(sd.term,'\"','\'') AS term
FROM snomedct.temp_1 t1
LEFT JOIN snomedct.snap_description sd ON (t1.conceptId = sd.conceptId)
WHERE sd.languageCode = 'es'
    AND sd.typeId = 900000000000013009
    AND sd.active = true;
```

Las cláusulas del `WHERE` tienen el siguiente fin:
- `sd.languageCode = 'es'` para que sólo se obtengan los nombres de conceptos en español.
- `sd.typeId = 900000000000013009` porque ese es el tipo de las traducciones abreviadas.
- `sd.active = true` para filtrar las traducciones que ya no están activas.

Al ejecutar la consulta, se obtienen las traducciones de los términos y el sctid de los conceptos como se muestra en la imagen:

![translation query](images/mysql-snomed-3-ejecutar-consulta-traducciones.jpg)

Esto se puede exportar haciendo click en el botón **_Export_** y seleccionando la forma de guardar _CSV (*.csv)_. Este archivo CSV se puede cargar en HSI a través del endpoint `/api/snowstorm/load-concepts-csv`, que acepta un archivo CSV y una clave ecl, que en el caso de alergias es **ALLERGY**.

## **1.2.2 Generar archivo CSV con script de Python**

Otra opción para generar los CSV es con un script escrito en Python, el cual genera archivos CSV consultando reiteradamente a un servicio de Snowstorm. A continuación se presenta un script de ejemplo, donde basta con configurar la variable de ambiente `SNOWSTORM_SERVICE` con la URL base del servicio Snowstorm que se esté utilizando. Adicionalmente, si el servidor de Snowstorm requiere de autenticación para su uso (lo que es normal para los servidores de producción, definir las variables de ambiente `API_ID` y `API_KEY` con los respectivos valores.

```
# En la raíz del proyecto
docker build -t csv-exporter ci/snomed-csv

docker run -itv ${PWD}:/exporter/csv -e SNOWSTORM_SERVICE=https://snowstorm.msal.gob.ar -e API_ID=aabbccdd -e API_KEY=0000000ccccccc22222222aabb1122 csv-exporter

```


## **1.2.2.1 Generar archivo CSV de sinónimos de conceptos**

Para generar el archivo de [sinónimos](https://confluence.ihtsdotools.org/display/DOCEG/Synonym) de conceptos utilizando el script anterior es necesario contar previamente con una [Release](https://confluence.ihtsdotools.org/display/DOCRELFMT) de Snomed. Adicionalmente, se deberá definir la variable de ambiente `DESCRIPTION_FULL_FILE` con la ruta al archivo del [tipo de Release](https://confluence.ihtsdotools.org/display/DOCRELFMT/3+Release+Types%2C+Packages+and+Files) deseada (el mismo deberá ser el correspondiente a la tabla Description).
Finalizada la ejecución, se habrá generado un archivo CSV adicional por cada par (clave, ECL) definida en el archivos ecls.json con el nombre de la ECL correspondiente y el sufijo SYNONYM.



## **2. Visualizar grupos de terminología desde Backoffice de HSI**

Desde el Backoffice de HSI se permite la visualización de los grupos de terminología cargados, junto con los conceptos que incluye y otros datos de interés. Se puede acceder al mismo a través del menú en **_Datos maestros_** > **_Grupos de terminología_**, donde se visualizará algo similar a la siguiente captura:

![backoffice-1](images/csv-backoffice-1.jpg)

En rojo se marcan los grupos cargados para la búsqueda con la caché de Snomed. Si accedemos a uno, se va a visualizar algo como a continuación:

![backoffice-1](images/csv-backoffice-2.jpg)

En esta vista, se puede ver la ECL asociada y su clave. Además, en la parte inferior se muestran todos los conceptos asociados al grupo, junto con su última actualización.

En el listado aparecen también los conceptos que ya no aparecen en las búsquedas debido a que en la última actualización del grupo no se incluyó este concepto. Este es el caso de &quot;_Alergia a aceite de cacahuate_&quot;, lo cual se hace evidente debido a que su fecha de última actualización (31/3/2022) es anterior a la última actualización del grupo ALLERGY (1/4/2022).

## **3. Habilitar búsqueda en caché de Snomed**

Como último paso, para activar la búsqueda de conceptos mediante la caché de Snomed se debe levantar el sistema con el _feature flag_ **HABILITAR\_BUSQUEDA\_LOCAL\_CONCEPTOS** activado.

Para ello, basta con configurar la propiedad como sigue:

```
app.feature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS=true
```

Una vez iniciado el sistema, las búsquedas de conceptos realizadas desde la Web App de HSI se realizarán de forma local en lugar de utilizar el servicio de Snowstorm.
