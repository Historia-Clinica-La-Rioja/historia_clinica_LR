package ar.lamansys.sgh.clinichistory.application.createDocumentFile;

import ar.lamansys.sgh.clinichistory.domain.document.event.GenerateFilePort;
import ar.lamansys.sgh.clinichistory.domain.document.event.OnGenerateDocumentEvent;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileRepository;
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

    private final GenerateFilePort generateFilePort;

    public CreateDocumentFileImpl(DocumentFileRepository documentFileRepository,
                                  GenerateFilePort generateFilePort) {
        this.documentFileRepository = documentFileRepository;
        this.generateFilePort = generateFilePort;

    }

    @Async
    @EventListener
    @Override
    public void execute(OnGenerateDocumentEvent event) {
        LOG.debug("Input parameters -> onGenerateDocumentEvent {}", event);
        if(event.getDocumentBo().isConfirmed())
            generateFilePort
                    .save(event)
                    .ifPresent(documentFileRepository::save);
    }

	@Override
	public void executeSync(OnGenerateDocumentEvent event){
		this.execute(event);
	}




}
