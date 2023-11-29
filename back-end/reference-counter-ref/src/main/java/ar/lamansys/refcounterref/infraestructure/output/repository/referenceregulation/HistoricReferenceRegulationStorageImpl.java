package ar.lamansys.refcounterref.infraestructure.output.repository.referenceregulation;

import ar.lamansys.refcounterref.application.port.HistoricReferenceRegulationStorage;
import ar.lamansys.refcounterref.domain.enums.EReferenceRegulationState;
import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.rule.SharedRuleDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.rule.SharedRulePort;

import org.springframework.stereotype.Service;

@Service
public class HistoricReferenceRegulationStorageImpl implements HistoricReferenceRegulationStorage {

	private final HistoricReferenceRegulationRepository historicReferenceRegulationRepository;
	private final SharedRulePort sharedRulePort;

	public HistoricReferenceRegulationStorageImpl(SharedRulePort sharedRulePort, HistoricReferenceRegulationRepository historicReferenceRegulationRepository){
		this.sharedRulePort = sharedRulePort;
		this.historicReferenceRegulationRepository = historicReferenceRegulationRepository;
	}


	@Override
	public Integer saveReferenceRegulation(Integer referenceId, CompleteReferenceBo reference) {
		SharedRuleDto rule;
		if (reference.getStudy() != null && reference.getStudy().getPractice() != null){
			rule = sharedRulePort.findRegulatedRuleBySnomedIdInInstitution(reference.getStudy().getPractice().getId(), reference.getDestinationInstitutionId()).orElse(null);
		} else {
			rule = sharedRulePort.findRegulatedRuleByClinicalSpecialtyIdInInstitution(reference.getClinicalSpecialtyId(), reference.getDestinationInstitutionId()).orElse(null);
		}
		if (rule == null){
			return historicReferenceRegulationRepository.save(new HistoricReferenceRegulation(null, referenceId, null, null, EReferenceRegulationState.APPROVED.getId(), null)).getId();
		}
		return historicReferenceRegulationRepository.save(new HistoricReferenceRegulation(null, referenceId, rule.getId(), rule.getLevel(), EReferenceRegulationState.WAITING_APPROVAL.getId(), null)).getId();
	}

}
