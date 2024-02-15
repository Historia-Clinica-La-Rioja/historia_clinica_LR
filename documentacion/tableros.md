# Tableros

Los tableros se apoyan en el módulo de [Extensiones](../back-end/documentacion/extensiones.md) para definir ítems de menú a agregar a nivel dominio o institución.

Estos ítems de menú para tableros se definen en [la configuración del módulo 'sgx-dashboards'](../back-end/sgx-dashboards/src/main/java/ar/lamansys/sgx/cubejs/CubejsAutoConfiguration.java) haciendo que la estructura de menú y la página asociada a ese menú se carguen desde [los recursos del módulo 'sgx-dashboards'](../back-end/sgx-dashboards/src/main/resources/extension/).

Los permisos para estos ítems de menú se definen en [el módulo 'app'](../back-end/app/src/main/java/net/pladema/sgh/app/security/infraestructure/configuration/ExtensionSecurityConfiguration.java).


Cada página se define utilizando un JSON que sigue la estructura de componentes visuales proporcionada por el módulo de Extensiones. Esta estructura es flexible y permite el uso de pestañas (tabs) y tarjetas (cards) en cualquier orden. Para incluir un tablero de cubejs, se debe declarar un elemento `cubejs-dashboard`. Dentro de este elemento, se puede optar por definir filtros (`filters`), y es obligatorio incluir uno o varios gráficos (`ui-chart`) que se verán afectados por los filtros del tablero. La estructura de componentes es flexible, lo que permite ubicar los filtros y los gráficos según sea necesario.

Los elementos `ui-chart` deben especificar cómo mostrar la información. Se puede elegir mostrar una tabla, un indicador numérico o un gráfico (como torta, barra, líneas o áreas). Los argumentos de un `ui-chart` son:

1. `query`: El **nombre** del ChartDefinitio, que es donde se define:
    - `cubeQuery`: es la consulta. Define qué información se extraerá de la base de datos relacional, puede incluir instrucciones como selección de columnas, filtrado de datos, agrupación, ordenación.
    - `chartType`: el tipo de gráfico a mostrar
    - `pivotConfig`[^1]: el _pivote_, permite realizar operaciones como agrupaciones y resúmenes de datos para obtener diferentes vistas y perspectivas de los resultados. Estas opciones pueden incluir detalles como las columnas a mostrar, las filas a agrupar, los valores a calcular y cualquier otra configuración relacionada con la estructura y presentación de los datos en el gráfico.

2. `chartOptions`[^2]: pueden variar según el tipo de gráfico que se esté utilizando (como gráficos de torta, barras, líneas, áreas, etc.). Estas opciones pueden incluir aspectos como colores, etiquetas, leyendas, títulos, escalas, tooltips, entre otros.

3. `title`: El título de los indicadores numéricos.

[^1] Se puede usar para tablas o gráficos
[^2] Se usa solo para gráficos

El componente `ui-chart` (gracias al `ChartDefinitionService`) puede obtener el `ChartDefinition`, que incluye la consulta (`cubeQuery`) a partir de su nombre (`query`) y los valores seleccionados en los filtros del tablero. Con la consulta puede obtener el `ResultSet` gracias al `CubejsClient`. 

Según el `chartType` definido en el `ChartDefinition` se usará el `ResultSet` para mostrar:

1. [query-table-renderer](../front-end/apps/projects/hospital/src/app/modules/extensions/components/query-table-renderer)
2. [query-card-renderer](../front-end/apps/projects/hospital/src/app/modules/extensions/components/query-card-renderer)
3. [query-chart-renderer](../front-end/apps/projects/hospital/src/app/modules/extensions/components/query-chart-renderer)


## ChartDefinition

Los [ChartDefinitions](../back-end/sgx-dashboards/src/main/java/ar/lamansys/sgx/cubejs/domain/charts/ChartDefinitionBo.java) se definen como json en [el módulo 'sgx-dashboards'](../back-end/sgx-dashboards/src/main/resources/dashboards/charts).

Representan gráficos a armar aplicando operaciones a los Cubos.


## Cubos

Los [Cubes](https://cube.dev/docs/schema/reference/cube) se definen en [cubejs/schema](../cubejs/schema/).

Representan una vista sobre la base de datos. Esto permite el entrecruzamiento de cualquier tipo de información almacenada en la base de datos relacional.

