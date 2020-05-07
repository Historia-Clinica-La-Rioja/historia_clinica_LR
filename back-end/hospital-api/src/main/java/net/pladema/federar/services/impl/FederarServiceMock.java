package net.pladema.federar.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import net.pladema.federar.services.FederarService;
import net.pladema.federar.services.domain.LocalIdSearchResponse;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.person.repository.entity.Person;

@Service
@Profile("!prod")
public class FederarServiceMock implements FederarService{

	private static final Logger LOG = LoggerFactory.getLogger(FederarServiceMock.class);

	@Override
	public Optional<LocalIdSearchResponse> searchByLocalId(Integer localId) {
		LOG.debug("FEDERAR mocked: Search by localId {} will return Empty", localId);
		return Optional.empty();
	}


	@Override
	@Async
	public Optional<LocalIdSearchResponse> federatePatient(Person person, Patient patient) {
		LOG.debug("FEDERAR mocked: Patient {} won't federate", patient);
		return Optional.empty();
	}
	
}
