package ar.lamansys.sgh.publicapi.patient.application.saveexternalclinicalhistory;

import ar.lamansys.sgh.publicapi.patient.application.port.out.ExternalClinicalHistoryStorage;
import ar.lamansys.sgh.publicapi.patient.domain.ExternalClinicalHistoryBo;
import ar.lamansys.sgh.publicapi.patient.application.saveexternalclinicalhistory.exception.SaveExternalClinicHistoryAccessDeniedException;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.service.PatientInformationPublicApiPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveExternalClinicalHistory {

	private final ExternalClinicalHistoryStorage externalClinicalHistoryStorage;
	private final PatientInformationPublicApiPermission patientInformationPublicApiPermission;

	public Integer run(ExternalClinicalHistoryBo externalClinicalHistoryBo) {
		assertUserCanAccess();
		log.debug("Input parameter -> externalClinicalHistoryBo {}", externalClinicalHistoryBo);
		return externalClinicalHistoryStorage.create(externalClinicalHistoryBo);
	}

	private void assertUserCanAccess() {
		if (!patientInformationPublicApiPermission.canAccessSaveExternalClinicHistory())
			throw new SaveExternalClinicHistoryAccessDeniedException();
	}

}
