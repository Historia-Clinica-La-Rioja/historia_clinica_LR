package ar.lamansys.sgx.cubejs.domain.charts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Un ChartDefinitionBo contiene toda la información necesaria para dibujar un gráfico:
 * 	* chartType: tipo de gráfico
 * 	* cubeQuery: consulta
 * 	* pivotConfig: manipulación de datos
 */
public class ChartDefinitionBo {
	public final String chartType;
	/**
	 * Cube Queries are plain JavaScript objects, describing an analytics query
	 * (ver https://cube.dev/docs/query-format)
 	 */
	public final Object cubeQuery;
	/**
	 * Configuration object that contains information about pivot axes and other options
	 * (ver https://cube.dev/docs/@cubejs-client-core#types-pivot-config)
	 */
	public final Object pivotConfig;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public ChartDefinitionBo(
			@JsonProperty("cubeQuery") Object cubeQuery,
			@JsonProperty("chartType") String chartType,
			@JsonProperty("pivotConfig") Object pivotConfig
	) {
		this.cubeQuery = cubeQuery;
		this.chartType = chartType;
		this.pivotConfig = pivotConfig;
	}
}
