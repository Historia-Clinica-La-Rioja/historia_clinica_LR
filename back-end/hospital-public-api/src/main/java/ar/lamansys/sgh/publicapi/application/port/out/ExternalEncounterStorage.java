package ar.lamansys.sgh.publicapi.application.port.out;

import ar.lamansys.sgh.publicapi.domain.ExternalEncounterBo;
import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalEncounterBoException;

public interface ExternalEncounterStorage {

    void save(ExternalEncounterBo externalEncounterBo);

    ExternalEncounterBo get(String externalEncounterId) throws ExternalEncounterBoException;

    boolean existsExternalEncounter(String externalEncounterId);

    void delete(String externalEncounterId);
}
