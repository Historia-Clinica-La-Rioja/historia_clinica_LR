package ar.lamansys.refcounterref.infraestructure.output.repository.study;

import ar.lamansys.refcounterref.application.port.ReferenceHealthConditionStorage;
import ar.lamansys.refcounterref.application.port.ReferenceStudyStorage;

import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceStudyBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedServiceRequestPort;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CompleteReferenceStudyDto;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SaveReferenceStudyStorageImpl implements ReferenceStudyStorage {

	private final SharedServiceRequestPort sharedServiceRequestPort;

	private final ReferenceHealthConditionStorage referenceHealthConditionStorage;

	@Override
	public Integer run(CompleteReferenceBo completeReferenceBo) {
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

}
