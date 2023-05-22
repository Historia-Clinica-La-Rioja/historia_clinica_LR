package ar.lamansys.sgh.shared.infrastructure.input.service;

import org.springframework.core.io.InputStreamResource;

public interface SharedDocumentPort {

	void deleteDocument(Long documentId, String reason);

	void updateDocumentModificationReason(Long documentId, String reason);

	DocumentReduceInfoDto getDocument(Long documentId);

	void rebuildFilesFromPatient(Integer patient);

	void rebuildFile(Long documentId);
}
