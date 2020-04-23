package net.pladema.federar.configuration;

import org.springframework.stereotype.Service;

import net.pladema.sgx.restclient.configuration.interceptors.LoggingRequestInterceptor;
import net.pladema.sgx.restclient.configuration.resttemplate.RestTemplateAuth;

@Service
public class FederarRestTemplateAuth extends RestTemplateAuth<FederarAuthInterceptor> {

	public FederarRestTemplateAuth(FederarAuthInterceptor authInterceptor,
			LoggingRequestInterceptor loggingRequestInterceptor) throws Exception {
		super(authInterceptor, loggingRequestInterceptor);
	}

	
}
