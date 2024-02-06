package net.pladema.cipres.application.port;

import ar.lamansys.sgx.shared.restclient.services.RestClient;
import ar.lamansys.sgx.shared.restclient.services.RestClientInterface;
import net.pladema.cipres.infrastructure.output.repository.CipresEncounter;
import net.pladema.cipres.infrastructure.output.repository.CipresEncounterRepository;
import net.pladema.cipres.infrastructure.output.rest.CipresRestTemplate;
import net.pladema.cipres.infrastructure.output.rest.CipresWSConfig;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

	public void saveStatusError(Integer encounterId, String status, Integer responseCode) {
		var result = new CipresEncounter();
		result.setEncounterId(encounterId);
		result.setStatus(status);
		result.setResponseCode(responseCode.shortValue());
		result.setDate(LocalDate.now());
		cipresEncounterRepository.save(result);
	}
}
