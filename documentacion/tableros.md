# Tableros

Los tableros se apoyan en el módulo de [Extensiones](../back-end/documentacion/extensiones.md) para definir ítems de menú a agregar a nivel dominio o institución.

Estos ítems de menú para tableros se definen en [la configuración del módulo 'sgx-dashboards'](../back-end/sgx-dashboards/src/main/java/ar/lamansys/sgx/cubejs/CubejsAutoConfiguration.java) haciendo que la estructura de menú y la página asociada a ese menú se carguen desde [los recursos del módulo 'sgx-dashboards'](../back-end/sgx-dashboards/src/main/resources/extension/).

Los permisos para estos ítems de menú se definen en [el módulo 'app'](../back-end/app/src/main/java/net/pladema/sgh/app/security/infraestructure/configuration/ExtensionSecurityConfiguration.java).


## ChartDefinition

Los [ChartDefinitions](../back-end/sgx-dashboards/src/main/java/ar/lamansys/sgx/cubejs/domain/charts/ChartDefinitionBo.java) se definen como json en [el módulo 'sgx-dashboards'](../back-end/sgx-dashboards/src/main/resources/dashboards/charts).

Representan gráficos a armar aplicando operaciones a los Cubos.


## Cubos

Los [Cubes](https://cube.dev/docs/schema/reference/cube) se definen en [cubejs/schema](../cubejs/schema/).

Representan una vista sobre la base de datos. Esto permite el entrecruzamiento de cualquier tipo de información almacenada en la base de datos relacional.

