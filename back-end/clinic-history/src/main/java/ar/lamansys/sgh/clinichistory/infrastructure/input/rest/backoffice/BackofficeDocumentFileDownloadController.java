package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.backoffice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.clinichistory.application.fetchdocumentfile.FetchDocumentFileById;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.files.FileService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("backoffice/documentfiles")
public class BackofficeDocumentFileDownloadController {

	private final FetchDocumentFileById fetchDocumentFileById;

	private final FileService fileService;

	private final FeatureFlagsService featureFlagsService;

	@GetMapping(value = "/{id}/downloadFile")
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public ResponseEntity<InputStreamResource> downloadPdf(@PathVariable Long id) {
		if (!featureFlagsService.isOn(AppFeature.HABILITAR_DESCARGA_DOCUMENTOS_PDF))
			return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);
		var documentFile = fetchDocumentFileById.run(id);
		ByteArrayInputStream pdfFile;
		long sizeFile;
		try {
			pdfFile = fileService.readStreamFromAbsolutePath(documentFile.getFilepath());
			sizeFile = Files.size(Paths.get(documentFile.getFilepath()));
		} catch (IOException e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		InputStreamResource resource = new InputStreamResource(pdfFile);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + documentFile.getFilename())
				.contentType(MediaType.APPLICATION_PDF)
				.contentLength(sizeFile)
				.body(resource);
	}



}