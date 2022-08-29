package ar.lamansys.sgx.shared.restclient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateSSL;

public class HttpClientTest {
	private final RestTemplate restTemplate;

	public HttpClientTest(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public static void main(String[] args) throws Exception {

		var restTemplate = new RestTemplateSSL(
				new HttpClientConfiguration(
						"http://localhost:3128",
						15000,
						false
				)
		);

		var httpClient = new HttpClientTest(
				restTemplate
		);

		httpClient.fetch(
				UriComponentsBuilder
						.fromHttpUrl("http://hsi.test1-os.dev-env.lamansys.ar")
						.path("/api/public/info")
		);
	}

	private void fetch(
			UriComponentsBuilder uriBuilder
	) {
		HttpHeaders defaultHeaders = new HttpHeaders();
		defaultHeaders.setContentType(APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(defaultHeaders);
		var response = restTemplate.exchange(
						uriBuilder.build().toUri(),
						HttpMethod.GET,
						entity,
						String.class
				);
		System.out.println("StatusCode: " + response.getStatusCode());
		System.out.println(response.getBody());
	}
}
