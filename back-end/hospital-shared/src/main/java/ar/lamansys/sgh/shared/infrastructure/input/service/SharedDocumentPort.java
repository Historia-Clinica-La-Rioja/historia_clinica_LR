package ar.lamansys.sgh.shared.infrastructure.input.service;

public interface SharedDocumentPort {

	void deleteDocument(Long documentId, String reason);

	void updateDocumentModificationReason(Long documentId, String reason);

	DocumentReduceInfoDto getDocument(Long documentId);

}
