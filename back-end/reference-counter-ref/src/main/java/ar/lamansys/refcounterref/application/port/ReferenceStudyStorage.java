package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;

public interface ReferenceStudyStorage {

	Integer run(CompleteReferenceBo referenceBo);

}
