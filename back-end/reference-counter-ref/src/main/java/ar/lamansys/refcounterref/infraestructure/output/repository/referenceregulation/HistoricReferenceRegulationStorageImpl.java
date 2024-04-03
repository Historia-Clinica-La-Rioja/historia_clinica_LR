package ar.lamansys.refcounterref.infraestructure.output.repository.referenceregulation;

import ar.lamansys.refcounterref.application.port.HistoricReferenceRegulationStorage;
import ar.lamansys.refcounterref.domain.enums.EReferenceRegulationState;
import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import ar.lamansys.refcounterref.domain.referenceregulation.ReferenceRegulationBo;
import ar.lamansys.refcounterref.infraestructure.output.repository.reference.Reference;
import ar.lamansys.refcounterref.infraestructure.output.repository.reference.ReferenceRepository;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.rule.SharedRuleDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.rule.SharedRulePort;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class HistoricReferenceRegulationStorageImpl implements HistoricReferenceRegulationStorage {

	private final HistoricReferenceRegulationRepository historicReferenceRegulationRepository;

	private final ReferenceRepository referenceRepository;

	private final SharedRulePort sharedRulePort;

	private final SharedStaffPort sharedStaffPort;

	@Override
	public Short saveReferenceRegulation(Integer referenceId, CompleteReferenceBo reference) {
		log.debug("Input parameters -> referenceId {}, reference {}", referenceId, reference);
		List<SharedRuleDto> rules = new ArrayList<>();
		if (reference.getStudy() != null && reference.getStudy().getPractice() != null)
			addRegulatedRuleBySnomedAndInstitution(reference, rules);
		else
			rules = sharedRulePort.findRegulatedRuleByClinicalSpecialtyIdInInstitution(reference.getClinicalSpecialtyIds(), reference.getInstitutionId());
		if (rules.isEmpty())
			return saveEmptyRegulation(referenceId);
		rules.forEach(rule -> saveHistoricReferenceRegulation(referenceId, rule));
		return EReferenceRegulationState.WAITING_APPROVAL.getId();
	}

	private Short saveEmptyRegulation(Integer referenceId) {
		Short regulationStateId =  EReferenceRegulationState.APPROVED.getId();
		HistoricReferenceRegulation emptyRegulation = new HistoricReferenceRegulation(null, referenceId, null, null, regulationStateId, null);
		historicReferenceRegulationRepository.save(emptyRegulation);
		return regulationStateId;
	}

	private void saveHistoricReferenceRegulation(Integer referenceId, SharedRuleDto rule) {
		HistoricReferenceRegulation historicReferenceRegulation = new HistoricReferenceRegulation(null, referenceId, rule.getId(), rule.getLevel(), EReferenceRegulationState.WAITING_APPROVAL.getId(), null);
		historicReferenceRegulationRepository.save(historicReferenceRegulation);
	}

	private void addRegulatedRuleBySnomedAndInstitution(CompleteReferenceBo reference, List<SharedRuleDto> rules) {
        sharedRulePort.findRegulatedRuleBySnomedIdInInstitution(reference.getStudy().getPractice().getId(), reference.getInstitutionId()).ifPresent(rules::add);
    }

	@Override
	public void approveReferencesByRuleId(Integer ruleId, List<Integer> institutionIds){
		log.debug("Input parameters -> ruleId {}, institutionIds {}", ruleId, institutionIds);
		List<HistoricReferenceRegulation> historicReferenceRegulations;
		if (institutionIds.isEmpty())
			historicReferenceRegulations = historicReferenceRegulationRepository.findByRuleId(ruleId);
		else
			historicReferenceRegulations = historicReferenceRegulationRepository.findByRuleIdInInstitutions(ruleId, institutionIds);
		Map<Integer, Short> referencesMap = new HashMap<>();
		historicReferenceRegulations.forEach(hrr -> {
			if(!referencesMap.containsKey(hrr.getReferenceId()))
				referencesMap.put(hrr.getReferenceId(), hrr.getStateId());
		});
		referencesMap.forEach((referenceId, stateId) -> {
			if (stateId.equals(EReferenceRegulationState.WAITING_APPROVAL.getId()))
				saveHistoricReferenceRegulation(referenceId);
		});
	}

	private void saveHistoricReferenceRegulation(Integer referenceId) {
		HistoricReferenceRegulation entity = new HistoricReferenceRegulation();
		entity.setReferenceId(referenceId);
		entity.setStateId(EReferenceRegulationState.APPROVED.getId());
		historicReferenceRegulationRepository.save(entity);
	}

	@Override
	public void updateRuleOnReferences(Integer ruleId, Short ruleLevel, List<Integer> ruleIdsToReplace){
		log.debug("Input parameters -> ruleId {}, ruleLevel {}, ruleIdsToReplace {}", ruleId, ruleLevel, ruleIdsToReplace);
		historicReferenceRegulationRepository.updateRuleOnReferences(ruleId, ruleLevel, ruleIdsToReplace);
	}

	@Override
	public Optional<ReferenceRegulationBo> getByReferenceId(Integer referenceId){
		log.debug("Input parameters -> referenceId {}", referenceId);
		Optional<ReferenceRegulationBo> result = historicReferenceRegulationRepository.getByReferenceId(referenceId).stream().map(this::mapToBo).findFirst();
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Boolean updateReferenceRegulationState(Integer referenceId, Short stateId, String reason){
		log.debug("Input parameters -> referenceId {}, stateId {}, reason {}", referenceId, stateId, reason);
		Optional<HistoricReferenceRegulation> hrr = historicReferenceRegulationRepository.getByReferenceId(referenceId).stream().findFirst();
		if(validRegulationState(hrr)){
			historicReferenceRegulationRepository.save(new HistoricReferenceRegulation(null, referenceId, hrr.get().getRuleId(), hrr.get().getRuleLevel(), stateId, reason));
			updateReferenceRegulationStateId(referenceId, stateId);
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private void updateReferenceRegulationStateId(Integer referenceId, Short stateId) {
		log.debug("Input parameters -> referenceId {}, stateId {} ", referenceId, stateId);
		Reference reference = referenceRepository.getById(referenceId);
		reference.setRegulationStateId(stateId);
		referenceRepository.save(reference);
	}

	private boolean validRegulationState(Optional<HistoricReferenceRegulation> hrr){
		return hrr.isPresent() && hrr.get().getStateId().equals(EReferenceRegulationState.WAITING_APPROVAL.getId());
	}

	ReferenceRegulationBo mapToBo(HistoricReferenceRegulation entity){
		ReferenceRegulationBo result = new ReferenceRegulationBo();
		result.setId(entity.getId());
		result.setReferenceId(entity.getReferenceId());
		result.setRuleId(entity.getRuleId());
		result.setRuleLevel(entity.getRuleLevel() != null ? sharedRulePort.getRuleLevelDescription(entity.getRuleLevel()) : null);
		result.setState(EReferenceRegulationState.getById(entity.getStateId()));
		result.setReason(entity.getReason());
		result.setCreatedOn(entity.getCreatedOn());
		result.setProfessionalName(entity.getRuleId() != null ? sharedStaffPort.getProfessionalCompleteNameByUserId(entity.getCreatedBy()).orElse(null) : null);
		return result;
	}

}
