package ar.lamansys.refcounterref.infraestructure.output.repository.referenceregulation;

import ar.lamansys.refcounterref.application.port.HistoricReferenceRegulationStorage;
import ar.lamansys.refcounterref.domain.enums.EReferenceRegulationState;
import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.rule.SharedRuleDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.rule.SharedRulePort;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class HistoricReferenceRegulationStorageImpl implements HistoricReferenceRegulationStorage {

	private final HistoricReferenceRegulationRepository historicReferenceRegulationRepository;
	private final SharedRulePort sharedRulePort;

	@Override
	public Integer saveReferenceRegulation(Integer referenceId, CompleteReferenceBo reference) {
		SharedRuleDto rule;
		if (reference.getStudy() != null && reference.getStudy().getPractice() != null)
			rule = sharedRulePort.findRegulatedRuleBySnomedIdInInstitution(reference.getStudy().getPractice().getId(), reference.getDestinationInstitutionId()).orElse(null);
		else
			rule = sharedRulePort.findRegulatedRuleByClinicalSpecialtyIdInInstitution(reference.getClinicalSpecialtyIds(), reference.getDestinationInstitutionId()).orElse(null);
		if (rule == null)
			return historicReferenceRegulationRepository.save(new HistoricReferenceRegulation(null, referenceId, null, null, EReferenceRegulationState.APPROVED.getId(), null)).getId();
		return historicReferenceRegulationRepository.save(new HistoricReferenceRegulation(null, referenceId, rule.getId(), rule.getLevel(), EReferenceRegulationState.WAITING_APPROVAL.getId(), null)).getId();
	}

	@Override
	public void approveReferencesByRuleId(Integer ruleId, List<Integer> institutionIds){
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
		historicReferenceRegulationRepository.updateRuleOnReferences(ruleId, ruleLevel, ruleIdsToReplace);
	}

}
