package net.pladema.sanitaryresponsibilityarea.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.patient.repository.PatientRepository;
import net.pladema.sanitaryresponsibilityarea.application.port.output.PatientCoordinatesPort;
import net.pladema.sanitaryresponsibilityarea.domain.SanitaryRegionPatientMapCoordinatesBo;
import net.pladema.sanitaryresponsibilityarea.domain.GetPatientCoordinatesByAddedInstitutionFilterBo;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PatientCoordinatesPortImpl implements PatientCoordinatesPort {

	private final PatientRepository patientRepository;

	@Override
	public List<SanitaryRegionPatientMapCoordinatesBo> getPatientCoordinatesByAddedInstitution(GetPatientCoordinatesByAddedInstitutionFilterBo filter) {
		return patientRepository.fetchPatientCoordinatesByAddedInstitution(filter.getInstitutionId(),
				filter.getMapLowerCorner().getLatitude(),
				filter.getMapLowerCorner().getLongitude(),
				filter.getMapUpperCorner().getLatitude(),
				filter.getMapUpperCorner().getLongitude());
	}

}
