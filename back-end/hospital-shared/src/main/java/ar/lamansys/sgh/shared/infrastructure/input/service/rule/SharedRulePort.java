package ar.lamansys.sgh.shared.infrastructure.input.service.rule;

import java.util.List;
import java.util.Optional;

public interface SharedRulePort {

	Optional<SharedRuleDto> findRegulatedRuleByClinicalSpecialtyIdInInstitution(List<Integer> clinicalSpecialtyIds, Integer institutionId);

	Optional<SharedRuleDto> findRegulatedRuleBySnomedIdInInstitution(Integer snomedId, Integer institutionId);

	String getRuleLevelDescription (Short ruleLevelId);

}
