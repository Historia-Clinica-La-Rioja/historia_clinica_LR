package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.document.DocumentFileBo;

import java.util.Optional;

public interface DocumentFileStorage {

    Optional<DocumentFileBo> findById(Long id);
}
