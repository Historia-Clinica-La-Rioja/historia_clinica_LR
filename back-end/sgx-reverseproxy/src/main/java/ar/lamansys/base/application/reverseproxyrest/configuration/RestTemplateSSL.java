package ar.lamansys.base.application.reverseproxyrest.configuration;

import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestTemplateSSL extends RestTemplate {

	public RestTemplateSSL(HttpClientConfiguration configuration) throws Exception {
		super(new BufferingClientHttpRequestFactory(getClientHttpRequestFactory(configuration.getTimeout(), configuration.isTrustInvalidCertificate(), configuration.getProxy())));
	}

	private static HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory(Integer timeout, boolean trustInvalidCertificate, String httpProxy) throws Exception {

		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setHttpClient(RestUtils.httpClient(trustInvalidCertificate, httpProxy));
		clientHttpRequestFactory.setConnectTimeout(timeout);
		clientHttpRequestFactory.setReadTimeout(timeout);
		clientHttpRequestFactory.setConnectionRequestTimeout(timeout);
		return clientHttpRequestFactory;
	}


}
