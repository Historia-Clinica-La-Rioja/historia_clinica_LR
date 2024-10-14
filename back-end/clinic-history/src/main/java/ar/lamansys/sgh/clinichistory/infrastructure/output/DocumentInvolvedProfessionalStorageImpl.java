package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.fetchAllDocumentInfo.port.DocumentInvolvedProfessionalStorage;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EElectronicSignatureStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentInvolvedProfessionalRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class DocumentInvolvedProfessionalStorageImpl implements DocumentInvolvedProfessionalStorage {

	private DocumentInvolvedProfessionalRepository documentInvolvedProfessionalRepository;

	@Override
	public List<Integer> fetchSignerInvolvedProfessionalIdsByDocumentId(Long documentId) {
		return documentInvolvedProfessionalRepository.getDocumentInvolvedProfessionalIdsByDocumentAndSignatureStatus(documentId, EElectronicSignatureStatus.SIGNED.getId());
	}

	@Override
	public List<Integer> getDocumentInvolvedProfessionalPersonIdsByDocumentIdAndStatusId(Long documentId, Short id) {
		return documentInvolvedProfessionalRepository.getDocumentInvolvedProfessionalPersonIdsByDocumentIdAndStatusId(documentId, id);
	}

	@Override
	public Integer getDocumentInvolvedProfessionalAmountThatDidNotSignByDocumentId(Long documentId, Short pendingStatusId) {
		return documentInvolvedProfessionalRepository.getDocumentInvolvedProfessionalAmountThatDidNotSignByDocumentId(documentId, pendingStatusId).size();
	}

}
