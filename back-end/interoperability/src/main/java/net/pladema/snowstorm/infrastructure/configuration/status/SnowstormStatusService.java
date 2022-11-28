package net.pladema.snowstorm.infrastructure.configuration.status;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import net.pladema.hsi.extensions.configuration.features.FeatureProperty;
import net.pladema.hsi.extensions.configuration.features.FeatureStatusService;
import net.pladema.snowstorm.configuration.SnowstormWSConfig;
import net.pladema.snowstorm.services.SnowstormService;

@Service
@Order(4)
public class SnowstormStatusService extends FeatureStatusService {

	public SnowstormStatusService(
			SnowstormService snowstormService,
			SnowstormWSConfig configuration
	) {
		super(
				"ws.snowstorm",
				listProperties(configuration),
				fetchStatusData(snowstormService)
		);
	}

	private static Supplier<Map<String, Object>> fetchStatusData(SnowstormService snowstormService) {
		return () -> Map.of(
		"concepts", snowstormService.status()
		);
	}

	private static Supplier<List<FeatureProperty>> listProperties(SnowstormWSConfig configuration) {
		return () -> List.of(
				new FeatureProperty("url.base", configuration.getBaseUrl()),
				new FeatureProperty("appId", configuration.getAppId()),
				new FeatureProperty("appKey", configuration.getAppKey())
		);
	}

}
