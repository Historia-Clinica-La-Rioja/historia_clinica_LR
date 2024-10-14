package ar.lamansys.sgh.publicapi.patient.application.deleteexternalencounter;

import ar.lamansys.sgh.publicapi.patient.application.deleteexternalencounter.exceptions.DeleteExternalEncounterAccessDeniedException;
import ar.lamansys.sgh.publicapi.patient.application.deleteexternalencounter.exceptions.DeleteExternalEncounterEnumException;
import ar.lamansys.sgh.publicapi.patient.application.deleteexternalencounter.exceptions.DeleteExternalEncounterException;
import ar.lamansys.sgh.publicapi.patient.application.port.out.ExternalEncounterStorage;
import ar.lamansys.sgh.publicapi.patient.domain.exceptions.ExternalEncounterBoException;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.service.PatientInformationPublicApiPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteExternalEncounter {

    private final ExternalEncounterStorage externalEncounterStorage;

	private final PatientInformationPublicApiPermission patientInformationPublicApiPermission;

	public void run(String externalEncounterId, Integer institutionId) throws ExternalEncounterBoException {
		assertUserCanAccess();
		log.debug("Input parameters -> externalEncounterId {}, institutionId {}", externalEncounterId, institutionId);
        if(!(externalEncounterStorage.get(externalEncounterId).getInstitutionId().equals(institutionId)))
            throw new DeleteExternalEncounterException(DeleteExternalEncounterEnumException.DIFFERENT_INSTITUTION_ID, String.format("La institución %s no pertenece al encuentro externo",institutionId));
        externalEncounterStorage.delete(externalEncounterId);
    }

	private void assertUserCanAccess(){
		if(!patientInformationPublicApiPermission.canAccessDeleteExternalEncounter())
			throw new DeleteExternalEncounterAccessDeniedException();
	}
}
