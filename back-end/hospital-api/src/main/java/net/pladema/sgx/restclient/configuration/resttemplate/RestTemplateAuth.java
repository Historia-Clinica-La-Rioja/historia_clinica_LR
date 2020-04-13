package net.pladema.sgx.restclient.configuration.resttemplate;

import org.springframework.http.client.ClientHttpRequestInterceptor;

import net.pladema.sgx.restclient.configuration.interceptors.LoggingRequestInterceptor;

public class RestTemplateAuth<AI extends ClientHttpRequestInterceptor> extends RestTemplateSSL {

	public RestTemplateAuth(AI authInterceptor, LoggingRequestInterceptor loggingRequestInterceptor) throws Exception {
		super(loggingRequestInterceptor);
		this.getInterceptors().add(authInterceptor);
	}
		
}
