package net.pladema.renaper.configuration;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateAuth;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

@Service
@Conditional(RenaperCondition.class)
public class RenaperRestTemplateAuth extends RestTemplateAuth<RenaperAuthInterceptor> {

	public RenaperRestTemplateAuth(
			RenaperAuthInterceptor authInterceptor,
			HttpClientConfiguration configuration
	) throws Exception {
		super(authInterceptor, configuration);
	}

}
