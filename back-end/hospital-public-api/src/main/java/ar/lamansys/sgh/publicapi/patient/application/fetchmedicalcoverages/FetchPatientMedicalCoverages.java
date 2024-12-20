package ar.lamansys.sgh.publicapi.patient.application.fetchmedicalcoverages;

import ar.lamansys.sgh.publicapi.patient.application.exception.PatientNotExistsException;
import ar.lamansys.sgh.publicapi.patient.application.port.out.FetchPatientMedicalCoveragesStorage;
import ar.lamansys.sgh.publicapi.patient.application.fetchpatientpersonbyid.exception.PatientPersonAccessDeniedException;
import ar.lamansys.sgh.publicapi.patient.domain.PatientMedicalCoverageBo;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.service.PatientInformationPublicApiPermission;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class FetchPatientMedicalCoverages {
	private final FetchPatientMedicalCoveragesStorage fetchPatientMedicalCoveragesStorage;

	private final PatientInformationPublicApiPermission patientInformationPublicApiPermission;

	public List<PatientMedicalCoverageBo> run(Integer patientId) {
		assertUserCanAccess();
		log.debug("Input parameters ->  patientId {}", patientId);
		List<PatientMedicalCoverageBo> result = getFromStorage(patientId);
		log.debug("Output -> {}", result);
		return result;
	}

	private List<PatientMedicalCoverageBo> getFromStorage(Integer patientId) {
		var data = fetchPatientMedicalCoveragesStorage.getMedicalCoverages(patientId);
		if (data.isEmpty()){
			throw new PatientNotExistsException();
		}
		else{
			return data.get();
		}
	}

	private void assertUserCanAccess() {
		if (!patientInformationPublicApiPermission.canAccessPersonFromIdPatient()) {
			throw new PatientPersonAccessDeniedException();
		}
	}
}
