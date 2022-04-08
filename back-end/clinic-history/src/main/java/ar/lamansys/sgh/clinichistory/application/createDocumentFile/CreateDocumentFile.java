package ar.lamansys.sgh.clinichistory.application.createDocumentFile;


import ar.lamansys.sgh.clinichistory.domain.document.event.OnGenerateDocumentEvent;


public interface CreateDocumentFile {

    void execute(OnGenerateDocumentEvent event);
	void executeSync(OnGenerateDocumentEvent event);
}
