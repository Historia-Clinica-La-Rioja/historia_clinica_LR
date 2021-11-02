package ar.lamansys.sgx.shared.restclient.configuration.resttemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import ar.lamansys.sgx.shared.restclient.configuration.interceptors.LoggingRequestInterceptor;

public class RestTemplateSSL extends RestTemplate {
	
	public RestTemplateSSL(LoggingRequestInterceptor loggingRequestInterceptor) throws Exception {
		super(new BufferingClientHttpRequestFactory(getClientHttpRequestFactory(null)));
		this.getInterceptors().add(loggingRequestInterceptor);
		this.setErrorHandler(new RestTemplateExceptionHandler());
	}

	public RestTemplateSSL(LoggingRequestInterceptor loggingRequestInterceptor, Integer timeout) throws Exception {
		super(new BufferingClientHttpRequestFactory(getClientHttpRequestFactory(timeout)));
		this.getInterceptors().add(loggingRequestInterceptor);
		this.setErrorHandler(new RestTemplateExceptionHandler());
	}

	private static HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory(Integer timeout) throws Exception {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setHttpClient(httpClient());
		clientHttpRequestFactory.setConnectTimeout((timeout != null) ? timeout : 15000);
		clientHttpRequestFactory.setReadTimeout((timeout != null) ? timeout :15000);
		return clientHttpRequestFactory;
	}

	private static HttpClient httpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
				new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build());

		return HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider)
				.setSSLSocketFactory(socketFactory).build();
	}

}
