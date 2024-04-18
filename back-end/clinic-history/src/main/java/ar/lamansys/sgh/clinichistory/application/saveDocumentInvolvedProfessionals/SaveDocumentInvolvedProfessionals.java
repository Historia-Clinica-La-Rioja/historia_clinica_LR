package ar.lamansys.sgh.clinichistory.application.saveDocumentInvolvedProfessionals;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EElectronicSignatureStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentInvolvedProfessionalRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentInvolvedProfessional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class SaveDocumentInvolvedProfessionals {

	private DocumentInvolvedProfessionalRepository documentInvolvedProfessionalRepository;

	public void run(Long documentId, List<Integer> healthcareProfessionalIds) {
		log.debug("Input parameters -> documentId {}, healthcareProfessionalId {}", documentId, healthcareProfessionalIds);
		if (healthcareProfessionalIds != null)
			saveDocumentInvolvedProfessionals(documentId, healthcareProfessionalIds);
	}

	private void saveDocumentInvolvedProfessionals(Long documentId, List<Integer> healthcareProfessionalIds) {
		List<DocumentInvolvedProfessional> entities = healthcareProfessionalIds.stream().map(healthcareProfessionalId -> parseDocumentInvolvedProfessional(documentId, healthcareProfessionalId)).collect(Collectors.toList());
		if (!entities.isEmpty())
			saveDocumentInvolvedProfessionals(healthcareProfessionalIds, entities);
	}

	private void saveDocumentInvolvedProfessionals(List<Integer> healthcareProfessionalIds, List<DocumentInvolvedProfessional> entities) {
		documentInvolvedProfessionalRepository.saveAll(entities);
		healthcareProfessionalIds.clear();
	}

	private DocumentInvolvedProfessional parseDocumentInvolvedProfessional(Long documentId, Integer healthcareProfessionalId) {
		DocumentInvolvedProfessional result = new DocumentInvolvedProfessional();
		result.setDocumentId(documentId);
		result.setHealthcareProfessionalId(healthcareProfessionalId);
		result.setStatusUpdateDate(LocalDate.now());
		result.setSignatureStatusId(EElectronicSignatureStatus.PENDING.getId());
		return result;
	}

}
