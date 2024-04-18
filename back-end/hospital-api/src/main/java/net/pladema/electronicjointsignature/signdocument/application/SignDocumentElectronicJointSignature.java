package net.pladema.electronicjointsignature.signdocument.application;

import ar.lamansys.sgh.clinichistory.application.rebuildFile.RebuildFile;
import ar.lamansys.sgh.clinichistory.domain.document.enums.EElectronicSignatureStatus;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.electronicjointsignature.signdocument.application.exception.SignDocumentElectronicJointSignatureException;
import net.pladema.electronicjointsignature.signdocument.application.port.SignDocumentElectronicJointSignaturePort;

import net.pladema.electronicjointsignature.signdocument.domain.exception.ESignDocumentElectronicJointSignatureException;

import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class SignDocumentElectronicJointSignature {

	private HealthcareProfessionalExternalService healthcareProfessionalExternalService;

	private SignDocumentElectronicJointSignaturePort signDocumentElectronicJointSignaturePort;

	private RebuildFile rebuildFile;

	@Transactional
	public Integer run(List<Long> documentIds) {
		log.debug("Input parameters -> documentIds {}", documentIds);
		Integer healthcareProfessionalId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		fetchAndValidateSignatureStatus(healthcareProfessionalId, documentIds);
		signDocumentElectronicJointSignaturePort.changeDocumentSignatureStatusToSigned(healthcareProfessionalId, documentIds);
		documentIds.forEach(documentId -> rebuildFile.run(documentId));
		Integer result = 1;
		log.debug("Output -> {}", result);
		return result;
	}

	private void fetchAndValidateSignatureStatus(Integer healthcareProfessionalId, List<Long> documentIds) {
		List<Short> documentSignatureStatusIds = signDocumentElectronicJointSignaturePort.fetchDocumentsSignatureStatus(healthcareProfessionalId, documentIds);
		if (documentSignatureStatusIds.size() != documentIds.size())
			throw new SignDocumentElectronicJointSignatureException("El profesional no se encuentra vinculado a ese documento", ESignDocumentElectronicJointSignatureException.NOT_LINKED_WITH_DOCUMENT);
		documentSignatureStatusIds.forEach(this::validateSignatureStatus);
	}

	private void validateSignatureStatus(Short documentSignatureStatusId) {
		if (!documentSignatureStatusId.equals(EElectronicSignatureStatus.PENDING.getId()))
			throw new SignDocumentElectronicJointSignatureException("No puede firmarse un documento que ya ha sido firmado, rechazado, o expirado", ESignDocumentElectronicJointSignatureException.CANNOT_BE_SIGNED);
	}

}
