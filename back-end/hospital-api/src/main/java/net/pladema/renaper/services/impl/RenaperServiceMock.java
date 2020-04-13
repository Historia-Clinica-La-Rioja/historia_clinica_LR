package net.pladema.renaper.services.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import net.pladema.renaper.services.RenaperService;
import net.pladema.renaper.services.domain.PersonMedicalCoverageResponse;
import net.pladema.renaper.services.domain.PersonDataResponse;

@Service
@Profile("!prod")
public class RenaperServiceMock implements RenaperService{

	@Override
	public List<PersonMedicalCoverageResponse> getPersonMedicalCoverage(String nroDocumento, Integer idSexo) {
		return Collections.emptyList();
	}

	@Override
	public Optional<PersonDataResponse> getPersonData(String nroDocumento, Integer idSexo) {
		return Optional.empty();
	}

	
}
