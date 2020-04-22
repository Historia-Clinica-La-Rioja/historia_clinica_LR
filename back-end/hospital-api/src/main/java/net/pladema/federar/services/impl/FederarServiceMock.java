package net.pladema.federar.services.impl;

import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import net.pladema.federar.services.FederarService;
import net.pladema.federar.services.domain.LocalIdSearchResponse;

@Service
@Profile("!prod")
public class FederarServiceMock implements FederarService{


	@Override
	public Optional<LocalIdSearchResponse> searchByLocalId(Integer localId) {
		return Optional.empty();
	}

	
}
