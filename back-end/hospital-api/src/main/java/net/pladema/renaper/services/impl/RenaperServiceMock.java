package net.pladema.renaper.services.impl;

import net.pladema.renaper.services.RenaperService;
import net.pladema.renaper.services.domain.PersonDataResponse;
import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@ConditionalOnProperty(
		value="ws.renaper.enabled",
		havingValue = "false",
		matchIfMissing = true)
public class RenaperServiceMock implements RenaperService{

	@Override
	public List<PersonMedicalCoverageBo> getPersonMedicalCoverage(String nroDocumento, Short idSexo) {
		return Collections.emptyList();
	}

	@Override
	public Optional<PersonDataResponse> getPersonData(String nroDocumento, Short idSexo) {
		return Optional.empty();
	}

	
}
