package net.pladema.clinichistory.documents.service;

import net.pladema.clinichistory.documents.events.OnGenerateDocumentEvent;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;


public interface CreateDocumentFile {

    @Async
    @EventListener
    void execute(OnGenerateDocumentEvent event);
}
