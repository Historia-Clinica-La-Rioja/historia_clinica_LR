package ar.lamansys.sgh.clinichistory.application.rebuildFile;

import ar.lamansys.sgh.clinichistory.application.document.FetchSpecificDocument;
import ar.lamansys.sgh.clinichistory.application.rebuildFile.exceptions.RebuildFileEnumException;
import ar.lamansys.sgh.clinichistory.application.rebuildFile.exceptions.RebuildFileException;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileHistoryRepository;

import ar.lamansys.sgh.shared.infrastructure.output.entities.ESignatureStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFileHistory;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.domain.document.event.GenerateFilePort;
import ar.lamansys.sgh.clinichistory.domain.document.event.OnGenerateDocumentEvent;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFile;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RebuildFile {

    private final FetchSpecificDocument fetchSpecificDocument;
	private final DocumentFileRepository documentFileRepository;
	private final GenerateFilePort generateFilePort;
	private final DocumentFileHistoryRepository documentFileHistoryRepository;

	public void run(Long id) {
        log.debug("FetchDocumentFile with id {}", id);
		assertSignatureStatus(id);
		fetchSpecificDocument.run(id)
			.ifPresent(documentBo -> {
				OnGenerateDocumentEvent event = new OnGenerateDocumentEvent(documentBo);
				if(event.getDocumentBo().isConfirmed())
					generateFilePort
							.save(event)
							.ifPresent(documentFile -> update(documentFile, id));
			});
	}

	private void assertSignatureStatus(Long id){
		documentFileRepository.findById(id)
				.ifPresent(file -> {
					if(!file.getSignatureStatusId().equals(ESignatureStatus.PENDING.getId()) && !file.getSignatureStatusId().equals(ESignatureStatus.CANNOT_BE_SIGNED.getId()))
						throw new RebuildFileException(RebuildFileEnumException.DOCUMENT_SIGNED, "No es posible regenerar debido a que el documento tiene una firma digital vigente.");
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
					documentFile.setSignatureStatusId(oldDocument.getSignatureStatusId());
					return oldDocument;
				})
				.ifPresent(documentFileRepository::save);
	}
}
