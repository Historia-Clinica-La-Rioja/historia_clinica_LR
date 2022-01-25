package ar.lamansys.sgh.publicapi.application.deleteexternalencounter;

import ar.lamansys.sgh.publicapi.application.deleteexternalencounter.exceptions.DeleteExternalEncounterEnumException;
import ar.lamansys.sgh.publicapi.application.deleteexternalencounter.exceptions.DeleteExternalEncounterException;
import ar.lamansys.sgh.publicapi.application.port.out.ExternalEncounterStorage;
import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalEncounterBoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteExternalEncounter {

    private final ExternalEncounterStorage externalEncounterStorage;

    public void run(String externalEncounterId, Integer institutionId) throws ExternalEncounterBoException {
        log.debug("Input parameters -> externalEncounterId {}, institutionId {}", externalEncounterId, institutionId);
        if(!(externalEncounterStorage.get(externalEncounterId).getInstitutionId().equals(institutionId)))
            throw new DeleteExternalEncounterException(DeleteExternalEncounterEnumException.DIFFERENT_INSTITUTION_ID, String.format("La institución %s no pertenece al encuentro externo",institutionId));
        externalEncounterStorage.delete(externalEncounterId);
    }
}
