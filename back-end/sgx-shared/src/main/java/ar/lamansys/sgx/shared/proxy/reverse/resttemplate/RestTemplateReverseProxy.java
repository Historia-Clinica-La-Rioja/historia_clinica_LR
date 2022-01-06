package ar.lamansys.sgx.shared.proxy.reverse.resttemplate;

import ar.lamansys.sgx.shared.proxy.reverse.ReverseProxy;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateSSL;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class RestTemplateReverseProxy implements ReverseProxy {
	private final RestTemplate restTemplate;
	private final String baseUrl;
	private final Map<String, String> defaultHeaders;

	public RestTemplateReverseProxy(
			String baseUrl,
			Map<String, String> defaultHeaders
	) throws Exception {
		this.restTemplate = new RestTemplateSSL();
		this.restTemplate.setErrorHandler(new ReverseProxyResponseErrorHandler());
		this.baseUrl = baseUrl;
		this.defaultHeaders = defaultHeaders;
	}


	@Override
	public ResponseEntity<String> getAsString(String path, Map<String, String[]> parameterMap) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromHttpUrl(this.baseUrl)
				.path(path);

		parameterMap
				.forEach(uriBuilder::queryParam);

		HttpEntity<String> entity = new HttpEntity<>(buildHeaders(this.defaultHeaders));

		return restTemplate.exchange(
				uriBuilder.build().toUri(),
				HttpMethod.GET,
				entity,
				String.class
		);
	}

	private static HttpHeaders buildHeaders(Map<String, String> headersValues) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(APPLICATION_JSON);
		headersValues.forEach(headers::add);
		return headers;
	}
}
