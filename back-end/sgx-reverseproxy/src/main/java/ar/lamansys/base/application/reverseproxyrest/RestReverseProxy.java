package ar.lamansys.base.application.reverseproxyrest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ar.lamansys.base.ReverseProxyAutoConfiguration;
import ar.lamansys.base.domain.ReverseProxyBo;
import lombok.extern.slf4j.Slf4j;

import static ar.lamansys.base.application.reverseproxyrest.configuration.RestUtils.removeContext;

@Service
@Slf4j
public class RestReverseProxy {

	private final RestTemplate restTemplate;
	private final HttpHeaders defaultHeaders;
	private final String baseUrl;

	public RestReverseProxy(ReverseProxyAutoConfiguration reverseProxyAutoConfiguration, RestTemplate restTemplate, HttpHeaders defaultHeaders) {
		this.baseUrl = reverseProxyAutoConfiguration.getServer();
		this.restTemplate = restTemplate;
		this.defaultHeaders = defaultHeaders;
		log.info("Reverse Proxy server enabled to forward URL '{}'", baseUrl);
	}

	public ResponseEntity<?> run(HttpServletRequest request) {
		String path = removeContext(request.getRequestURI(), request.getContextPath());
		log.debug("DoReverseProxy execute params -> path '{}'", path);

		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromHttpUrl(this.baseUrl)
				.path(path);
		request.getParameterMap()
				.forEach(uriBuilder::queryParam);

		HttpHeaders headers = new HttpHeaders(defaultHeaders);
		headers.set("Accept", request.getHeader("Accept"));
		HttpEntity<String> entity = new HttpEntity<>(headers);
		log.trace("Headers to send {}", entity);

		ResponseEntity<?> response = new ReverseProxyBo(restTemplate.exchange(uriBuilder.build().toUri(), HttpMethod.GET, entity, byte[].class)).getResponse();
		log.debug("Response from server -> {}", response);
		return response;
	}
}
