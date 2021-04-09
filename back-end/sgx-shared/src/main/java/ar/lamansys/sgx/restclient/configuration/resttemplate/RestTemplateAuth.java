package ar.lamansys.sgx.restclient.configuration.resttemplate;

import ar.lamansys.sgx.restclient.configuration.interceptors.LoggingRequestInterceptor;
import org.springframework.http.client.ClientHttpRequestInterceptor;

public class RestTemplateAuth<I extends ClientHttpRequestInterceptor> extends RestTemplateSSL {

	public RestTemplateAuth(I authInterceptor, LoggingRequestInterceptor loggingRequestInterceptor) throws Exception {
		super(loggingRequestInterceptor);
		this.getInterceptors().add(authInterceptor);
	}
		
}
