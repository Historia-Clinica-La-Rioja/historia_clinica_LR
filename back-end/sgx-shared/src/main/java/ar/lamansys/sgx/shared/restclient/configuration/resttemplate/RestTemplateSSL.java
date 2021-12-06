package ar.lamansys.sgx.shared.restclient.configuration.resttemplate;

import ar.lamansys.sgx.shared.restclient.configuration.RestUtils;
import ar.lamansys.sgx.shared.restclient.configuration.interceptors.LoggingRequestInterceptor;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class RestTemplateSSL extends RestTemplate {
	
	public RestTemplateSSL() throws Exception {
		super(new BufferingClientHttpRequestFactory(getClientHttpRequestFactory(null, false)));
		this.getInterceptors().add(new LoggingRequestInterceptor());
		this.setErrorHandler(new RestTemplateExceptionHandler());
	}

	public RestTemplateSSL(Integer timeout) throws Exception {
		super(new BufferingClientHttpRequestFactory(getClientHttpRequestFactory(timeout, false)));
		this.getInterceptors().add(new LoggingRequestInterceptor());
		this.setErrorHandler(new RestTemplateExceptionHandler());
	}

	public RestTemplateSSL(Integer timeout, boolean trustInvalidCertificate) throws Exception {
		super(new BufferingClientHttpRequestFactory(getClientHttpRequestFactory(timeout, trustInvalidCertificate)));
		this.getInterceptors().add(new LoggingRequestInterceptor());
		this.setErrorHandler(new RestTemplateExceptionHandler());
	}

	public RestTemplateSSL(boolean trustInvalidCertificate) throws Exception {
		super(new BufferingClientHttpRequestFactory(getClientHttpRequestFactory(null, trustInvalidCertificate)));
		this.getInterceptors().add(new LoggingRequestInterceptor());
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
