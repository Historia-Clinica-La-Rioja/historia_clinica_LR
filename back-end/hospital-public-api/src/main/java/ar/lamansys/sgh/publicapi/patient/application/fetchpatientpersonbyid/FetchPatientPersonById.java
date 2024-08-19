package ar.lamansys.sgh.publicapi.patient.application.fetchpatientpersonbyid;

import ar.lamansys.sgh.publicapi.patient.application.fetchpatientpersonbyid.exception.PatientNotExistsException;
import ar.lamansys.sgh.publicapi.patient.application.fetchpatientpersonbyid.exception.PatientAccessDeniedException;
import ar.lamansys.sgh.publicapi.application.port.out.ExternalPatientStorage;

import ar.lamansys.sgh.publicapi.patient.domain.PersonBo;

import ar.lamansys.sgh.publicapi.patient.infrastructure.input.service.PatientInformationPublicApiPermission;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class FetchPatientPersonById {

	private final ExternalPatientStorage externalPatientStorage;

	private final PatientInformationPublicApiPermission patientInformationPublicApiPermission;

	public PersonBo run(String patientId) {
		assertUserCanAccess();
		log.debug("Input parameters ->  patientId {}", patientId);
		PersonBo result = getFromStorage(patientId);
		log.debug("Output -> {}", result);
		return result;
	}

	private PersonBo getFromStorage(String patientId) {
		var data = externalPatientStorage.getPersonDataById(patientId);
		if (data.isEmpty()){
			throw new PatientNotExistsException();
		}
		else{
			return data.get();
		}
	}

	private void assertUserCanAccess() {
		if (!patientInformationPublicApiPermission.canAccessPersonFromIdPatient()) {
			throw new PatientAccessDeniedException();
		}
	}

}
