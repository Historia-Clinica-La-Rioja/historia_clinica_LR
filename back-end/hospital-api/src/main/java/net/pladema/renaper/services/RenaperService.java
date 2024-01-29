package net.pladema.renaper.services;

import java.util.List;
import java.util.Optional;

import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;
import net.pladema.renaper.services.domain.PersonDataResponse;
import net.pladema.renaper.services.domain.RenaperServiceException;

public interface RenaperService {
	
	Optional<PersonDataResponse> getPersonData(String nroDocumento, Short idSexo) throws RenaperServiceException;
	
	List<PersonMedicalCoverageBo> getPersonMedicalCoverage(String nroDocumento, Short idSexo) throws RenaperServiceException;
}
