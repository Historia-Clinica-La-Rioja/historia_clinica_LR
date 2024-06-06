package ar.lamansys.refcounterref.infraestructure.output.repository.healthcondition;

import ar.lamansys.refcounterref.application.port.ReferenceHealthConditionStorage;
import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import ar.lamansys.refcounterref.domain.referenceproblem.ReferenceProblemBo;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencehealthcondition.ReferenceHealthCondition;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencehealthcondition.ReferenceHealthConditionPk;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencehealthcondition.ReferenceHealthConditionRepository;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHealthConditionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReferenceHealthConditionStorageImpl implements ReferenceHealthConditionStorage {

	private final SharedHealthConditionPort sharedHealthConditionPort;

	private final ReferenceHealthConditionRepository referenceHealthConditionRepository;

	@Override
	public Integer fetchHealthConditionByEncounterAndSnomedData(Integer encounterId, Integer sourceTypeId, String sctid, String pt) {
		return sharedHealthConditionPort.getHealthConditionIdByEncounterAndSnomedConcept(encounterId, sourceTypeId, sctid, pt);
	}

	@Override
	public List<Integer> saveProblems(Integer referenceId, CompleteReferenceBo referenceBo) {
		return referenceBo.getProblems().stream().map(problem -> {
			Integer healthConditionId = this.fetchHealthConditionByEncounterAndSnomedData(
					referenceBo.getEncounterId(), referenceBo.getSourceTypeId(), problem.getSnomed().getSctid(), problem.getSnomed().getPt());
			ReferenceHealthConditionPk refPk = new ReferenceHealthConditionPk(referenceId, healthConditionId);
			ReferenceHealthCondition result = referenceHealthConditionRepository.save(new ReferenceHealthCondition(refPk));
			return result.getPk().getHealthConditionId();
		}).collect(Collectors.toList());
	}

	@Override
	public List<Integer> getReferenceIds(Integer healthConditionId) {
		return referenceHealthConditionRepository.getReferenceIdsByHealthConditionId(healthConditionId);
	}

	@Override
	public List<ReferenceProblemBo> getReferenceProblems(Integer referenceId){
		return referenceHealthConditionRepository.getReferencesProblems(Collections.singletonList(referenceId));
	}
}
