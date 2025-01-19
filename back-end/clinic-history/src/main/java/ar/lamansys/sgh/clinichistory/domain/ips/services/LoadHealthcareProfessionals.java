package ar.lamansys.sgh.clinichistory.domain.ips.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentHealthcareProfessionalBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentHealthcareProfessional;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.EProfessionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoadHealthcareProfessionals {

	public static final String OUTPUT = "Output -> {}";

	private final DocumentService documentService;

	public List<DocumentHealthcareProfessionalBo> run(Long documentId, List<DocumentHealthcareProfessionalBo> professionals) {
		log.debug("Input parameters -> documentId {}, professionals {}", documentId, professionals);
		List<DocumentHealthcareProfessionalBo> result = professionals.stream()
				.map(professional -> documentService.createDocumentHealthcareProfessional(documentId, professional))
				.map(this::mapToBo)
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}

	private DocumentHealthcareProfessionalBo mapToBo(DocumentHealthcareProfessional entity) {
		return new DocumentHealthcareProfessionalBo(entity.getId(), entity.getHealthcareProfessionalId(), EProfessionType.map(entity.getProfessionTypeId()), entity.getComments());
	}

}

