# Convenciones generales

Para asegurarnos tener una base de datos estable y evitar problemas de inconsistencia de datos se establecieron un conjunto de reglas a seguir. Estas reglas serán validadas en el proceso de code review de los merge requests dirigidos a la rama principal de master.


## Base de datos

- [x] No se permiten el uso de Triggers en los scripts. El uso de los mismos si bien son performantes, pueden provocar una ejecución masiva de queries que no se pueden controlar.
- [x] No se permiten el uso de vistas materializadas. Por el mismo motivo que los triggers, se pierde control de lo que hace la base de datos.
- [x] Evitar usar de código SQL especifico del motor de base de datos (vistas/consultas). Esto puede generar incompatibilidad del sistema a otros motores.
- [x] Todo elemento de la base de datos tiene que estar escrito en idioma inglés.  
- [x] Todas las tablas tienen que tener una clave primaria. 
- [x] No se permiten el uso de procedimientos, al menos que se necesite corregir datos. Pero el mismo debe ser eliminado una vez ejecutado.
- [x] Los scripts se deben generar usando la herramienta liquibase (se explica más adelante)
   


## Nomeclatura

En esta sección se detalla la nomeclatura esperada para los distintos elementos que se pueden crear en una base de datos.

1. Claves primarias:   PK_nombretabla
2. Claves extranjeras: FK_nombretablaorigen_nombrecolumna
3. Indices:   IDX_nombretabla_descripcion
4. Renstricciones de unicidad: UQ_nombretabla_nombrecolumna1_nombrecolumna2_...
6. Nombre de clave primaria simple: En cada tabla se debe llamar id
7. Nombres de atributos referentes a claves extranjeras: nombretabla_id. Ejemplo si tenemos la tabla person que tiene una referencia a la tabla address, el atributo se debe llamar address_id.



# Liquibase

Como se menciona en el siguiente [documento](liquibase.md) usamos la herramienta liquibase para la generación de scripts. Las convenciones a tener en cuenta para su uso son las siguientes:

1. Se debe usar el formato YML para cualquier changeset agregado.
2. Al momento de mergear el branch a master los nuevos changeset deben estar al final del incremental correspondiente al sprint.
3. Se deben usar las variables definidas en el archivo [multivendor.yml](../liquibase/multivendor.yml) para evitar problemas de incompatibilidad de tipos entre distintas bases de datos. Por ejemplo

```yml
- column:
    constraints:
        nullable: false
    name: deleted
    type: boolean
    defaultValue: ${boolean.false}  
```

4. Separar los changeset de cambios estructurales con los de cambios de datos. En los changeset que involucran actualizaciones de datos, agregar el atributo **context: "!schema-only"**
