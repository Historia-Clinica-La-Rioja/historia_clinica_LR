package net.pladema.renaper.services;

import java.util.List;
import java.util.Optional;

import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;
import net.pladema.renaper.services.domain.PersonDataResponse;

public interface RenaperService {
	
	public Optional<PersonDataResponse> getPersonData(String nroDocumento, Short idSexo);
	
	public List<PersonMedicalCoverageBo> getPersonMedicalCoverage(String nroDocumento, Short idSexo);
}
