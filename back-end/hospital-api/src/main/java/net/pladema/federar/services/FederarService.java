package net.pladema.federar.services;

import java.util.Optional;

import net.pladema.federar.services.domain.LocalIdSearchResponse;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.person.repository.entity.Person;

public interface FederarService {
	
	public Optional<LocalIdSearchResponse> searchByLocalId(Integer localId);
	
	public Optional<LocalIdSearchResponse> federatePatient(Person person, Patient patient);
	
}
