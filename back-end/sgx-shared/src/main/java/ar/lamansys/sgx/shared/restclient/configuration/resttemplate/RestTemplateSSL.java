package ar.lamansys.sgx.shared.restclient.configuration.resttemplate;

import ar.lamansys.sgx.shared.restclient.configuration.RestUtils;
import ar.lamansys.sgx.shared.restclient.configuration.interceptors.LoggingRequestInterceptor;
import ar.lamansys.sgx.shared.restclient.configuration.interceptors.MonitoringRequestInterceptor;

import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestTemplateSSL extends RestTemplate {

	public RestTemplateSSL() throws Exception {
		super(new BufferingClientHttpRequestFactory(getClientHttpRequestFactory(null, false)));
		this.getInterceptors().add(new LoggingRequestInterceptor());
		this.getInterceptors().add(new MonitoringRequestInterceptor());
		this.setErrorHandler(new RestTemplateExceptionHandler());
	}

	public RestTemplateSSL(Integer timeout) throws Exception {
		super(new BufferingClientHttpRequestFactory(getClientHttpRequestFactory(timeout, false)));
		this.getInterceptors().add(new LoggingRequestInterceptor());
		this.getInterceptors().add(new MonitoringRequestInterceptor());
		this.setErrorHandler(new RestTemplateExceptionHandler());
	}

	public RestTemplateSSL(Integer timeout, boolean trustInvalidCertificate) throws Exception {
		super(new BufferingClientHttpRequestFactory(getClientHttpRequestFactory(timeout, trustInvalidCertificate)));
		this.getInterceptors().add(new LoggingRequestInterceptor());
		this.getInterceptors().add(new MonitoringRequestInterceptor());
		this.setErrorHandler(new RestTemplateExceptionHandler());
	}

	public RestTemplateSSL(boolean trustInvalidCertificate) throws Exception {
		super(new BufferingClientHttpRequestFactory(getClientHttpRequestFactory(null, trustInvalidCertificate)));
		this.getInterceptors().add(new LoggingRequestInterceptor());
		this.getInterceptors().add(new MonitoringRequestInterceptor());
		this.setErrorHandler(new RestTemplateExceptionHandler());
	}

	private static HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory(Integer timeout, boolean trustInvalidCertificate) throws Exception {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setHttpClient(RestUtils.httpClient(trustInvalidCertificate));
		clientHttpRequestFactory.setConnectTimeout((timeout != null) ? timeout : 15000);
		clientHttpRequestFactory.setReadTimeout((timeout != null) ? timeout :15000);
		return clientHttpRequestFactory;
	}



}
