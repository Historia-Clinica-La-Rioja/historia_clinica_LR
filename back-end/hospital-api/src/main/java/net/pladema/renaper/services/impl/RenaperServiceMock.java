package net.pladema.renaper.services.impl;

import net.pladema.renaper.services.RenaperService;
import net.pladema.renaper.services.domain.PersonDataResponse;
import net.pladema.renaper.services.domain.PersonMedicalCoverageResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Profile("!prod")
public class RenaperServiceMock implements RenaperService{

	@Override
	public List<PersonMedicalCoverageResponse> getPersonMedicalCoverage(String nroDocumento, Short idSexo) {
		return Collections.emptyList();
	}

	@Override
	public Optional<PersonDataResponse> getPersonData(String nroDocumento, Short idSexo) {
		return Optional.empty();
	}

	
}
