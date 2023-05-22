package net.pladema.cipres.infrastructure.output.rest;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateAuth;

import org.springframework.stereotype.Service;

@Service
public class CipresRestTemplate extends RestTemplateAuth<CipresAuthInterceptor> {

	public CipresRestTemplate(
			CipresAuthInterceptor cipresAuthInterceptor,
			HttpClientConfiguration configuration
	) throws Exception {
		super(cipresAuthInterceptor, configuration);
	}
}
