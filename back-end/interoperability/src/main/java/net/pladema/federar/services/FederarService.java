package net.pladema.federar.services;

import net.pladema.federar.services.domain.FederarResourceAttributes;

import java.util.Optional;

public interface FederarService {
	
	Optional<Integer> federatePatient(FederarResourceAttributes attributes, Integer localId);
	
}
