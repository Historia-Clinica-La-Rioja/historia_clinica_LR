package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.document;

import ar.lamansys.sgh.clinichistory.application.fetchdocumentfile.FetchDocumentLazyFileById;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.mapper.DocumentMapper;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile.ClinicalSpecialtyFinder;
import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.clinichistory.application.fetchAllDocumentInfo.FetchAllDocumentInfo;
import ar.lamansys.sgh.clinichistory.application.fetchdocumentfile.FetchDocumentFileById;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.DocumentDto;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

@Slf4j
//@Api(value = "Documents", tags = {"Documents"})
@RestController
@RequestMapping("/institutions/{institutionId}/documents")
public class DocumentFileDownloadController {

	private final FetchDocumentLazyFileById fetchDocumentLazyFileById;

	private final FetchAllDocumentInfo fetchAllDocumentInfo;

	private final Function<Integer, ClinicalSpecialtyDto> clinicalSpecialtyDtoFunction;

	private final DocumentMapper documentMapper;

	public DocumentFileDownloadController(
			FetchDocumentLazyFileById fetchDocumentLazyFileById,
			FetchAllDocumentInfo fetchAllDocumentInfo,
			DocumentMapper documentMapper,
			ClinicalSpecialtyFinder clinicalSpecialtyFinder
	){
		this.fetchDocumentLazyFileById = fetchDocumentLazyFileById;
		this.fetchAllDocumentInfo = fetchAllDocumentInfo;
		this.documentMapper = documentMapper;
		this.clinicalSpecialtyDtoFunction = clinicalSpecialtyFinder::getClinicalSpecialty;
	}

    @GetMapping(value = "/{id}/downloadFile")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PRESCRIPTOR, TECNICO, INFORMADOR, PERSONAL_DE_IMAGENES, ABORDAJE_VIOLENCIAS, PERSONAL_DE_FARMACIA')")
	public ResponseEntity<Resource> downloadPdf(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "id") Long id)
	{

		return StoredFileResponse.sendStoredBlob(//DocumentService.downloadFile
				fetchDocumentLazyFileById.run(id)
		);
    }


	@GetMapping(value = "/{id}/info")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PRESCRIPTOR, ABORDAJE_VIOLENCIAS')")
	public ResponseEntity<DocumentDto> getInfo(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "id") Long id)
	{
		log.debug("Input parameters -> institutionId {}, documentId {}", institutionId, id);
		DocumentDto result = fetchAllDocumentInfo.run(id).map(documentMapper::fromBo).orElse(null);
		if(result.getClinicalSpecialtyId()!=null)
			result.setClinicalSpecialtyName(clinicalSpecialtyDtoFunction.apply(result.getClinicalSpecialtyId()).getName());
		log.debug("Output result -> {}", result);
		return ResponseEntity.ok().body(result);
	}

}
