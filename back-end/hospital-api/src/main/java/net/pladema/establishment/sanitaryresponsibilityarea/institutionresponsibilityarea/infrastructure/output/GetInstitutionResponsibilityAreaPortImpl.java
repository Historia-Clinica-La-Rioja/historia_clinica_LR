package net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.application.port.output.GetInstitutionResponsibilityAreaPort;
import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.output.repository.InstitutionResponsibilityAreaRepository;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetInstitutionResponsibilityAreaPortImpl implements GetInstitutionResponsibilityAreaPort {

	private final InstitutionResponsibilityAreaRepository institutionResponsibilityAreaRepository;

	@Override
	public List<GlobalCoordinatesBo> run(Integer institutionId) {
		return institutionResponsibilityAreaRepository.fetchInstitutionResponsibilityArea(institutionId);
	}

}
