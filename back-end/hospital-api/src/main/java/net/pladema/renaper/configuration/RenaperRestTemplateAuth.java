package net.pladema.renaper.configuration;

import ar.lamansys.sgx.shared.restclient.configuration.interceptors.LoggingRequestInterceptor;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateAuth;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

@Service
@Conditional(RenaperCondition.class)
public class RenaperRestTemplateAuth extends RestTemplateAuth<RenaperAuthInterceptor> {

	public RenaperRestTemplateAuth(RenaperAuthInterceptor authInterceptor) throws Exception {
		super(authInterceptor, new LoggingRequestInterceptor());
	}

}
