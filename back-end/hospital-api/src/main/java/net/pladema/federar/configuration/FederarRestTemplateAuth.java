package net.pladema.federar.configuration;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.restclient.configuration.interceptors.LoggingRequestInterceptor;
import ar.lamansys.sgx.restclient.configuration.resttemplate.RestTemplateAuth;

@Service
public class FederarRestTemplateAuth extends RestTemplateAuth<FederarAuthInterceptor> {

	public FederarRestTemplateAuth(FederarAuthInterceptor authInterceptor) throws Exception {
		super(authInterceptor, new LoggingRequestInterceptor());
	}


}
