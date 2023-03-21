package net.pladema.federar.services;

import net.pladema.federar.services.domain.FederarResourceAttributes;
import net.pladema.federar.services.exceptions.FederarApiException;

import java.util.Optional;

public interface FederarService {
	
	Optional<Integer> federatePatient(FederarResourceAttributes attributes, Integer localId) throws FederarApiException;
	
}
