package net.pladema.federar.configuration;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateAuth;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

@Service
@Conditional(FederarCondition.class)
public class FederarRestTemplateAuth extends RestTemplateAuth<FederarAuthInterceptor> {

	public FederarRestTemplateAuth(FederarAuthInterceptor authInterceptor) throws Exception {
		super(authInterceptor);
	}

	
}
