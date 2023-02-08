package ar.lamansys.sgh.clinichistory.application.rebuildFile;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileHistoryRepository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFileHistory;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.fetchAllDocumentInfo.FetchAllDocumentInfo;
import ar.lamansys.sgh.clinichistory.domain.document.event.GenerateFilePort;
import ar.lamansys.sgh.clinichistory.domain.document.event.OnGenerateDocumentEvent;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFile;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RebuildFile {

    private final FetchAllDocumentInfo fetchAllDocumentInfo;
	private final DocumentFileRepository documentFileRepository;
	private final GenerateFilePort generateFilePort;
	private final DocumentFileHistoryRepository documentFileHistoryRepository;

	public RebuildFile(FetchAllDocumentInfo fetchAllDocumentInfo,
					   DocumentFileRepository documentFileRepository,
					   GenerateFilePort generateFilePort, DocumentFileHistoryRepository documentFileHistoryRepository) {
		this.fetchAllDocumentInfo = fetchAllDocumentInfo;
		this.documentFileRepository = documentFileRepository;
		this.generateFilePort = generateFilePort;
		this.documentFileHistoryRepository = documentFileHistoryRepository;
	}

	public void run(Long id) {
        log.debug("FetchDocumentFile with id {}",id);
		fetchAllDocumentInfo.run(id)
			.ifPresent(documentBo -> {
				OnGenerateDocumentEvent event = new OnGenerateDocumentEvent(documentBo);
				if(event.getDocumentBo().isConfirmed())
					generateFilePort
							.save(event)
							.ifPresent(documentFile -> update(documentFile, id));
			});
	}

	private void update(DocumentFile documentFile, Long id) {
		documentFileRepository.findById(id)
				.map(oldDocument -> {
					documentFileHistoryRepository.save(new DocumentFileHistory(oldDocument));
					oldDocument.setFilename(documentFile.getFilename());
					oldDocument.setFilepath(documentFile.getFilepath());
					oldDocument.setChecksum(documentFile.getChecksum());
					oldDocument.setUuidfile(documentFile.getUuidfile());
					oldDocument.setTypeId(documentFile.getTypeId());
					oldDocument.setSourceId(documentFile.getSourceId());
					oldDocument.setSourceTypeId(documentFile.getSourceTypeId());
					return oldDocument;
				})
				.ifPresent(documentFileRepository::save);
	}
}
