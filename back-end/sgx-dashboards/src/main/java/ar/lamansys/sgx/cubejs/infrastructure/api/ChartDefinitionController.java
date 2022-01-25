package ar.lamansys.sgx.cubejs.infrastructure.api;

import ar.lamansys.sgx.cubejs.application.charts.FetchChartDefinitionService;
import ar.lamansys.sgx.cubejs.domain.charts.ChartDefinitionBo;
import lombok.extern.slf4j.Slf4j;
import net.pladema.hsi.extensions.infrastructure.controller.dto.ChartDefinitionDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/dashboards/charts")
//@Api(value = "ChartDefinition", tags = { "Dashboards" })
public class ChartDefinitionController {
	private final FetchChartDefinitionService fetchChartDefinition;

	public ChartDefinitionController(FetchChartDefinitionService fetchChartDefinition) {
		this.fetchChartDefinition = fetchChartDefinition;
	}

	@GetMapping("/{chartName}")
	@ResponseBody
	public ChartDefinitionDto fetchChartDefinition(
			@PathVariable(name = "chartName") String chartName,
			@RequestParam Map<String, String> params
	) {

		log.debug("Fetching {}", chartName);
		log.debug("Params {}", params);

		return mapToQuery(fetchChartDefinition.run(chartName, params));

	}

	private static ChartDefinitionDto mapToQuery(ChartDefinitionBo chartDefinition) {
		return new ChartDefinitionDto(
				chartDefinition.cubeQuery,
				chartDefinition.chartType,
				chartDefinition.pivotConfig
		);
	}
}
