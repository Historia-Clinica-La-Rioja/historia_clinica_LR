package net.pladema.emergencycare.application.getevolutionnotebydocumentid;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.getevolutionnotebydocumentid.exceptions.GetEvolutionNoteByDocumentIdException;
import net.pladema.emergencycare.application.getevolutionnotebydocumentid.exceptions.GetEvolutionNoteByDocumentIdExceptionEnum;
import net.pladema.emergencycare.service.EmergencyCareEvolutionNoteDocumentService;


import net.pladema.emergencycare.service.domain.EmergencyCareEvolutionNoteDocumentBo;

import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class GetEvolutionNoteByDocumentId {

	private final EmergencyCareEvolutionNoteDocumentService emergencyCareEvolutionNoteDocumentService;

	public EmergencyCareEvolutionNoteDocumentBo run (Long documentId) {
		log.debug("Input parameters -> documentId {}", documentId);
		EmergencyCareEvolutionNoteDocumentBo result = emergencyCareEvolutionNoteDocumentService.getByDocumentId(documentId)
				.orElseThrow(() -> new GetEvolutionNoteByDocumentIdException(GetEvolutionNoteByDocumentIdExceptionEnum.INVALID_DOCUMENT_ID, ""));
		if (result.getDocumentType() != EDocumentType.EMERGENCY_CARE_EVOLUTION.getId())
			throw new GetEvolutionNoteByDocumentIdException(GetEvolutionNoteByDocumentIdExceptionEnum.INVALID_DOCUMENT_TYPE, "");
		log.debug("Output -> result {}", result);
		return result;
	}

}
