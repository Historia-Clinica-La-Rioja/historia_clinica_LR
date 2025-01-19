package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgx.shared.files.FixFileService;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.ports.DigitalSignatureStorage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFile;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFileHistory;
import ar.lamansys.sgh.shared.infrastructure.DigitalSignatureDataDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDigitalSignaturePort;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DigitalSignatureStorageImpl implements DigitalSignatureStorage {

	private final SharedDigitalSignaturePort sharedDigitalSignaturePort;

	private final DocumentFileRepository documentFileRepository;

	private final DocumentFileHistoryRepository documentFileHistoryRepository;

	private final FixFileService fixFileService;

	@Override
	public String generateDigitalSigningLink(DigitalSignatureDataDto digitalSignatureData) {
		log.debug("Input parameters -> digitalSignatureData {}", digitalSignatureData);
		return sharedDigitalSignaturePort.generateDigitalSigningLink(digitalSignatureData);
	}

	@Override
	public void updateFile(DocumentFile documentFile) {
		documentFileRepository.save(documentFile);
		documentFileHistoryRepository.save(new DocumentFileHistory(documentFile));
		fixFileService.fixMetadata(documentFile.getId());
	}

	@Override
	public DocumentFile getFile(Long id) {
		return documentFileRepository.findById(id)
				.orElseThrow(()-> new NotFoundException("document-file-not-found", "Document file not found"));
	}
}
