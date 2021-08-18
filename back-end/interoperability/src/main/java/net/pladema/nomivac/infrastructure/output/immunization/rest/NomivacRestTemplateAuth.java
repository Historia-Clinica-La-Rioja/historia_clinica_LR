package net.pladema.nomivac.infrastructure.output.immunization.rest;

import ar.lamansys.sgx.shared.restclient.configuration.interceptors.LoggingRequestInterceptor;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateAuth;
import net.pladema.nomivac.infrastructure.configuration.NomivacCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
@Conditional(NomivacCondition.class)
public class NomivacRestTemplateAuth extends RestTemplateAuth<NomivacAuthInterceptor> {

	public NomivacRestTemplateAuth(NomivacAuthInterceptor authInterceptor) throws Exception {
		super(authInterceptor, new LoggingRequestInterceptor());
	}

	
}
