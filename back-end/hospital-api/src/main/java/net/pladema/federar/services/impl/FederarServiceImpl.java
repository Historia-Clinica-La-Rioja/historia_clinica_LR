package net.pladema.federar.services.impl;

import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import net.pladema.federar.configuration.FederarAuthInterceptor;
import net.pladema.federar.configuration.FederarWSConfig;
import net.pladema.federar.services.FederarService;
import net.pladema.federar.services.domain.LocalIdSearchResponse;
import net.pladema.sgx.restclient.configuration.resttemplate.RestTemplateAuth;
import net.pladema.sgx.restclient.services.RestClient;

@Service
@Profile("prod")
public class FederarServiceImpl extends RestClient implements FederarService {

	private FederarWSConfig federarWSConfig;
	
	public FederarServiceImpl(RestTemplateAuth<FederarAuthInterceptor> restTemplateAuth, FederarWSConfig wsConfig) {
		super(restTemplateAuth, wsConfig);
		this.federarWSConfig = wsConfig;
	}

	@Override
	public Optional<LocalIdSearchResponse> searchByLocalId(Integer localId) {
		String urlWithParams = federarWSConfig.getLocalSearchIdUrl() + "?identifier=" + federarWSConfig.getIss() + "|" + localId; 
		ResponseEntity<LocalIdSearchResponse> response = exchangeGet(urlWithParams, LocalIdSearchResponse.class);
		return Optional.of(response.getBody());
	}

	
}
