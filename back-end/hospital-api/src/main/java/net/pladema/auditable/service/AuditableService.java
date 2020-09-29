package net.pladema.auditable.service;

import java.util.Optional;

import net.pladema.clinichistory.documents.events.OnGenerateDocumentEvent;
import net.pladema.clinichistory.documents.repository.entity.DocumentFile;

public interface AuditableService {

    Optional<DocumentFile> save(OnGenerateDocumentEvent event);

}
