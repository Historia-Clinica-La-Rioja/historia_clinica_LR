package net.pladema.establishment.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.rule.SharedRuleDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.rule.SharedRulePort;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.infrastructure.input.rest.mapper.RuleMapper;

import net.pladema.establishment.repository.RuleRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class SharedRulePortImpl implements SharedRulePort {

	private final RuleRepository ruleRepository;
	private final RuleMapper ruleMapper;

	public SharedRulePortImpl(RuleRepository ruleRepository, RuleMapper ruleMapper){
		this.ruleRepository = ruleRepository;
		this.ruleMapper = ruleMapper;
	}


	@Override
	public Optional<SharedRuleDto> findRegulatedRuleByClinicalSpecialtyIdInInstitution(Integer clinicalSpecialtyId, Integer institutionId) {
		log.debug("Input parameters -> clinicalSpecialtyId {}, institutionId {}", clinicalSpecialtyId, institutionId);
		return ruleRepository.findRegulatedRuleByClinicalSpecialtyIdInInstitution(clinicalSpecialtyId, institutionId).map(ruleMapper::fromRule);
	}

	@Override
	public Optional<SharedRuleDto> findRegulatedRuleBySnomedIdInInstitution(Integer snomedId, Integer institutionId) {
		log.debug("Input parameters -> snomedId {}, institutionId {}", snomedId, institutionId);
		return ruleRepository.findRegulatedRuleBySnomedIdInInstitution(snomedId, institutionId).map(ruleMapper::fromRule);
    }
}
