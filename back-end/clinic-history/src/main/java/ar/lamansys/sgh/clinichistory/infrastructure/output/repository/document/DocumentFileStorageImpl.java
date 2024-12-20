package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.ports.DocumentFileStorage;
import ar.lamansys.sgh.clinichistory.domain.document.DigitalSignatureDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentFileBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFile;

@Slf4j
@Service
public class DocumentFileStorageImpl implements DocumentFileStorage {

    private final DocumentFileRepository documentRepository;

	private final SharedPersonPort personService;

    public DocumentFileStorageImpl(DocumentFileRepository documentRepository, SharedPersonPort personService) {
        this.documentRepository = documentRepository;
		this.personService = personService;
    }

    @Override
    public Optional<DocumentFileBo> findById(Long id) {
        return documentRepository.findById(id)
                .map(this::mapToDocumentFileBo);
    }

	@Override
	public List<Long> getIdsByDocumentsIds(List<Long> ids) {
		return documentRepository.findAllById(ids).stream().map(DocumentFile::getId).collect(Collectors.toList());
	}

	@Override
	public boolean isDocumentBelongsToUser(Long documentId, Integer userId){
		return documentRepository.findById(documentId)
				.map(document -> document.getCreatedBy().equals(userId))
				.orElse(false);
	}

	@Override
	public void updateDigitalSignatureHash(Long documentId, String hash) {
		log.debug("Input parameters -> documentId {}, hash {}", documentId, hash);
		documentRepository.updateDigitalSignatureHash(documentId, hash);
	}

	@Override
	public Page<DigitalSignatureDocumentBo> findDocumentsByUserAndInstitution(Integer userId, Integer institutionId, Pageable pageable) {
		log.debug("Input parameters -> userId {}, institutionId {}, pageable {}", userId, institutionId, pageable);
		Page<DigitalSignatureDocumentBo> result = documentRepository.findDocumentsByUserAndInstitution(userId, institutionId, pageable);
		result.forEach(this::setExtendedInformation);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public void deleteById(Long id){
		log.debug("Input parameters -> id {}", id);
		documentRepository.deleteById(id);
	}

	private void setExtendedInformation(DigitalSignatureDocumentBo bo) {
		bo.setSnomedConceptBo(documentRepository.findSnomedConceptsByDocumentId(bo.getDocumentId()));
		String doctor = personService.getCompletePersonNameById(bo.getProfessionalPersonId());
		String patient = personService.getCompletePersonNameById(bo.getPatientPersonId());
		bo.setPatientFullName(patient);
		bo.setProfessionalFullName(doctor);
	}

	private DocumentFileBo mapToDocumentFileBo(DocumentFile documentFile) {
        return new DocumentFileBo(
                documentFile.getId(),
                documentFile.getSourceId(),
                documentFile.getSourceTypeId(),
                documentFile.getTypeId(),
                documentFile.getFilepath(),
                documentFile.getFilename(),
                documentFile.getUuidfile(),
                documentFile.getChecksum(),
				documentFile.getSignatureStatusId(),
				documentFile.getDigitalSignatureHash());
    }
}
