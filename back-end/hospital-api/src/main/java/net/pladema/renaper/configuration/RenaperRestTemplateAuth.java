package net.pladema.renaper.configuration;

import org.springframework.stereotype.Service;
import ar.lamansys.sgx.restclient.configuration.interceptors.LoggingRequestInterceptor;
import ar.lamansys.sgx.restclient.configuration.resttemplate.RestTemplateAuth;

@Service
public class RenaperRestTemplateAuth extends RestTemplateAuth<RenaperAuthInterceptor> {

	public RenaperRestTemplateAuth(RenaperAuthInterceptor authInterceptor) throws Exception {
		super(authInterceptor, new LoggingRequestInterceptor());
	}

}
