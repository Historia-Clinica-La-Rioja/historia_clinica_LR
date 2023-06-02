package ar.lamansys.base.application.reverseproxyrest;

import ar.lamansys.base.ReverseProxyAutoConfiguration;
import ar.lamansys.base.domain.ReverseProxyBo;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

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
		MediaType mtdicom = MediaType.valueOf("multipart/related;type=\"application/dicom\"");
		this.restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
		var converter = restTemplate.getMessageConverters().stream()
				.filter(FormHttpMessageConverter.class::isInstance)
				.map(FormHttpMessageConverter.class::cast)
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Failed to find FormHttpMessageConverter"));
		converter.addSupportedMediaTypes(mtdicom);
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
		HttpEntity<?> entity = ServletFileUpload.isMultipartContent(request) ? buildMultipartEntity((StandardMultipartHttpServletRequest) request) : buildNoMultipartEntity(request);
		log.trace("Headers to send {}", entity);

		ResponseEntity<?> response = new ReverseProxyBo(restTemplate.exchange(uri, HttpMethod.POST, entity, byte[].class)).getResponse();
		log.debug("Response from server -> {}", response);
		return response;
	}

	private HttpEntity<?> buildMultipartEntity(StandardMultipartHttpServletRequest request) {
		LinkedMultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
		request.getMultiFileMap().forEach((s, multipartFiles) ->
				multipartFiles.stream()
						.filter(multipartFile -> !multipartFile.getName().equals("inputStream"))
						.forEach(multipartFile -> {
					HttpHeaders fileHeaders = new HttpHeaders();
					fileHeaders.setContentType(MediaType.parseMediaType(Objects.requireNonNull(multipartFile.getContentType())));
					form.add(s, new HttpEntity<>(multipartFile.getResource(), fileHeaders));
				}));
		return new HttpEntity<>(form, copyPostHeaders(request));
	}

	private HttpEntity<?> buildNoMultipartEntity(HttpServletRequest request) throws IOException {
		byte[] requestBody = request.getInputStream().readAllBytes();
		return new HttpEntity<>(requestBody, copyPostHeaders(request));
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
