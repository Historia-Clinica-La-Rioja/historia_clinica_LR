package net.pladema.auditable.service;

import java.util.Optional;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFile;
import net.pladema.clinichistory.documents.events.OnGenerateDocumentEvent;

public interface AuditableService {

    Optional<DocumentFile> save(OnGenerateDocumentEvent event);

}
