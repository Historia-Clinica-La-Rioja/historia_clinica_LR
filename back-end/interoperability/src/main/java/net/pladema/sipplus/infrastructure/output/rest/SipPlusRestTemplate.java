package net.pladema.sipplus.infrastructure.output.rest;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateAuth;

import org.springframework.stereotype.Service;

@Service
public class SipPlusRestTemplate extends RestTemplateAuth {

	public SipPlusRestTemplate(
			SipPlusAuthInterceptor sipPlusAuthInterceptor,
			HttpClientConfiguration configuration
	) throws Exception {
		super(sipPlusAuthInterceptor, configuration);
	}
}
