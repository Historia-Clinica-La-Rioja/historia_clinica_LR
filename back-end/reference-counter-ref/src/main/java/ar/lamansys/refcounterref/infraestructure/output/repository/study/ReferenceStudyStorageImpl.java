package ar.lamansys.refcounterref.infraestructure.output.repository.study;

import ar.lamansys.refcounterref.application.port.ReferenceHealthConditionStorage;
import ar.lamansys.refcounterref.application.port.ReferenceStudyStorage;

import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceStudyBo;
import ar.lamansys.refcounterref.domain.snomed.SnomedBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedServiceRequestPort;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CompleteReferenceStudyDto;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ReferenceStudyStorageImpl implements ReferenceStudyStorage {

	private final SharedServiceRequestPort sharedServiceRequestPort;

	private final ReferenceHealthConditionStorage referenceHealthConditionStorage;

	@Override
	public Integer save(CompleteReferenceBo completeReferenceBo) {
		ReferenceStudyBo study = completeReferenceBo.getStudy();
		Integer healthConditionId = referenceHealthConditionStorage.fetchHealthConditionByEncounterAndSnomedData(completeReferenceBo.getEncounterId(), completeReferenceBo.getSourceTypeId(), study.getProblem().getSctid(), study.getProblem().getPt());
		var completeStudy = CompleteReferenceStudyDto.builder()
				.practice(new SharedSnomedDto(study.getPractice().getSctid(), study.getPractice().getPt()))
				.categoryId(completeReferenceBo.getStudy().getCategoryId())
				.patientId(completeReferenceBo.getPatientId())
				.patientMedicalCoverageId(completeReferenceBo.getPatientMedicalCoverageId())
				.institutionId(completeReferenceBo.getInstitutionId())
				.doctorId(completeReferenceBo.getDoctorId())
				.healthConditionId(healthConditionId)
				.encounterId(completeReferenceBo.getEncounterId())
				.sourceTypeId(completeReferenceBo.getSourceTypeId())
				.build();
		return sharedServiceRequestPort.create(completeStudy);
	}

	@Override
	public Map<Integer, Pair<SnomedBo, String>> getReferencesProcedures(Map<Integer, Integer> referencesStudiesIds) {
		var referencesProcedures = sharedServiceRequestPort.getProceduresByServiceRequestIds(new ArrayList<>(referencesStudiesIds.keySet()));
		Map<Integer, Pair<SnomedBo, String>> result = new HashMap<>();
		referencesProcedures.forEach(rf -> {
			var snomed = rf.getProcedure();
			result.put(referencesStudiesIds.get(rf.getServiceRequestId()), Pair.of(new SnomedBo(snomed.getId(), snomed.getSctid(), snomed.getPt()), rf.getCategory()));
		});
		return result;
	}


}
