package ar.lamansys.sgh.shared.infrastructure.input.service.rule;

import java.util.Optional;

public interface SharedRulePort {

	Optional<SharedRuleDto> findRegulatedRuleByClinicalSpecialtyIdInInstitution(Integer clinicalSpecialtyId, Integer institutionId);

	Optional<SharedRuleDto> findRegulatedRuleBySnomedIdInInstitution(Integer snomedId, Integer institutionId);

}
