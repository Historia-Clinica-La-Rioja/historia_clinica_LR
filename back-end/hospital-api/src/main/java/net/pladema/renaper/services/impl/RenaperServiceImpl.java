package net.pladema.renaper.services.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.pladema.renaper.configuration.RenaperCondition;

import org.springframework.context.annotation.Conditional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import net.pladema.renaper.configuration.RenaperRestTemplateAuth;
import net.pladema.renaper.configuration.RenaperWSConfig;
import net.pladema.renaper.services.RenaperService;
import net.pladema.renaper.services.domain.PersonDataResponse;
import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;
import ar.lamansys.sgx.shared.restclient.services.RestClient;

@Service
@Conditional(RenaperCondition.class)
public class RenaperServiceImpl extends RestClient implements RenaperService {

	private RenaperWSConfig renaperWSConfig;

	private final ObjectMapper jackson;
	
	public RenaperServiceImpl(RenaperRestTemplateAuth restTemplateAuth, RenaperWSConfig wsConfig,
							  ObjectMapper jackson) {
		super(restTemplateAuth, wsConfig);
		this.renaperWSConfig = wsConfig;
		this.jackson = jackson;
	}

	@Override
	public List<PersonMedicalCoverageBo> getPersonMedicalCoverage(String nroDocumento, Short idSexo) {
		String urlWithParams = renaperWSConfig.getUrlCobertura() + "?nroDocumento=" + nroDocumento + "&idSexo=" + idSexo;
		ResponseEntity<PersonMedicalCoverageBo[]> response = exchangeGet(urlWithParams,
			PersonMedicalCoverageBo[].class);
		switch(response.getStatusCode()) {
			case OK:
				return Arrays.asList(response.getBody());
			case NO_CONTENT:
			case INTERNAL_SERVER_ERROR:
			default:
				return Collections.emptyList();
		}
	}

	@Override
	public Optional<PersonDataResponse> getPersonData(String nroDocumento, Short idSexo) {
		String urlWithParams = renaperWSConfig.getUrlPersona() + "?nroDocumento=" + nroDocumento + "&idSexo=" + idSexo; 
		try{
			ResponseEntity<String> response = exchangeGet(urlWithParams, String.class);
			switch(response.getStatusCode()){
				case OK:
					return Optional.ofNullable(jackson.readValue(response.getBody(), PersonDataResponse.class));
				case INTERNAL_SERVER_ERROR:
				default:
					return Optional.empty();
			}
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	
}
