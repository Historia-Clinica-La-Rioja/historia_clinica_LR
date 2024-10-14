package net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.application.getinstitutionresponsibilityarea;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.output.GetInstitutionResponsibilityAreaPortImpl;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetInstitutionResponsibilityArea {

	private final GetInstitutionResponsibilityAreaPortImpl getInstitutionResponsibilityAreaPort;

	public List<GlobalCoordinatesBo> run(Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<GlobalCoordinatesBo> result = getInstitutionResponsibilityAreaPort.run(institutionId);
		log.debug("Output -> {}", result);
		return result;
	}

}
