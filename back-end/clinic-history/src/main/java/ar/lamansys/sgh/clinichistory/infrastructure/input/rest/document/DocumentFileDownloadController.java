package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.document;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.clinichistory.application.fetchdocumentfile.FetchDocumentFileById;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
//@Api(value = "Documents", tags = {"Documents"})
@RestController
@RequestMapping("/institutions/{institutionId}/documents")
public class DocumentFileDownloadController {

    private final FetchDocumentFileById fetchDocumentFileById;


    @GetMapping(value = "/{id}/downloadFile")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PRESCRIPTOR, TECNICO')")
	public ResponseEntity<Resource> downloadPdf(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "id") Long id)
	{
        var storedFileBo = fetchDocumentFileById.run(id);

		return StoredFileResponse.sendFile(storedFileBo);
    }
}
