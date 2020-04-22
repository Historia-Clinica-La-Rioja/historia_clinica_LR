package net.pladema.federar.services;

import java.util.Optional;

import net.pladema.federar.services.domain.LocalIdSearchResponse;

public interface FederarService {
	
	public Optional<LocalIdSearchResponse> searchByLocalId(Integer localId);
	
}
