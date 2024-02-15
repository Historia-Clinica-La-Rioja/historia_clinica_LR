package ar.lamansys.sgh.clinichistory.domain.ips.services;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoadProsthesis {

	public static final String OUTPUT = "Output -> {}";

	private final DocumentService documentService;


	public String run(Long documentId, String description) {
		log.debug("Input parameters -> documentId {}, description {}", documentId, description);
		String result = description != null ? documentService.createDocumentProsthesis(documentId, description).getDescription() : null;
		log.debug(OUTPUT, result);
		return result;
	}

}

