package net.pladema.cipres.application.port;

import ar.lamansys.sgx.shared.restclient.services.RestClient;
import ar.lamansys.sgx.shared.restclient.services.RestClientInterface;
import lombok.extern.slf4j.Slf4j;
import net.pladema.cipres.infrastructure.output.repository.CipresEncounter;
import net.pladema.cipres.infrastructure.output.repository.CipresEncounterRepository;
import net.pladema.cipres.infrastructure.output.rest.CipresRestTemplate;
import net.pladema.cipres.infrastructure.output.rest.CipresWSConfig;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;

@Slf4j
@Service
public class CipresStorage {

	public RestClientInterface restClient;

	public CipresWSConfig cipresWSConfig;

	private final CipresEncounterRepository cipresEncounterRepository;

	public CipresStorage(CipresRestTemplate cipresRestTemplate, CipresWSConfig cipresWSConfig, CipresEncounterRepository cipresEncounterRepository) {
		this.restClient = new RestClient(cipresRestTemplate, cipresWSConfig);
		this.cipresWSConfig = cipresWSConfig;
		this.cipresEncounterRepository = cipresEncounterRepository;
	}

	public void saveStatusError(Integer cipresEncounterId, Integer encounterId, String status, Integer responseCode) {
		var result = new CipresEncounter();
		result.setId(cipresEncounterId);
		result.setEncounterId(encounterId);
		result.setStatus(status);
		result.setResponseCode(responseCode.shortValue());
		result.setDate(LocalDate.now());
		cipresEncounterRepository.save(result);
	}

	public void handleResourceAccessException(ResourceAccessException e, Integer encounterId, Integer cipresEncounterId) {
		log.debug("Fallo en la comunicación - API SALUD", e);
		saveStatusError(cipresEncounterId, encounterId, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	public <T> boolean isSuccessfulResponse(ResponseEntity<T[]> response) {
		return response != null && response.getBody() != null && response.getBody().length > 0;
	}
}
