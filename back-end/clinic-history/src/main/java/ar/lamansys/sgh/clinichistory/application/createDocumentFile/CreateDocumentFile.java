package ar.lamansys.sgh.clinichistory.application.createDocumentFile;


import ar.lamansys.sgh.clinichistory.domain.document.event.OnGenerateDocumentEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;


public interface CreateDocumentFile {

    @Async
    @EventListener
    void execute(OnGenerateDocumentEvent event);
}
