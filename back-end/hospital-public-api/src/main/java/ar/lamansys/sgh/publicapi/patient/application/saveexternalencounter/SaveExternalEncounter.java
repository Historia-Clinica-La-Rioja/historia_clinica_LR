package ar.lamansys.sgh.publicapi.patient.application.saveexternalencounter;

import ar.lamansys.sgh.publicapi.patient.application.port.out.ExternalEncounterStorage;
import ar.lamansys.sgh.publicapi.patient.application.port.out.ExternalPatientStorage;
import ar.lamansys.sgh.publicapi.patient.application.saveexternalencounter.exceptions.SaveExternalEncounterAccessDeniedException;
import ar.lamansys.sgh.publicapi.patient.application.saveexternalencounter.exceptions.SaveExternalEncounterEnumException;
import ar.lamansys.sgh.publicapi.patient.application.saveexternalencounter.exceptions.SaveExternalEncounterException;
import ar.lamansys.sgh.publicapi.patient.domain.ExternalEncounterBo;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.service.PatientInformationPublicApiPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveExternalEncounter {

    private final ExternalPatientStorage externalPatientStorage;

    private final ExternalEncounterStorage externalEncounterStorage;

	private final PatientInformationPublicApiPermission patientInformationPublicApiPermission;

	public void run(ExternalEncounterBo externalEncounterBo) {
		assertUserCanAccess();
		log.debug("Input parameters -> externalEncounterBo {}", externalEncounterBo);
        externalPatientStorage.findByExternalId(externalEncounterBo.getExternalId())
                .orElseThrow(() -> new SaveExternalEncounterException(SaveExternalEncounterEnumException.UNEXISTED_EXTERNAL_ID, String.format("El id externo %s no existe", externalEncounterBo.getExternalId())));
        if(!(externalEncounterStorage.existsExternalEncounter(externalEncounterBo.getExternalEncounterId())))
            externalEncounterStorage.save(externalEncounterBo);
        else
            throw new SaveExternalEncounterException(SaveExternalEncounterEnumException.EXTERNAL_ENCOUNTER_ID_ALREADY_EXISTS, String.format("El id de encuentro externo %s ya existe",externalEncounterBo.getExternalEncounterId()));
    }

	private void assertUserCanAccess(){
		if(!patientInformationPublicApiPermission.canAccessSaveExternalEncounter())
			throw new SaveExternalEncounterAccessDeniedException();
	}
}
