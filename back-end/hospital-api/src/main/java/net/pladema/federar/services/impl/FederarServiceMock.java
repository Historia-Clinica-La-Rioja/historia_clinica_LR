package net.pladema.federar.services.impl;

import net.pladema.federar.services.FederarService;
import net.pladema.federar.services.domain.LocalIdSearchResponse;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.person.repository.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@ConditionalOnProperty(
		value="ws.federar.enabled",
		havingValue = "false",
		matchIfMissing = true)
public class FederarServiceMock implements FederarService{

	private static final Logger LOG = LoggerFactory.getLogger(FederarServiceMock.class);

	@Override
	public Optional<LocalIdSearchResponse> searchByLocalId(Integer localId) {
		LOG.debug("FEDERAR mocked: Search by localId {} will return Empty", localId);
		return Optional.empty();
	}


	@Override
	public Optional<LocalIdSearchResponse> federatePatient(Person person, Patient patient) {
		LOG.debug("FEDERAR mocked: Patient {} won't federate", patient);
		return Optional.empty();
	}
	
}
