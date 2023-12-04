package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceBo;

import java.util.List;

public interface HistoricReferenceRegulationStorage {

	Integer saveReferenceRegulation(Integer referenceId, CompleteReferenceBo reference);

	void approveReferencesByRuleId(Integer ruleId, List<Integer> institutionIds);

	void updateRuleOnReferences(Integer ruleId, Short ruleLevel, List<Integer> ruleIdsToReplace);

}
