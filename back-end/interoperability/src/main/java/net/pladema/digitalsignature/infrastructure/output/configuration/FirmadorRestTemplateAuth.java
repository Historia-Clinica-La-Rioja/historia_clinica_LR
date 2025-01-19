package net.pladema.digitalsignature.infrastructure.output.configuration;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateAuth;

@Service
public class FirmadorRestTemplateAuth extends RestTemplateAuth<FirmadorAuthInterceptor> {

	public FirmadorRestTemplateAuth(FirmadorAuthInterceptor authInterceptor, HttpClientConfiguration configuration) throws Exception {
		super(authInterceptor, configuration);
	}
}
