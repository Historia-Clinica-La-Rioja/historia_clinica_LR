package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.document.DigitalSignatureDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentFileBo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DocumentFileStorage {

    Optional<DocumentFileBo> findById(Long id);

	List<Long> getIdsByDocumentsIds(List<Long> ids);

	Page<DigitalSignatureDocumentBo> findDocumentsByUserAndInstitution(Integer userId, Integer institutionId, Pageable pageable);

	boolean isDocumentBelongsToUser(Long documentId, Integer userId);

	void updateDigitalSignatureHash(Long documentId, String hash);

	void deleteById(Long id);

}
