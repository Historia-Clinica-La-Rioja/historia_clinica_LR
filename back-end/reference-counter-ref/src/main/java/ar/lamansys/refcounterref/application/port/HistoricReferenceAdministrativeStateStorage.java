package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.reference.ReferenceAdministrativeStateBo;
import ar.lamansys.refcounterref.domain.referenceregulation.ReferenceRegulationBo;

import java.util.Optional;

public interface HistoricReferenceAdministrativeStateStorage {

	Integer save(Integer referenceId, Short administrativeStateId, String reason);

	void updateReferenceAdministrativeState(Integer referenceId, Short administrativeStateId, String reason);

	Optional<ReferenceAdministrativeStateBo> getByReferenceId(Integer referenceId);

}
