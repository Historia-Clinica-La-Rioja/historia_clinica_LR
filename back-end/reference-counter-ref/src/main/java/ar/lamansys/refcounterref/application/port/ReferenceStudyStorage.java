package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import ar.lamansys.refcounterref.domain.snomed.SnomedBo;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

public interface ReferenceStudyStorage {

	Integer save(CompleteReferenceBo referenceBo);

	Map<Integer, Pair<SnomedBo, String>> getReferencesProcedures(Map<Integer, Integer> referencesStudiesIds);

}
