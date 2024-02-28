package net.pladema.establishment.infrastructure.port;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.application.port.RuleStorage;

import net.pladema.establishment.repository.RuleRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class RuleStorageImpl implements RuleStorage {

	private final RuleRepository ruleRepository;

	@Override
	public boolean isAnyClinicalSpecialtyRegulated(List<Integer> clinicalSpecialtyIds, Integer institutionId) {
		log.debug("Input parameter -> clinicalSpecialtyIds {}, institutionId {}", clinicalSpecialtyIds, institutionId);
		return !ruleRepository.findRegulatedRuleByClinicalSpecialtyIdInInstitution(clinicalSpecialtyIds, institutionId).isEmpty();
	}

	@Override
	public boolean isPracticeRegulated(Integer practiceId, Integer institutionId) {
		log.debug("Input parameter -> practiceId {}, institutionId {}", practiceId, institutionId);
		return ruleRepository.findRegulatedRuleBySnomedIdInInstitution(practiceId, institutionId).isPresent();
	}

}
