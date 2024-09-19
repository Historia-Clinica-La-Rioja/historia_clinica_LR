package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import ar.lamansys.refcounterref.domain.referenceregulation.ReferenceRegulationBo;

import java.util.List;
import java.util.Optional;

public interface HistoricReferenceRegulationStorage {

	Short saveReferenceRegulation(Integer referenceId, CompleteReferenceBo reference);

	void approveReferencesByRuleId(Integer ruleId, List<Integer> institutionIds);

	void updateRuleOnReferences(Integer ruleId, Short ruleLevel, List<Integer> ruleIdsToReplace);

	Optional<ReferenceRegulationBo> getByReferenceId(Integer referenceId);

	void updateReferenceRegulationState(Integer referenceId, Short regulationStatusId, String reason);

}
