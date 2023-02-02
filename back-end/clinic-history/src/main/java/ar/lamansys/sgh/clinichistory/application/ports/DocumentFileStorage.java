package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.document.DocumentFileBo;

import java.util.List;
import java.util.Optional;

public interface DocumentFileStorage {

    Optional<DocumentFileBo> findById(Long id);

	List<Long> getIdsByDocumentsIds(List<Long> ids);
}
