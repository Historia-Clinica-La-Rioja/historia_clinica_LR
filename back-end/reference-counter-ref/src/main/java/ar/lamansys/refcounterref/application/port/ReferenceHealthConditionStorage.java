package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import ar.lamansys.refcounterref.domain.referenceproblem.ReferenceProblemBo;

import java.util.List;

public interface ReferenceHealthConditionStorage {

	Integer fetchHealthConditionByEncounterAndSnomedData(Integer encounterId, Integer sourceTypeId, String sctid, String pt);

	List<Integer> saveProblems(Integer referenceId, CompleteReferenceBo referenceBo);

	List<Integer> getReferenceIds(Integer healthConditionId);

	List<ReferenceProblemBo> getReferenceProblems(Integer referenceId);

}
