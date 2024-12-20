package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.document;

import java.util.List;

import ar.lamansys.sgh.clinichistory.application.signDocumentFile.SignDocumentFile;
import ar.lamansys.sgh.clinichistory.domain.document.DigitalSignatureDocumentBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.document.dto.DigitalSignatureDocumentDto;

import ar.lamansys.sgh.shared.infrastructure.input.service.datastructures.PageDto;
import io.jsonwebtoken.lang.Collections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.clinichistory.application.getpendingdocumentsbyprofessional.GetDocumentsByProfessional;
import ar.lamansys.sgh.shared.infrastructure.input.service.DocumentTypeDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SourceTypeDto;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RequestMapping("/institutions/{institutionId}/signature/documents")
@RestController
public class DocumentFileDigitalSignatureController {

	private final SignDocumentFile signDocumentFile;

	private final GetDocumentsByProfessional getDocumentsByProfessional;

	@PutMapping()
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<String> sign(@PathVariable(name = "institutionId") Integer institutionId, @RequestBody List<Long> documentIds) {
		log.debug("Input parameters -> documentIds {}", documentIds);
		String result = signDocumentFile.run(institutionId, documentIds, UserInfo.getCurrentAuditor());
		log.debug("Output result -> {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping()
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public PageDto<DigitalSignatureDocumentDto> getPendingDocumentsByUser(@PathVariable(name = "institutionId") Integer institutionId,
																		  @PageableDefault(size = 5) @SortDefault.SortDefaults({@SortDefault(sort = "creationable.createdOn", direction = Sort.Direction.DESC)}) Pageable pageable) {
		log.debug("Input parameters -> institutionId {}, pageable {}", institutionId, pageable);
		Page<DigitalSignatureDocumentDto> result = getDocumentsByProfessional.run(UserInfo.getCurrentAuditor(), institutionId, pageable).map(this::mapToDto);
		log.debug("Output result -> {}", result);
		return PageDto.fromPage(result);
	}

	private DigitalSignatureDocumentDto mapToDto(DigitalSignatureDocumentBo bo){
		return DigitalSignatureDocumentDto.builder()
				.documentId(bo.getDocumentId())
				.sourceTypeDto(new SourceTypeDto(bo.getSourceTypeBo().getId(), bo.getSourceTypeBo().getDescription()))
				.createdOn(bo.getCreatedOn())
				.patientFullName(bo.getPatientFullName())
				.professionalFullName(bo.getProfessionalFullName())
				.snomedConcepts(Collections.arrayToList(bo.getSnomedConceptBo().toArray()))
				.documentTypeDto(new DocumentTypeDto(bo.getDocumentTypeBo().getId().intValue(), bo.getDocumentTypeBo().getDescription()))
				.patientFullName(bo.getPatientFullName())
				.build();
	}
}
