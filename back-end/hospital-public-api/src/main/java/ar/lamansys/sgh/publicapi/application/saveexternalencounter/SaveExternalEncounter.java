package ar.lamansys.sgh.publicapi.application.saveexternalencounter;

import ar.lamansys.sgh.publicapi.application.port.out.ExternalEncounterStorage;
import ar.lamansys.sgh.publicapi.application.port.out.ExternalPatientStorage;
import ar.lamansys.sgh.publicapi.application.saveexternalencounter.exceptions.SaveExternalEncounterEnumException;
import ar.lamansys.sgh.publicapi.application.saveexternalencounter.exceptions.SaveExternalEncounterException;
import ar.lamansys.sgh.publicapi.domain.ExternalEncounterBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveExternalEncounter {

    private final ExternalPatientStorage externalPatientStorage;

    private final ExternalEncounterStorage externalEncounterStorage;

    public void run(ExternalEncounterBo externalEncounterBo) {
        log.debug("Input parameters -> externalEncounterBo {}", externalEncounterBo);
        externalPatientStorage.findByExternalId(externalEncounterBo.getExternalId())
                .orElseThrow(() -> new SaveExternalEncounterException(SaveExternalEncounterEnumException.UNEXISTED_EXTERNAL_ID, String.format("El id externo %s no existe", externalEncounterBo.getExternalId())));
        if(!(externalEncounterStorage.existsExternalEncounter(externalEncounterBo.getExternalEncounterId())))
            externalEncounterStorage.save(externalEncounterBo);
        else
            throw new SaveExternalEncounterException(SaveExternalEncounterEnumException.EXTERNAL_ENCOUNTER_ID_ALREADY_EXISTS, String.format("El id de encuentro externo %s ya existe",externalEncounterBo.getExternalEncounterId()));
    }
}
