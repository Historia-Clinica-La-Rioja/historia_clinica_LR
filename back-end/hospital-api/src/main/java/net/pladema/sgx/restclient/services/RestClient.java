package net.pladema.sgx.restclient.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import net.pladema.sgx.restclient.configuration.WSConfig;
import net.pladema.sgx.restclient.configuration.resttemplate.RestTemplateSSL;

public class RestClient {
	protected final WSConfig wsConfig;
	private final RestTemplate restTemplate;

	public RestClient(RestTemplateSSL restTemplateSSL, WSConfig wsConfig) {
		this.restTemplate = restTemplateSSL;
		this.wsConfig = wsConfig;
	}

	private static HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return headers;
	}

	protected <T> ResponseEntity<T> exchangeGet(String relUrl, Class<T> responseType) {
		String fullUrl = wsConfig.getAbsoluteURL(relUrl);
		HttpEntity<String> entity = new HttpEntity<>(getHeaders());
		return restTemplate.exchange(fullUrl, HttpMethod.GET, entity, responseType);
	}

	protected <T> ResponseEntity<T> exchangeDelete(String relUrl, Class<T> responseType) {
		String fullUrl = wsConfig.getAbsoluteURL(relUrl);
		HttpEntity<String> entity = new HttpEntity<>(getHeaders());
		return restTemplate.exchange(fullUrl, HttpMethod.DELETE, entity, responseType);
	}

	protected <ResponseBody, RequestBody> ResponseEntity<ResponseBody> exchangePost(String relUrl,
			RequestBody requestBody, Class<ResponseBody> responseType) {
		String fullUrl = wsConfig.getAbsoluteURL(relUrl);
		HttpEntity<RequestBody> entity = new HttpEntity<>(requestBody, getHeaders());
		return restTemplate.exchange(fullUrl, HttpMethod.POST, entity, responseType);
	}

	protected <ResponseBody, RequestBody> ResponseEntity<ResponseBody> exchangePut(String relUrl,
			RequestBody requestBody, Class<ResponseBody> responseType) {
		String fullUrl = wsConfig.getAbsoluteURL(relUrl);
		HttpEntity<RequestBody> entity = new HttpEntity<>(requestBody, getHeaders());
		return restTemplate.exchange(fullUrl, HttpMethod.PUT, entity, responseType);
	}

}
