package net.pladema.electronicjointsignature.rejectsignature.application;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EElectronicSignatureStatus;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.electronicjointsignature.rejectsignature.application.exception.RejectDocumentElectronicJointSignatureException;
import net.pladema.electronicjointsignature.rejectsignature.application.port.RejectDocumentElectronicJointSignaturePort;

import net.pladema.electronicjointsignature.rejectsignature.domain.RejectDocumentElectronicJointSignatureBo;

import net.pladema.electronicjointsignature.rejectsignature.domain.RejectElectronicJointSignatureDocumentInvolvedProfessionalBo;
import net.pladema.electronicjointsignature.rejectsignature.domain.exception.ERejectDocumentElectronicJointSignatureException;

import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class RejectDocumentElectronicJointSignature {

	private HealthcareProfessionalExternalService healthcareProfessionalExternalService;

	private RejectDocumentElectronicJointSignaturePort rejectDocumentElectronicJointSignaturePort;

	@Transactional
	public Integer run(RejectDocumentElectronicJointSignatureBo rejectData) {
		log.debug("Input parameters -> rejectData {}", rejectData);
		setHealthcareProfessionalId(rejectData);
		Integer result = processAndSaveRejectData(rejectData);
		log.debug("Output -> {}", result);
		return result;
	}

	private void setHealthcareProfessionalId(RejectDocumentElectronicJointSignatureBo rejectData) {
		Integer healthcareProfessionalId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		rejectData.setHealthcareProfessionalId(healthcareProfessionalId);
	}

	private Integer processAndSaveRejectData(RejectDocumentElectronicJointSignatureBo rejectData) {
		rejectData.getDocumentIds().forEach(documentId -> saveRejectData(rejectData, documentId));
		return 1;
	}

	private void saveRejectData(RejectDocumentElectronicJointSignatureBo rejectData, Long documentId) {
		Integer documentInvolvedProfessionalId = fetchDocumentInvolvedProfessionalId(documentId, rejectData.getHealthcareProfessionalId());
		rejectDocumentElectronicJointSignaturePort.saveRejectData(documentInvolvedProfessionalId, rejectData);
	}

	private Integer fetchDocumentInvolvedProfessionalId(Long documentId, Integer healthcareProfessionalId) {
		Optional<RejectElectronicJointSignatureDocumentInvolvedProfessionalBo> documentInvolvedProfessional = rejectDocumentElectronicJointSignaturePort.getDocumentInvolvedProfessionalByProfessionalAndDocumentId(documentId, healthcareProfessionalId);
		if (documentInvolvedProfessional.isEmpty())
			throw new RejectDocumentElectronicJointSignatureException(ERejectDocumentElectronicJointSignatureException.HEALTHCARE_PROFESSIONAL_NOT_LINKED_TO_DOCUMENT, "Un profesional no puede rechazar la firma de un documento al cual no se encuentra asociado");
		if (!statusIsPending(documentInvolvedProfessional.get()))
			throw new RejectDocumentElectronicJointSignatureException(ERejectDocumentElectronicJointSignatureException.ALREADY_SIGNED_OR_REJECTED, "No puede rechazarse la firma de un documento que ya ha sido firmado, rechazado, o expirado");
		return documentInvolvedProfessional.get().getDocumentInvolvedProfessionalId();
	}

	private boolean statusIsPending(RejectElectronicJointSignatureDocumentInvolvedProfessionalBo documentInvolvedProfessional) {
		return documentInvolvedProfessional.getStatusId().equals(EElectronicSignatureStatus.PENDING.getId());
	}

}
