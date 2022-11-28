package ar.lamansys.sgx.cubejs.infrastructure.configuration.status;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.cubejs.application.dashboardinfo.DashboardInfoService;
import ar.lamansys.sgx.cubejs.CubejsAutoConfiguration;
import net.pladema.hsi.extensions.configuration.features.FeatureProperty;
import net.pladema.hsi.extensions.configuration.features.FeatureStatusService;


@Service
@Order(3)
public class DashboardStatusService extends FeatureStatusService {

	public DashboardStatusService(
			DashboardInfoService dashboardInfoService,
			CubejsAutoConfiguration configuration
	) {
		super(
				"app.gateway.cubejs",
				listProperties(configuration),
				configuration.isEnabled(true) ? fetchStatusData(dashboardInfoService) : FEATURE_DISABLED
		);
	}

	private static Supplier<Map<String, Object>> fetchStatusData(DashboardInfoService dashboardInfoService) {
		return () -> Map.of(
		"meta", dashboardInfoService.execute("/meta", Collections.emptyMap()).getResponse()
		);
	}

	private static Supplier<List<FeatureProperty>> listProperties(CubejsAutoConfiguration configuration) {
		return () -> List.of(
				new FeatureProperty("apiUrl", configuration.getApiUrl()),
				new FeatureProperty("proxy", configuration.getProxy())
		);
	}

}
