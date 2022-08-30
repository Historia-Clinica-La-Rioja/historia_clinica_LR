package ar.lamansys.sgx.shared.restclient.configuration.resttemplate;

import org.springframework.http.client.ClientHttpRequestInterceptor;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;

public class RestTemplateAuth<I extends ClientHttpRequestInterceptor> extends RestTemplateSSL {

	public RestTemplateAuth(I authInterceptor, HttpClientConfiguration configuration) throws Exception {
		super(configuration);
		this.getInterceptors().add(authInterceptor);
	}
		
}
