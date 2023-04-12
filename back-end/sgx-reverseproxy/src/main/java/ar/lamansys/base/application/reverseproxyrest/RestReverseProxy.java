package ar.lamansys.base.application.reverseproxyrest;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import ar.lamansys.base.application.reverseproxyrest.handler.RestResponseErrorHandler;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ar.lamansys.base.ReverseProxyAutoConfiguration;
import ar.lamansys.base.application.reverseproxyrest.configuration.HttpClientConfiguration;
import ar.lamansys.base.application.reverseproxyrest.configuration.RestTemplateSSL;
import ar.lamansys.base.domain.ReverseProxyBo;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RestReverseProxy {

	private final RestTemplate restTemplate;
	private final String baseUrl;
	private final HttpHeaders defaultHeaders;

	public RestReverseProxy(ReverseProxyAutoConfiguration reverseProxyAutoConfiguration, HttpClientConfiguration configuration) throws Exception {
		this.restTemplate = new RestTemplateSSL(configuration.withProxy(reverseProxyAutoConfiguration.getProxy()));
		this.restTemplate.setErrorHandler(new RestResponseErrorHandler());
		this.baseUrl = reverseProxyAutoConfiguration.getServer();
		this.defaultHeaders = buildHeaders(reverseProxyAutoConfiguration.getHeaders());
		log.info("Reverse Proxy server enabled to forward URL '{}'", baseUrl);
	}

	public ResponseEntity<?> run(HttpServletRequest request) {
		String path = removeContext(request.getRequestURI(), request.getContextPath());
		log.trace("DoReverseProxy execute params -> path '{}'", path);
		ResponseEntity<?> response = new ReverseProxyBo(this.getAsString(path, request.getParameterMap())).getResponse();
		log.trace("Response from server -> {}", response);
		return response;
	}

	private ResponseEntity<String> getAsString(String path, Map<String, String[]> parameterMap) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromHttpUrl(this.baseUrl)
				.path(path);

		parameterMap.forEach(uriBuilder::queryParam);

		HttpEntity<String> entity = new HttpEntity<>(defaultHeaders);

		log.trace("Headers to send {}", entity);

		return restTemplate.exchange(uriBuilder.build().toUri(), HttpMethod.GET, entity, String.class);
	}

	public void addHeaders(Map<String, String> headersValues) {
		headersValues.forEach(defaultHeaders::add);
	}

	public void updateHeader(String key, String value) {
		if (defaultHeaders.containsKey(key)) defaultHeaders.set(key, value);
		else addHeaders(Map.of(key, value));
	}

	private static HttpHeaders buildHeaders(Map<String, String> headersValues) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(APPLICATION_JSON);
		headersValues.forEach(headers::add);
		return headers;
	}

	private static String removeContext(String fullURI, String prefix) {
		return fullURI.split(prefix)[1];
	}
}
