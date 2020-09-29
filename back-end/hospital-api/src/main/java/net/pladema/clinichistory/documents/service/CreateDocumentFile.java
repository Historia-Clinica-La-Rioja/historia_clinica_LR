package net.pladema.clinichistory.documents.service;

import net.pladema.clinichistory.documents.events.OnGenerateDocumentEvent;
import net.pladema.sgx.pdf.PDFDocumentException;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;

public interface CreateDocumentFile {

    @Async
    @EventListener
    void execute(OnGenerateDocumentEvent event) throws IOException, PDFDocumentException;
}
