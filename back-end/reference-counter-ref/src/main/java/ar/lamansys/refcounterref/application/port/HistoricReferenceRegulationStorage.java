package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceBo;

public interface HistoricReferenceRegulationStorage {

	Integer saveReferenceRegulation(Integer referenceId, CompleteReferenceBo reference);

}
