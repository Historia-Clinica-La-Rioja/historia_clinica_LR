package ar.lamansys.sgx.shared.restclient.configuration.resttemplate;

import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.RestUtils;
import ar.lamansys.sgx.shared.restclient.configuration.interceptors.LoggingRequestInterceptor;
import ar.lamansys.sgx.shared.restclient.configuration.interceptors.MonitoringRequestInterceptor;

public class RestTemplateSSL extends RestTemplate {

	public RestTemplateSSL(HttpClientConfiguration configuration) throws Exception {
		super(new BufferingClientHttpRequestFactory(getClientHttpRequestFactory(
				configuration.getTimeout(),
				configuration.isTrustInvalidCertificate(),
				configuration.getProxyHost(),
				configuration.getProxyPort()
		)));
		this.getInterceptors().add(new LoggingRequestInterceptor());
		this.getInterceptors().add(new MonitoringRequestInterceptor());
		this.setErrorHandler(new RestTemplateExceptionHandler());
	}

	private static HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory(
			Integer timeout,
			boolean trustInvalidCertificate,
			String httpProxyHost,
			Integer httpProxyPort
	) throws Exception {

		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setHttpClient(RestUtils.httpClient(
				trustInvalidCertificate,
				httpProxyHost,
				httpProxyPort
		));
		clientHttpRequestFactory.setConnectTimeout(timeout);
		clientHttpRequestFactory.setReadTimeout(timeout);
		clientHttpRequestFactory.setConnectionRequestTimeout(timeout);
		return clientHttpRequestFactory;
	}



}
