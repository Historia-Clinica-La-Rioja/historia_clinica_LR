package net.pladema.renaper.configuration;

import org.springframework.stereotype.Service;

import net.pladema.sgx.restclient.configuration.interceptors.LoggingRequestInterceptor;
import net.pladema.sgx.restclient.configuration.resttemplate.RestTemplateAuth;

@Service
public class RenaperRestTemplateAuth extends RestTemplateAuth<RenaperAuthInterceptor> {

	public RenaperRestTemplateAuth(RenaperAuthInterceptor authInterceptor,
			LoggingRequestInterceptor loggingRequestInterceptor) throws Exception {
		super(authInterceptor, loggingRequestInterceptor);
	}

}
