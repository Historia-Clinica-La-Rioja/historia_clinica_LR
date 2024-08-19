package ar.lamansys.sgh.clinichistory.infrastructure.input.service;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.ports.DocumentFileStorage;
import ar.lamansys.sgh.clinichistory.application.rebuildFile.RebuildFile;
import ar.lamansys.sgh.clinichistory.application.updatesignaturestatus.UpdateSignatureStatus;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentFileBo;
import ar.lamansys.sgh.clinichistory.domain.document.digitalsignature.DigitalSignatureCallbackBo;
import ar.lamansys.sgh.clinichistory.domain.document.digitalsignature.DigitalSignatureStatusBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgh.shared.infrastructure.input.service.DocumentReduceInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.digitalsignature.DigitalSignatureCallbackRequestDto;
import ar.lamansys.sgh.shared.infrastructure.output.entities.ESignatureStatus;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SharedDocumentPortImpl implements SharedDocumentPort {

	private final DocumentService documentService;
	private final RebuildFile rebuildFile;
	private final DocumentFileStorage documentFileStorage;
	private final UpdateSignatureStatus updateSignatureStatus;

	@Override
	public void deleteDocument(Long documentId, String newDocumentStatus) {
		log.debug("Input parameter documentId {}, newDocumentStatus {}", documentId, newDocumentStatus);
		documentService.deleteById(documentId, newDocumentStatus);
		documentFileStorage.deleteById(documentId);
	}

	@Override
	public void updateDocumentModificationReason(Long documentId, String reason) {
		log.debug("Input parameter documentId {}, reason {}", documentId, reason);
		documentService.updateDocumentModificationReason(documentId, reason);
	}
	
	@Override
	public DocumentReduceInfoDto getDocument(Long documentId) {
		log.debug("Input parameter documentId {}", documentId);
		Document document = documentService.findById(documentId)
				.orElseThrow(() -> new NotFoundException("document-not-exists", String.format("No existe el documento con id %s", documentId)));
		DocumentReduceInfoDto result = new DocumentReduceInfoDto();
		result.setSourceId(document.getSourceId());
		result.setCreatedBy(document.getCreatedBy());
		result.setCreatedOn(document.getCreatedOn());
		result.setTypeId(document.getTypeId());
		result.setIsConfirmed(document.isConfirmed());
		if (document.isConfirmed()) {
			DocumentFileBo file = documentFileStorage.findById(documentId)
					.orElseThrow(() -> new NotFoundException("file-not-exists", String.format("No existe el documento con id %s", documentId)));
			result.setSignatureStatus(ESignatureStatus.map(file.getSignatureStatusId()));
		}
		return result;
	}

	@Override
	public void rebuildFilesFromPatient(Integer patient) {
		List<Long> documentsIds = documentService.getDocumentsIdsFromPatient(patient);
		List<Long> documentFilesIds = documentFileStorage.getIdsByDocumentsIds(documentsIds);
		documentFilesIds.forEach(this::rebuildFile);
	}

	@Override
	public void rebuildFile(Long documentId) {
		log.debug("Input parameter documentId {}", documentId);
		rebuildFile.run(documentId);
	}

	@Override
	public void updateSignatureStatus(DigitalSignatureCallbackRequestDto documentSignature) {
		log.debug("Input parameters -> documentSignature {}",documentSignature);
		updateSignatureStatus.run(mapToDigitalSignatureBo(documentSignature));
	}

	@Override
	public String getChecksumById(Long documentId) {
		log.debug("Input parameters -> documentId {}",documentId);
		return documentFileStorage.findById(documentId).map(DocumentFileBo::getChecksum)
				.orElseThrow(()-> new NotFoundException("document-not-exists", String.format("No existe el documento con id %s", documentId)));
	}

	@Override
	public void updateDigitalSignatureHash(Long documentId, String hash) {
		log.debug("Input parameters -> documentId {}, hash {}", documentId, hash);
		documentFileStorage.updateDigitalSignatureHash(documentId, hash);
	}

	@Override
	public String getDigitalSignatureHashById(Long documentId) {
		log.debug("Input parameters -> documentId {}",documentId);
		return documentFileStorage.findById(documentId).map(DocumentFileBo::getDigitalSignatureHash)
				.orElseThrow(()-> new NotFoundException("document-not-exists", String.format("No existe el documento con id %s", documentId)));
	}

	@Override
	public Optional<Long> getInitialDocumentId(Long documentId) {
		log.debug("Input parameters -> documentId {}",documentId);
		return documentService.findById(documentId).map(Document::getInitialDocumentId);
	}

	@Override
	public void updateInitialDocumentId(Long documentId, Long initialDocumentId) {
		log.debug("Input parameters -> documentId {}, initialDocumentId {}", documentId, initialDocumentId);
		documentService.findById(documentId).ifPresent(document -> {
			document.setInitialDocumentId(initialDocumentId);
			documentService.save(document);
		});
	}

	private DigitalSignatureCallbackBo mapToDigitalSignatureBo(DigitalSignatureCallbackRequestDto dto){
		return new DigitalSignatureCallbackBo(
				new DigitalSignatureStatusBo(dto.getStatus().getSuccess(), dto.getStatus().getMsg()),
				dto.getDocumentId(),
				dto.getPersonId(),
				dto.getHash(),
				dto.getSignatureHash());
	}

}
