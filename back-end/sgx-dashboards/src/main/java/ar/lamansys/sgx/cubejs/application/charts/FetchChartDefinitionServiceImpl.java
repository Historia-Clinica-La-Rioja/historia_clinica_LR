package ar.lamansys.sgx.cubejs.application.charts;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.lamansys.sgx.cubejs.application.charts.exceptions.ChartDefinitionFetchException;
import ar.lamansys.sgx.cubejs.domain.charts.ChartDefinitionBo;
import ar.lamansys.sgx.shared.templating.JSONTemplateEngine;
import ar.lamansys.sgx.shared.templating.TemplateEngine;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FetchChartDefinitionServiceImpl implements FetchChartDefinitionService {
	private final TemplateEngine<ChartDefinitionBo> chartDefinitionEngine;

	public FetchChartDefinitionServiceImpl(
			@Value("${app.gateway.cubejs.charts.template.prefix:classpath:/dashboards/charts/}") String templatePrefix,
			@Value("${app.gateway.cubejs.charts.list}") String[] chartNames,
			ApplicationContext applicationContext
	) {
		this.chartDefinitionEngine = new JSONTemplateEngine<>(
				chartNames,
				springTemplateEngine(
						jsonTemplateResolver(
								templatePrefix,
								applicationContext
						)
				),
				new ObjectMapper(),
				new TypeReference<>() {}
		);
	}

	@Override
	public ChartDefinitionBo run(String chartName, Map<String, String> params) {
		try {
			return chartDefinitionEngine.process(chartName, params);
		} catch (Exception e) {
			log.error(String.format("Processing %s", chartName), chartName, e);
			throw new ChartDefinitionFetchException(chartName, e);
		}
	}

	private static SpringTemplateEngine springTemplateEngine(
			SpringResourceTemplateResolver chartsTemplateResolver
	) {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.addTemplateResolver(chartsTemplateResolver);
		templateEngine.addDialect(new Java8TimeDialect());
		return templateEngine;
	}

	private static SpringResourceTemplateResolver jsonTemplateResolver(
			String prefix,
			ApplicationContext applicationContext
	) {

		SpringResourceTemplateResolver jsonTemplateResolver = new SpringResourceTemplateResolver();
		jsonTemplateResolver.setPrefix(prefix);
		jsonTemplateResolver.setSuffix(".json");
		jsonTemplateResolver.setTemplateMode(TemplateMode.JAVASCRIPT);
		jsonTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		jsonTemplateResolver.setCheckExistence(true);
		jsonTemplateResolver.setCacheable(false);
		jsonTemplateResolver.setApplicationContext(applicationContext);

		return jsonTemplateResolver;
	}
}
