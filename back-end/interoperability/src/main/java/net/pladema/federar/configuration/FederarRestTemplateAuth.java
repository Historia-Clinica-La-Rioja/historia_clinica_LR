package net.pladema.federar.configuration;

import ar.lamansys.sgx.restclient.configuration.interceptors.LoggingRequestInterceptor;
import ar.lamansys.sgx.restclient.configuration.resttemplate.RestTemplateAuth;
import org.springframework.stereotype.Service;

@Service
public class FederarRestTemplateAuth extends RestTemplateAuth<FederarAuthInterceptor> {

	public FederarRestTemplateAuth(FederarAuthInterceptor authInterceptor) throws Exception {
		super(authInterceptor, new LoggingRequestInterceptor());
	}

	
}
