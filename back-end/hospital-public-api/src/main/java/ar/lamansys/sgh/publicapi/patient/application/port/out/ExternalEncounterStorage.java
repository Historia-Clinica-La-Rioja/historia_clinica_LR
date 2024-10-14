package ar.lamansys.sgh.publicapi.patient.application.port.out;

import ar.lamansys.sgh.publicapi.patient.domain.ExternalEncounterBo;
import ar.lamansys.sgh.publicapi.patient.domain.exceptions.ExternalEncounterBoException;

public interface ExternalEncounterStorage {

    void save(ExternalEncounterBo externalEncounterBo);

    ExternalEncounterBo get(String externalEncounterId) throws ExternalEncounterBoException;

    boolean existsExternalEncounter(String externalEncounterId);

    void delete(String externalEncounterId);
}
