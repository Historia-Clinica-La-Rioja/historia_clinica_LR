package ar.lamansys.sgh.clinichistory.domain.document.event;

import java.util.Optional;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFile;

public interface GenerateFilePort {

    Optional<DocumentFile> save(OnGenerateDocumentEvent event);

}
