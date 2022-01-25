package ar.lamansys.sgh.publicapi.application.deleteexternalencounter;

import ar.lamansys.sgh.publicapi.application.port.out.ExternalEncounterStorge;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteExternalEncounter {

    private final ExternalEncounterStorge externalEncounterStorge;

    public void run(String externalEncounterId) {
        log.debug("Input parameters -> externalEncounterId {}", externalEncounterId);
        externalEncounterStorge.delete(externalEncounterId);
    }
}
