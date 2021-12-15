package ar.lamansys.sgh.publicapi.application.port.out;

import ar.lamansys.sgh.publicapi.domain.ExternalEncounterBo;

public interface ExternalEncounterStorge {

    void save(ExternalEncounterBo externalEncounterBo);

    boolean existsExternalEncounter(String externalEncounterId);
}
