package ar.lamansys.sgh.publicapi.patient.application.saveexternalpatient;

import ar.lamansys.sgh.publicapi.patient.application.port.out.ExternalPatientStorage;
import ar.lamansys.sgh.publicapi.patient.domain.ExternalPatientBo;
import ar.lamansys.sgh.publicapi.patient.domain.ExternalPatientExtendedBo;
import ar.lamansys.sgh.publicapi.patient.application.saveexternalpatient.exception.SaveExternalPatientAccessDeniedException;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.service.PatientInformationPublicApiPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveExternalPatient {

    private final ExternalPatientStorage externalPatientStorage;
	private final PatientInformationPublicApiPermission patientInformationPublicApiPermission;

	public Integer run(ExternalPatientExtendedBo epeBo) {
		assertUserCanAccess();
		log.debug("Input parameters -> externalPatientExtendedBo {}", epeBo);
        Integer patientId = externalPatientStorage.findByExternalId(epeBo.getExternalId())
                .map(ExternalPatientBo::getPatientId)
                .orElseGet(() -> externalPatientStorage.getPatientId(epeBo)
                                .orElseGet(() ->externalPatientStorage.createPatient(epeBo)));
        epeBo.setPatientId(patientId);
        externalPatientStorage.saveMedicalCoverages(epeBo);
        externalPatientStorage.save(epeBo);
        return patientId;
    }

	private void assertUserCanAccess(){
		if(!patientInformationPublicApiPermission.canAccessSaveExternalPatient())
			throw new SaveExternalPatientAccessDeniedException();
	}
}
