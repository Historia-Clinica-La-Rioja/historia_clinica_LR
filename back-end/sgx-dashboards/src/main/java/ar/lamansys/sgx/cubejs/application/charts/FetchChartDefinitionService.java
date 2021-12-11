package ar.lamansys.sgx.cubejs.application.charts;

import java.util.Map;

import ar.lamansys.sgx.cubejs.domain.charts.ChartDefinitionBo;

/**
 * Permite obtener un {@link ChartDefinitionBo} a partir de un nombre de gráfico y sus parámetros.
 */
public interface FetchChartDefinitionService {
	ChartDefinitionBo run(String chartName, Map<String, String> params);
}
