package net.pladema.establishment.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.rule.SharedRuleDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.rule.SharedRulePort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.controller.dto.ERuleLevel;
import net.pladema.establishment.infrastructure.input.rest.mapper.RuleMapper;

import net.pladema.establishment.repository.RuleRepository;

import net.pladema.establishment.repository.entity.Rule;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class SharedRulePortImpl implements SharedRulePort {

	private final RuleRepository ruleRepository;
	private final RuleMapper ruleMapper;

	@Override
	public List<SharedRuleDto> findRegulatedRuleByClinicalSpecialtyIdInInstitution(List<Integer> clinicalSpecialtyIds, Integer institutionId) {
		log.debug("Input parameters -> clinicalSpecialtyIds {}, institutionId {}", clinicalSpecialtyIds, institutionId);
		List<Rule> rules = ruleRepository.findRegulatedRuleByClinicalSpecialtyIdInInstitution(clinicalSpecialtyIds, institutionId);
		List<SharedRuleDto> result = rules.stream().map(ruleMapper::fromRule).collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Optional<SharedRuleDto> findRegulatedRuleBySnomedIdInInstitution(Integer snomedId, Integer institutionId) {
		log.debug("Input parameters -> snomedId {}, institutionId {}", snomedId, institutionId);
		return ruleRepository.findRegulatedRuleBySnomedIdInInstitution(snomedId, institutionId).map(ruleMapper::fromRule);
	}

	@Override
	public String getRuleLevelDescription(Short ruleLevelId){
		log.debug("Input parameters -> ruleLevelId {}", ruleLevelId);
		String result = ERuleLevel.map(ruleLevelId).getValue();
		log.debug("Output -> {}", result);
		return result;
	}

}
