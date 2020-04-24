package net.pladema.renaper.services;

import java.util.List;
import java.util.Optional;

import net.pladema.renaper.services.domain.PersonMedicalCoverageResponse;
import net.pladema.renaper.services.domain.PersonDataResponse;

public interface RenaperService {
	
	public Optional<PersonDataResponse> getPersonData(String nroDocumento, Short idSexo);
	
	public List<PersonMedicalCoverageResponse> getPersonMedicalCoverage(String nroDocumento, Short idSexo);
}
