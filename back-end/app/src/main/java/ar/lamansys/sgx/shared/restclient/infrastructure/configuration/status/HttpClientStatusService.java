package ar.lamansys.sgx.shared.restclient.infrastructure.configuration.status;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.strings.StringHelper;
import net.pladema.hsi.extensions.configuration.features.FeatureProperty;
import net.pladema.hsi.extensions.configuration.features.FeatureStatusService;

@Service
@Order(1)
public class HttpClientStatusService extends FeatureStatusService {

	public HttpClientStatusService(
			HttpClientConfiguration configuration
	) {
		super(
				"app.http.client",
				listProperties(configuration),
				fetchStatusData()
		);
	}

	private static Supplier<Map<String, Object>> fetchStatusData() {
		return Collections::emptyMap;
	}

	private static Supplier<List<FeatureProperty>> listProperties(HttpClientConfiguration properties) {
		return () -> List.of(
				new FeatureProperty("trustInvalidCertificate", StringHelper.toString(properties.isTrustInvalidCertificate())),
				new FeatureProperty("timeout", StringHelper.toString(properties.getTimeout())),
				new FeatureProperty("proxy", properties.getProxy())
		);
	}

}
