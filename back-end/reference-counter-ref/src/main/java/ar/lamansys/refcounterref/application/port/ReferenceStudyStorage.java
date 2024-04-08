package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import ar.lamansys.refcounterref.domain.snomed.SnomedBo;

import java.util.Map;

public interface ReferenceStudyStorage {

	Integer save(CompleteReferenceBo referenceBo);

	Map<Integer, SnomedBo> getReferencesProcedures(Map<Integer, Integer> referencesStudiesIds);
}
