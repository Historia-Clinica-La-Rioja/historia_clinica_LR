package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.digitalsignature.DigitalSignatureCallbackRequestDto;

import java.util.Optional;

public interface SharedDocumentPort {

	void deleteDocument(Long documentId, String reason);

	void updateDocumentModificationReason(Long documentId, String reason);

	DocumentReduceInfoDto getDocument(Long documentId);

	void rebuildFilesFromPatient(Integer patient);

	void rebuildFile(Long documentId);

	void updateSignatureStatus(DigitalSignatureCallbackRequestDto documentSignature);

	String getChecksumById(Long documentId);

	void updateDigitalSignatureHash(Long documentId, String hash);

	String getDigitalSignatureHashById(Long documentId);

	Optional<Long> getInitialDocumentId(Long documentId);

	void updateInitialDocumentId(Long documentId, Long initialDocumentId);
}
