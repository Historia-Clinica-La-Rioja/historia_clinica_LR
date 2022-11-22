package net.pladema.federar.configuration;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateAuth;

@Service
@Conditional(FederarCondition.class)
public class FederarRestTemplateAuth extends RestTemplateAuth<FederarAuthInterceptor> {

	public FederarRestTemplateAuth(
			FederarAuthInterceptor authInterceptor,
			FederarWSConfig wsConfig,
			HttpClientConfiguration configuration
	) throws Exception {
		super(authInterceptor, !wsConfig.isSettedTimeout()? configuration : configuration.withTimeout(wsConfig.getRequestTimeOut()));
	}

	
}
