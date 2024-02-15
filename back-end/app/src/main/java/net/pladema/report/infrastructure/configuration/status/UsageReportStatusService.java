package net.pladema.report.infrastructure.configuration.status;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import net.pladema.hsi.extensions.configuration.features.FeatureProperty;
import net.pladema.hsi.extensions.configuration.features.FeatureStatusService;
import net.pladema.report.infrastructure.output.rest.ReportWSConfig;

@Service
@Order(6)
public class UsageReportStatusService extends FeatureStatusService {

	public UsageReportStatusService(
			ReportWSConfig reportWSConfig
	) {
		super(
				"app.usage-metrics",
				listProperties(reportWSConfig),
				fetchStatusData()
		);
	}

	private static Supplier<Map<String, Object>> fetchStatusData() {
		return Collections::emptyMap;
	}

	private static Supplier<List<FeatureProperty>> listProperties(ReportWSConfig configuration) {
		return () -> List.of(
				new FeatureProperty("url", configuration.getUrl()),
				new FeatureProperty("appId", configuration.getAppId()),
				new FeatureProperty("appKey", configuration.getAppKey())
		);
	}

}
