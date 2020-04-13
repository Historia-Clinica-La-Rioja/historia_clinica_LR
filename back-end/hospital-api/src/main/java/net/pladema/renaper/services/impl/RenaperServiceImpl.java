package net.pladema.renaper.services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import net.pladema.renaper.configuration.RenaperAuthInterceptor;
import net.pladema.renaper.configuration.RenaperWSConfig;
import net.pladema.renaper.services.RenaperService;
import net.pladema.renaper.services.domain.PersonMedicalCoverageResponse;
import net.pladema.renaper.services.domain.PersonDataResponse;
import net.pladema.sgx.restclient.configuration.resttemplate.RestTemplateAuth;
import net.pladema.sgx.restclient.services.RestClient;

@Service
@Profile("prod")
public class RenaperServiceImpl extends RestClient implements RenaperService {

	private RenaperWSConfig renaperWSConfig;
	
	public RenaperServiceImpl(RestTemplateAuth<RenaperAuthInterceptor> restTemplateAuth, RenaperWSConfig wsConfig) {
		super(restTemplateAuth, wsConfig);
		this.renaperWSConfig = wsConfig;
	}

	@Override
	public List<PersonMedicalCoverageResponse> getPersonMedicalCoverage(String nroDocumento, Integer idSexo) {
		String urlWithParams = renaperWSConfig.getUrlCobertura() + "?nroDocumento=" + nroDocumento + "&idSexo=" + idSexo; 
		ResponseEntity<PersonMedicalCoverageResponse[]> response = exchangeGet(urlWithParams, PersonMedicalCoverageResponse[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public Optional<PersonDataResponse> getPersonData(String nroDocumento, Integer idSexo) {
		String urlWithParams = renaperWSConfig.getUrlPersona() + "?nroDocumento=" + nroDocumento + "&idSexo=" + idSexo; 
		ResponseEntity<PersonDataResponse> response = exchangeGet(urlWithParams, PersonDataResponse.class);
		return Optional.of(response.getBody());
	}

	
}
