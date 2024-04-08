package net.pladema.report.infrastructure.output;

import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.WSConfig;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateSSL;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import ar.lamansys.sgx.shared.restclient.services.RestClient;
import ar.lamansys.sgx.shared.restclient.services.RestClientInterface;
import lombok.extern.slf4j.Slf4j;
import net.pladema.report.application.ReportWSStorage;
import net.pladema.report.infrastructure.output.rest.ReportWSConfig;

@Slf4j
@ConditionalOnProperty(
		value="app.usage-metrics.enabled",
		havingValue = "true"
)
@Service
public class ReportWSStorageImpl implements ReportWSStorage {

	private final RestClientInterface restClient;

	public ReportWSStorageImpl(
			HttpClientConfiguration httpClientConfiguration,
			ReportWSConfig reportWSConfig
	) throws Exception {
		this.restClient = new RestClient(
				new RestTemplateSSL(httpClientConfiguration),
				new WSConfig(reportWSConfig.getUrl(), false)
		) {
			@Override
			public HttpHeaders getHeaders() {
				var headers = super.getHeaders();
				if (reportWSConfig.getAppId() != null && reportWSConfig.getAppKey() != null) {
					headers.add("AppId", reportWSConfig.getAppId());
					headers.add("Authorization", reportWSConfig.getAppKey());
				}
				return headers;
			}
		};
	}

	@Override
	public void saveReport(List<Map<String, Object>> reportData, String reportName) {
		log.debug("Input parameters -> reportData {}, reportName {}", reportData, reportName);
		log.info("Saving ReportWS -> {} ({})", reportName, reportData.size());
		try {
			restClient.exchangePost( relativeUrl(reportName), reportData, Object.class);
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}

	private String relativeUrl(String reportName) {
		return String.format("/report/%s", reportName);
	}

	@Override
	public String healthCheck() throws RestTemplateApiException {
		return restClient.exchangeGet(relativeUrl("healthcheck"), String.class).getBody();
	}

}
