package net.pladema.sanitaryresponsibilityarea.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.sanitaryresponsibilityarea.application.port.output.PatientCoordinatesPort;
import net.pladema.sanitaryresponsibilityarea.domain.SanitaryRegionPatientMapCoordinatesBo;

import net.pladema.sanitaryresponsibilityarea.domain.GetPatientCoordinatesByAddedInstitutionFilterBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetPatientCoordinatesByAddedInstitution {

	private final PatientCoordinatesPort patientCoordinatesPort;

	public List<SanitaryRegionPatientMapCoordinatesBo> run(GetPatientCoordinatesByAddedInstitutionFilterBo filter) {
		log.debug("Input parameters -> filter {}", filter);
		List<SanitaryRegionPatientMapCoordinatesBo> result = patientCoordinatesPort.getPatientCoordinatesByAddedInstitution(filter);
		log.debug("Output -> {}", result);
		return result;
	}

}
