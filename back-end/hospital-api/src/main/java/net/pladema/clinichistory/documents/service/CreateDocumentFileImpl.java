package net.pladema.clinichistory.documents.service;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileRepository;
import net.pladema.auditable.service.AuditableService;
import net.pladema.clinichistory.documents.events.OnGenerateDocumentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


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
    public void execute(OnGenerateDocumentEvent event) {
        LOG.debug("Input parameters -> onGenerateDocumentEvent {}", event);
        if(event.getDocumentBo().isConfirmed())
            auditableService
                    .save(event)
                    .ifPresent(documentFileRepository::save);
    }




}
