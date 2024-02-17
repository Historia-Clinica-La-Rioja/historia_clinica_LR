package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.reference.ReferenceObservationBo;

import java.util.Optional;

public interface ReferenceObservationStorage {

	void save(Integer referenceId, String observation);

	Optional<ReferenceObservationBo> getReferenceObservation(Integer referenceId);

}
