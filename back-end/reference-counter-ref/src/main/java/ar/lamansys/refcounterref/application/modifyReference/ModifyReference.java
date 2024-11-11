package ar.lamansys.refcounterref.application.modifyReference;

import ar.lamansys.refcounterref.application.modifyReference.exceptions.ModifyReferenceException;
import ar.lamansys.refcounterref.application.modifyReference.exceptions.ModifyReferenceExceptionEnum;
import ar.lamansys.refcounterref.application.port.ReferenceHealthConditionStorage;
import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.domain.clinicalspecialty.ClinicalSpecialtyBo;
import ar.lamansys.refcounterref.domain.enums.EReferenceRegulationState;
import ar.lamansys.refcounterref.domain.enums.EReferenceStatus;
import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceDataBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceStudyBo;
import ar.lamansys.refcounterref.domain.referenceproblem.ReferenceProblemBo;
import ar.lamansys.refcounterref.infraestructure.output.repository.reference.Reference;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedServiceRequestPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModifyReference {

	private final ReferenceStorage referenceStorage;
	private final SharedServiceRequestPort sharedServiceRequestPort;
	private final ReferenceHealthConditionStorage referenceHealthConditionStorage;
	private final SharedStaffPort sharedStaffPort;

	@Transactional
	public void run(Integer userId, Integer oldReferenceId, ReferenceBo referenceBo){
		log.debug("Input parameters -> userId {}, oldReferenceId {}, referenceBo{}", userId, oldReferenceId, referenceBo);
		CompleteReferenceBo completeReference = buildCompleteReference(userId, oldReferenceId, referenceBo);
		cancelServiceRequest(oldReferenceId);
		referenceStorage.deleteAndUpdateStatus(oldReferenceId, EReferenceStatus.MODIFIED.getId());
		referenceStorage.save(Collections.singletonList(completeReference));
		log.debug("reference successfully modified");
	}

	private CompleteReferenceBo buildCompleteReference(Integer userId, Integer oldReferenceId, ReferenceBo referenceBo){
		CompleteReferenceBo result = new CompleteReferenceBo();

		ReferenceDataBo referenceData = referenceStorage.getReferenceData(oldReferenceId).orElse(null);
		if (referenceData == null)
			throw new ModifyReferenceException(ModifyReferenceExceptionEnum.INVALID_REFERENCE_ID, String.format("La referencia con id %s no existe", oldReferenceId));

		Integer sourceTypeId = referenceStorage.getReferenceEncounterTypeId(oldReferenceId).orElse(null);

		List<ReferenceProblemBo> referenceProblems = referenceHealthConditionStorage.getReferenceProblems(oldReferenceId);

		Optional<ReferenceStudyBo> referenceStudy = referenceStorage.getReferenceStudy(oldReferenceId);

		Integer doctorId = sharedStaffPort.getProfessionalId(UserInfo.getCurrentAuditor());


		assertContextValid(userId, referenceData);
		/* Modifiable fields */
		result.setNote(referenceBo.getNote());
		result.setPriority(referenceBo.getPriority());
		result.setDestinationInstitutionId(referenceBo.getDestinationInstitutionId());
		result.setFileIds(referenceBo.getFileIds());
		/* Unmodifiable fields */
		result.setConsultation(referenceData.getConsultation());
		result.setEncounterId(referenceData.getEncounterId());
		result.setSourceTypeId(sourceTypeId);
		result.setInstitutionId(referenceData.getInstitutionOrigin().getId());
		result.setProblems(referenceProblems);
		result.setCareLineId(referenceData.getCareLine().getId());
		result.setClinicalSpecialtyIds(referenceData.getDestinationClinicalSpecialties().stream().map(ClinicalSpecialtyBo::getId).collect(Collectors.toList()));
		result.setPhoneNumber(referenceData.getPhoneNumber());
		result.setPhonePrefix(referenceData.getPhonePrefix());
		result.setDoctorId(doctorId);
		result.setPatientId(referenceData.getPatientId());
		result.setPatientMedicalCoverageId(referenceData.getPatientMedicalCoverageId());
		result.setStudy(referenceStudy.orElse(null));
		result.setRegulationState(referenceData.getRegulationState());
		result.setAdministrativeState(referenceData.getAdministrativeState());

		return result;
	}

	private void assertContextValid(Integer userId, ReferenceDataBo referenceData){
		if (!referenceData.getCreatedBy().equals(userId))
			throw new ModifyReferenceException(ModifyReferenceExceptionEnum.INVALID_USER_ID, "Solo puede modificar la referencia el usuario que la creó");

		if (!referenceData.getRegulationState().equals(EReferenceRegulationState.SUGGESTED_REVISION))
			throw new ModifyReferenceException(ModifyReferenceExceptionEnum.INVALID_REFERENCE_REGULATION_STATE, "Para modificar la referencia se debe haber sugerido una revision previamente");

		if (!referenceData.getStatus().equals(EReferenceStatus.ACTIVE))
			throw new ModifyReferenceException(ModifyReferenceExceptionEnum.INVALID_REFERENCE_STATUS, "La referencia ya fue modificada o cancelada previamente");

		if(referenceData.getPriority() == null)
			throw new ModifyReferenceException(ModifyReferenceExceptionEnum.PRIORITY_REQUIRED, "Se debe indicar un nivel de prioridad");
		
	}

	private void cancelServiceRequest(Integer referenceId){
		Optional<Integer> serviceRequestId = referenceStorage.findById(referenceId).map(Reference::getServiceRequestId);
		serviceRequestId.ifPresent(sharedServiceRequestPort::cancelServiceRequest);
	}

}
