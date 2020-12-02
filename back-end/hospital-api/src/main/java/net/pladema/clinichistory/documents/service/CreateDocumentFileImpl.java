package net.pladema.clinichistory.documents.service;

import net.pladema.auditable.service.AuditableService;
import net.pladema.clinichistory.documents.events.OnGenerateDocumentEvent;
import net.pladema.clinichistory.documents.repository.DocumentFileRepository;
import net.pladema.clinichistory.documents.repository.entity.DocumentFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CreateDocumentFileImpl implements CreateDocumentFile {

    private static final Logger LOG = LoggerFactory.getLogger(CreateDocumentFileImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentFileRepository documentFileRepository;

    private final AuditableService auditableService;

    public CreateDocumentFileImpl(DocumentFileRepository documentFileRepository,
                                  AuditableService auditableService) {
        this.documentFileRepository = documentFileRepository;
        this.auditableService = auditableService;

    }

    @Async
    @EventListener
    @Override
    public void execute(OnGenerateDocumentEvent event) throws IOException {
        LOG.debug("Input parameters -> onGenerateDocumentEvent {}", event);
        if(event.getDocument().isConfirmed()) {
            try {
                DocumentFile file = auditableService.save(event).orElseThrow( () -> new IOException());
                documentFileRepository.save(file);
            } catch (IOException ex) {
                throw new IOException(ex);
            }
        }
    }




}
