package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.backoffice;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.clinichistory.application.fetchdocumentfile.FetchDocumentFileById;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("backoffice/documentfiles")
public class BackofficeDocumentFileDownloadController {

	private final FetchDocumentFileById fetchDocumentBucketObject;

	private final FeatureFlagsService featureFlagsService;

	@GetMapping(value = "/{id}/downloadFile")
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public ResponseEntity<Resource> downloadPdf(@PathVariable Long id) {
		if (!featureFlagsService.isOn(AppFeature.HABILITAR_DESCARGA_DOCUMENTOS_PDF))
			return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);

		return StoredFileResponse.sendFile(
				fetchDocumentBucketObject.run(id)
		);

	}



}