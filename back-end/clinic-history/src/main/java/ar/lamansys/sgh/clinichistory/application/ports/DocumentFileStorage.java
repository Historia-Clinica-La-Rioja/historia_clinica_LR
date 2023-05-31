package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.document.DigitalSignatureDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentFileBo;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentFileSummaryBo;

import java.util.List;
import java.util.Optional;

public interface DocumentFileStorage {

    Optional<DocumentFileBo> findById(Long id);

	List<Long> getIdsByDocumentsIds(List<Long> ids);

	List<DigitalSignatureDocumentBo> findDocumentsByUserAndInstitution(Integer userId, Integer institutionId);

	boolean isDocumentBelongsToUser(Long documentId, Integer userId);

	void updateDigitalSignatureHash(Long documentId, String hash);

}
