package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentAuthorFinder {

    private final SharedStaffPort sharedStaffPort;

	private final DocumentRepository documentRepository;

    public DocumentAuthorFinder(SharedStaffPort sharedStaffPort, DocumentRepository documentRepository) {
		this.sharedStaffPort = sharedStaffPort;
		this.documentRepository = documentRepository;
	}
    public ProfessionalCompleteDto getAuthor(Long documentId) {
		return documentRepository.findById(documentId)
				.map(document -> sharedStaffPort.getProfessionalComplete(document.getCreatedBy()))
				.orElse(null);
	}
}
