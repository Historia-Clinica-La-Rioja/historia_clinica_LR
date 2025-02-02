package ar.lamansys.sgx.cubejs.application.charts;

import static ar.lamansys.sgx.shared.templating.utils.SpringTemplateUtils.createJsonTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import ar.lamansys.sgx.shared.featureflags.AppFeature;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.lamansys.sgx.cubejs.application.charts.exceptions.ChartDefinitionFetchException;
import ar.lamansys.sgx.cubejs.domain.charts.ChartDefinitionBo;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.templating.JSONTemplateEngine;
import ar.lamansys.sgx.shared.templating.TemplateEngine;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FetchChartDefinitionServiceImpl implements FetchChartDefinitionService {
	private final TemplateEngine<ChartDefinitionBo> chartDefinitionEngine;

	private final FeatureFlagsService featureFlagsService;

	public FetchChartDefinitionServiceImpl(
			@Value("${app.gateway.cubejs.charts.template.prefix:classpath:/dashboards/charts/}") String templatePrefix,
			@Value("${app.gateway.cubejs.charts.list}") String[] chartNames,
			ApplicationContext applicationContext,
			FeatureFlagsService featureFlagsService
	) {
		this.chartDefinitionEngine = new JSONTemplateEngine<>(
				chartNames,
				createJsonTemplateEngine(
					templatePrefix,
					applicationContext
				),
				new ObjectMapper(),
				new TypeReference<>() {}
		);
		this.featureFlagsService = featureFlagsService;
	}

	@Override
	public ChartDefinitionBo run(String chartName, Map<String, String> params) {
		Map<String, String> clonedParams = new HashMap<>(params);
		clonedParams.put("nameSelfDeterminationFF", String.valueOf(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS)));
		try {
			return chartDefinitionEngine.process(chartName, clonedParams);
		} catch (Exception e) {
			log.error(String.format("Processing %s", chartName), chartName, e);
			throw new ChartDefinitionFetchException(chartName, e);
		}
	}

}
