# Tableros

Este proyecto tiene como objetivo el manejo de tableros del sistema mediante el uso de [cubejs](https://cube.dev/).

## Seguridad

Cubejs permite el uso de un sistema de autenticación y autorización mediante el uso de [JWT](https://dev.to/cubejs/multi-tenant-analytics-with-auth0-and-cube-js-the-complete-guide-31lo).

En las siguientes secciones vamos a definir un conjunto de escenarios de seguridad y como lo resolvemos haciendo uso de este JWT

### Caso 1

Se quiere limitar el acceso a toda la información expuesta por una consulta. Es decir, un usuario tiene que tener un rol especifico para poder ver el resultado de la query.

Este escenario se puede resolver de la siguiente forma:

```javascript
cube(`Referencias`, {
    sql: `SELECT 
      r.id,
      oc.start_date as fecha_consulta,
      oc.institution_id as institucion_origen_id
    FROM reference r 
    JOIN outpatient_consultation oc ON (r.encounter_id = oc.id) 
    ${SECURITY_CONTEXT.userId.unsafeValue() ? '' +  `
    WHERE exists (
    SELECT u.id 
    FROM users as u 
    JOIN user_role ur on u.id = ur.user_id 
    WHERE ur.role_id = 5 #ROL ADMINISTRADOR
    AND u.id = ${SECURITY_CONTEXT.userId.unsafeValue()})`  : `WHERE r.id IS NULL`}`,
  
  measures: {
    cant_referencia: {
        sql: `id`,
        type: `sum`,
        title: `Cantidad Referencias`,
    }
  },
  
  dimensions: {
    // Fecha consulta
    fecha_consulta: {
      sql: `fecha_consulta`,
      type: `time`,
    },
    // Institución origen
    institucion_origen_id: {
      sql: `institucion_origen_id`,
      type: `number`,
    },
  },
  title:` `,
  dataSource: `default`
});

```


### Caso 2

Se quiere filtrar la respuesta basadas en los roles del usuario. Es decir, una persona con X rol puede ver un subconjunto de la información.

```javascript
cube(`Referencias`, {
    sql: `SELECT 
      r.id,
      oc.start_date as fecha_consulta,
      oc.institution_id as institucion_origen_id
    FROM reference r 
    JOIN outpatient_consultation oc ON (r.encounter_id = oc.id) 
    WHERE oc.start_date BETWEEN '2021-01-01' AND '2021-12-31'
    ${SECURITY_CONTEXT.userId.unsafeValue() ? '' +  `
    AND oc.institution_id IN (
    SELECT ur.institution_id 
    FROM users as u 
    JOIN user_role ur on u.id = ur.user_id 
    WHERE ur.role_id = 5 #Administrador de una institución
    AND u.id = ${SECURITY_CONTEXT.userId.unsafeValue()})` : `WHERE r.id IS NULL`}`,
    UNION ALL
    SELECT 
      r.id,
      oc.start_date as fecha_consulta,
      oc.institution_id as institucion_origen_id
    FROM reference r 
    JOIN outpatient_consultation oc ON (r.encounter_id = oc.id) 
    WHERE oc.start_date BETWEEN '2020-01-01' AND '2020-12-31'
    ${SECURITY_CONTEXT.userId.unsafeValue() ? '' +  `
    AND oc.institution_id IN (
    SELECT ur.institution_id 
    FROM users as u 
    JOIN user_role ur on u.id = ur.user_id 
    WHERE ur.role_id = 6 #SECRETARIO de una institución
    AND u.id = ${SECURITY_CONTEXT.userId.unsafeValue()})` : `WHERE r.id IS NULL`}`,
  
  measures: {
    cant_referencia: {
        sql: `id`,
        type: `sum`,
        title: `Cantidad Referencias`,
    }
  },
  
  dimensions: {
    // Fecha consulta
    fecha_consulta: {
      sql: `fecha_consulta`,
      type: `time`,
    },
    // Institución origen
    institucion_origen_id: {
      sql: `institucion_origen_id`,
      type: `number`,
    },
  },
  title:` `,
  dataSource: `default`
});

