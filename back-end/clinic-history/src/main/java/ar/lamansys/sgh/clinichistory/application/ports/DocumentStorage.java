package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.document.DocumentBo;
import java.util.Optional;

public interface DocumentStorage {

    Optional<DocumentBo> getDocumentBo(Long documentId);
}