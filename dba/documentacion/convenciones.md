# Convenciones generales

Para asegurar tener una base de datos estable y evitar problemas de inconsistencia de datos se estableció un conjunto de reglas a seguir. Estas reglas serán validadas en el proceso de code review de los merge requests dirigidos a la rama principal de desarrollo.


## Base de datos

- [x] No se permite el uso de Triggers en los scripts. El uso de los mismos si bien son performantes, pueden provocar una ejecución masiva de queries que no se pueden controlar.
- [x] No se permite el uso de vistas materializadas. Por el mismo motivo que los triggers, se pierde control de lo que hace la base de datos.
- [x] No se permite el uso de procedimientos, al menos que se necesite corregir datos. Pero el mismo debe ser eliminado una vez ejecutado.
- [x] Evitar el uso de código SQL especifico del motor de base de datos (vistas/consultas). Esto puede generar incompatibilidad del sistema con otros motores.
- [x] Todo elemento de la base de datos tiene que estar escrito en idioma inglés.  
- [x] Todas las tablas deben tener clave primaria. 
- [x] Los scripts se deben generar usando la herramienta liquibase.
   

## Nomenclatura

En esta sección se detalla la nomenclatura esperada para los distintos elementos que se pueden crear en una base de datos.

1. Tablas: Nombres plenamente en minúsculas y con el caracter `_` como separador entre palabras.
2. Claves primarias:   PK_nombretabla
3. Claves extranjeras: FK_nombretablaorigen_nombrecolumna
4. Indices:   IDX_nombretabla_descripcion
5. Restricciones de unicidad: UQ_nombretabla_nombrecolumna1_nombrecolumna2_...
6. Nombre de clave primaria simple: En cada tabla se debe llamar **id**
7. Nombre de atributos referentes a claves extranjeras: nombretabla_id. 

> Ejemplo si tenemos la tabla `person` que tiene una referencia a la tabla `address`, el atributo debe llamarse `address_id`.



# Liquibase

La herramienta predeterminada para la generación de Scripts es Liquibase, por lo tanto, todos los archivos de configuración pertinentes incluidos los scripts incrementales se encuentran en la carpeta `liquibase/`. Las convenciones a tener en cuenta para su uso son las siguientes:

1. Formato **YML** para cualquier changeset agregado.
2. Escribir los changeset en el archivo incremental correspondiente a la [version actual](../../pom-parent.xml#L16) de desarrollo. 
3. Ubicar los changeset al final del archivo incremental correspondiente al momento de mergear el branch a la rama principal.
4. Usar las variables definidas en el archivo [multivendor.yml](../liquibase/multivendor.yml) para evitar problemas de incompatibilidad de tipos entre distintas bases de datos. Por ejemplo

```yml
- column:
    constraints:
        nullable: false
    name: deleted
    type: boolean
    defaultValue: ${boolean.false}  
```

5. Separar los changeset entre cambios estructurales y metadatos. Para changeset que implican actualizaciones de datos agregar el atributo **context: "!schema-only"**