```

### Caso 3

Se quiere ver solamente la información de la institución a la cuál se tiene X permisos. Es decir sí el usuario tiene un rol X sobre tal institución puiede ver su información.

```javascript
cube(`Referencias`, {
    sql: `SELECT 
      r.id,
      oc.start_date as fecha_consulta,
      oc.institution_id as institucion_origen_id
    FROM reference r 
    JOIN outpatient_consultation oc ON (r.encounter_id = oc.id) 
    ${SECURITY_CONTEXT.userId.unsafeValue() ? '' +  `
    WHERE oc.institution_id IN (
    SELECT ur.institution_id 
    FROM users as u 
    JOIN user_role ur on u.id = ur.user_id 
    WHERE ur.role_id = 5 #Administrador de una institución
    AND u.id = ${SECURITY_CONTEXT.userId.unsafeValue()})` : `WHERE r.id IS NULL`}`,
  
  measures: {
    cant_referencia: {
        sql: `id`,
        type: `sum`,
        title: `Cantidad Referencias`,
    }
  },
  
  dimensions: {
    // Fecha consulta
    fecha_consulta: {
      sql: `fecha_consulta`,
      type: `time`,
    },
    // Institución origen
    institucion_origen_id: {
      sql: `institucion_origen_id`,
      type: `number`,
    },
  },
  title:` `,
  dataSource: `default`
});

```

Como el cubo tiene la dimensión institución origen, entonces se puede filtrar por dicha institución.

### Caso 4

Se quiere ver solamente la información relacionada al usuario que la consulta.

```javascript
cube(`Referencias`, {
    sql: `SELECT 
      r.id,
      oc.start_date as fecha_consulta,
      oc.institution_id as institucion_origen_id
    FROM reference r 
    JOIN outpatient_consultation oc ON (r.encounter_id = oc.id) 
    ${SECURITY_CONTEXT.userId.unsafeValue() ? '' +  `
    WHERE oc.created_by = ${SECURITY_CONTEXT.userId.unsafeValue()})` : `WHERE r.id IS NULL`}`,
  
  measures: {
    cant_referencia: {
        sql: `id`,
        type: `sum`,
        title: `Cantidad Referencias`,
    }
  },
  
  dimensions: {
    // Fecha consulta
    fecha_consulta: {
      sql: `fecha_consulta`,
      type: `time`,
    },
    // Institución origen
    institucion_origen_id: {
      sql: `institucion_origen_id`,
      type: `number`,
    },
  },
  title:` `,
  dataSource: `default`
});

```


## Configuración

El módulo de dashboard usa las siguientes propiedades

| Propiedad | Variable de ambiente | Valor por defecto | Necesidad   | Descripcion                                                                                                                                                          | Desde   |
| ----------------------- | ---------------|-------------------|-------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
|app.gateway.cubejs.apiUrl   | CUBEJS_API_URL |                   | Obligatoria | Define la url donde se encuentra el contenedor de cubejs. Por ejemplo, http://localhost:4000/cubejs-api/v1                                                           | v1.43.0 |
|app.gateway.cubejs.token.secret   | CUBEJS_API_SECRET |                   | Obligatoria | Define la clave de encriptación del token usado en la comunicación backend-cubejs. Este valor debe coincidir con la variable de ambiente CUBEJS_API_SECRET de cubejs | v1.43.0 |
|app.gateway.cubejs.token.header   | | Authorization     | Obligatoria | Define el header en el que se setea el token                                                                                                                         | v1.43.0 |
|app.gateway.cubejs.token.expiration   |  | 20d               | Obligatoria | Define el tiempo de duración del token 20                                                                                                                            | v1.43.0 |
|app.gateway.cubejs.charts.list   |  | tablaReferencia,tablaContraReferencia                  | Obligatoria | Define los cubos definidos en el container de cubejs                                                                | v1.43.0 |
