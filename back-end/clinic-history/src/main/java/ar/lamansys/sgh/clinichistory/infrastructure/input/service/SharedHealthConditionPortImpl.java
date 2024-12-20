package ar.lamansys.sgh.clinichistory.infrastructure.input.service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.healthCondition.HealthConditionStorage;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEHealthConditionBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.HCEHealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEHealthConditionVo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHealthConditionPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class SharedHealthConditionPortImpl implements SharedHealthConditionPort {

	private final HealthConditionStorage healthConditionStorage;
	private final HCEHealthConditionRepository hceHealthConditionRepository;

	@Override
	public Integer getHealthConditionIdByEncounterAndSnomedConcept(Integer encounterId, Integer sourceTypeId, String sctid, String pt) {
		log.debug("Input parameters -> encounterId {}, sourceTypeId {}, sctid {}, pt {} ", encounterId, sctid, sctid, pt);
		return healthConditionStorage.getHealthConditionIdByEncounterAndSnomedConcept(encounterId, sourceTypeId, sctid, pt).get(0);
	}

	@Override
	public Optional<Integer> getHealthConditionIdByDocumentIdAndSnomedConcept(Long documentId, Integer sourceTypeId, String sctid, String pt) {
		log.debug("Input parameters -> documentId {}, sourceTypeId {}, sctid {}, pt {} ", documentId, sctid, sctid, pt);
		return healthConditionStorage
			.getHealthConditionIdByDocumentIdAndSnomedConcept(documentId, sctid, pt)
			.stream()
			.findFirst();
	}

	@Override
	public Optional<Integer> getLatestHealthConditionByPatientIdAndInstitutionIdAndSnomedConcept(Integer institutionId, Integer patientId, String sctid, String pt) {

		/**
		 * Similar to
		 * HCEHealthConditionsServiceImpl#getActiveProblems
		 */
		List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getSummaryProblems(patientId);
		return resultQuery
		.stream()
		.map(HCEHealthConditionBo::new)
		.filter(healthCondition -> healthCondition.isActiveProblem() || healthCondition.isChronic())
		.filter(problem -> Objects.equals(problem.getSnomedSctid(), sctid) && Objects.equals(problem.getSnomedPt(), pt))
		.sorted(
			Comparator.comparing(
				HCEHealthConditionBo::getStartDate,
				Comparator.nullsFirst(Comparator.naturalOrder())
			).reversed()
		)
		.findFirst()
		.map(HCEHealthConditionBo::getId);
	}
}
