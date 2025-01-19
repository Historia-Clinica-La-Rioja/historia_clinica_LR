package ar.lamansys.sgh.clinichistory.application.createDocumentFile;

import ar.lamansys.sgh.clinichistory.domain.document.event.GenerateFilePort;
import ar.lamansys.sgh.clinichistory.domain.document.event.OnGenerateDocumentEvent;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFile;

import ar.lamansys.sgh.shared.infrastructure.output.entities.ESignatureStatus;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@AllArgsConstructor
@Service
public class CreateDocumentFileImpl implements CreateDocumentFile {

    private static final Logger LOG = LoggerFactory.getLogger(CreateDocumentFileImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentFileRepository documentFileRepository;

    private final GenerateFilePort generateFilePort;

	private final FeatureFlagsService featureFlagsService;

    @Async
    @EventListener
    @Override
    public void execute(OnGenerateDocumentEvent event) {
        LOG.debug("Input parameters -> onGenerateDocumentEvent {}", event);
        if(event.getDocumentBo().isConfirmed())
            generateFilePort
                    .save(event)
                    .ifPresent(saveDocumentFileData());
    }

	private Consumer<DocumentFile> saveDocumentFileData() {
		return documentFile -> {
			boolean digitalSignatureFlagEnabled = featureFlagsService.isOn(AppFeature.HABILITAR_FIRMA_DIGITAL);
			if (digitalSignatureFlagEnabled || documentFileRepository.documentsWereAlreadySigned())
				documentFile.setSignatureStatusId(ESignatureStatus.PENDING.getId());
			else
				documentFile.setSignatureStatusId(ESignatureStatus.CANNOT_BE_SIGNED.getId());
			documentFileRepository.save(documentFile);
		};
	}

	@Override
	public void executeSync(OnGenerateDocumentEvent event){
		this.execute(event);
	}




}
