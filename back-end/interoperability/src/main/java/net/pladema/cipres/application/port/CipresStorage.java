package net.pladema.cipres.application.port;

import ar.lamansys.sgx.shared.restclient.services.RestClient;
import ar.lamansys.sgx.shared.restclient.services.RestClientInterface;
import net.pladema.cipres.infrastructure.output.rest.CipresRestTemplate;
import net.pladema.cipres.infrastructure.output.rest.CipresWSConfig;

import org.springframework.stereotype.Service;

@Service

public class CipresStorage {

	public RestClientInterface restClient;

	public CipresWSConfig cipresWSConfig;

	public CipresStorage(CipresRestTemplate cipresRestTemplate, CipresWSConfig cipresWSConfig) {
		this.restClient = new RestClient(cipresRestTemplate, cipresWSConfig);
		this.cipresWSConfig = cipresWSConfig;
	}

}
