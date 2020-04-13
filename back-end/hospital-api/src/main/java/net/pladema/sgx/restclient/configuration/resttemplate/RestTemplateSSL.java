package net.pladema.sgx.restclient.configuration.resttemplate;

import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.pladema.sgx.restclient.configuration.interceptors.LoggingRequestInterceptor;

@Service
public class RestTemplateSSL extends RestTemplate {
	
	public RestTemplateSSL(LoggingRequestInterceptor loggingRequestInterceptor) throws Exception {
		super(new BufferingClientHttpRequestFactory(getClientHttpRequestFactory()));
		this.getInterceptors().add(loggingRequestInterceptor);
	}

	private static HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory() throws Exception {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setHttpClient(httpClient());
		clientHttpRequestFactory.setConnectTimeout(5000);
		clientHttpRequestFactory.setReadTimeout(5000);
		return clientHttpRequestFactory;
	}

	private static HttpClient httpClient() throws Exception {
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
				new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build());

		return HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider)
				.setSSLSocketFactory(socketFactory).build();
	}

}
