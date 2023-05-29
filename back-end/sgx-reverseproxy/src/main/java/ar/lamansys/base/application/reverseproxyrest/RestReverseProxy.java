package ar.lamansys.base.application.reverseproxyrest;

import static ar.lamansys.base.application.reverseproxyrest.configuration.RestUtils.removeContext;

import java.io.IOException;
import java.net.URI;

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

	public ResponseEntity<?> get(HttpServletRequest request) {
		URI uri = configURI(request);

		HttpHeaders headers = new HttpHeaders(defaultHeaders);
		headers.set("Accept", request.getHeader("Accept"));
		HttpEntity<String> entity = new HttpEntity<>(headers);
		log.trace("Headers to send {}", entity);

		ResponseEntity<?> response = new ReverseProxyBo(restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class)).getResponse();
		log.debug("Response from server -> {}", response);
		return response;
	}

	public ResponseEntity<?> post(HttpServletRequest request) throws IOException {
		URI uri = configURI(request);

		byte[] requestBody = request.getInputStream().readAllBytes();
		HttpEntity<byte[]> entity = new HttpEntity<>(requestBody, copyPostHeaders(request));
		log.trace("Headers to send {}", entity);

		ResponseEntity<?> response = new ReverseProxyBo(restTemplate.exchange(uri, HttpMethod.POST, entity, byte[].class)).getResponse();
		log.debug("Response from server -> {}", response);
		return response;
	}

	private URI configURI(HttpServletRequest request) {
		String path = removeContext(request.getRequestURI(), request.getContextPath());
		log.debug("DoReverseProxy execute params -> path '{}'", path);

		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromHttpUrl(this.baseUrl)
				.path(path);
		request.getParameterMap()
				.forEach(uriBuilder::queryParam);

		return uriBuilder.build().toUri();
	}

	private HttpHeaders copyPostHeaders(HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders(defaultHeaders);
		headers.set("Accept", request.getHeader("Accept"));
		headers.set("Accept-Encoding", request.getHeader("Accept-Encoding"));
		headers.set("Content-Length", request.getHeader("Content-Length"));
		headers.set("Content-Type", request.getHeader("Content-Type"));
		return headers;
	}

}
