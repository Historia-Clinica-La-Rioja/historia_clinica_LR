package ar.lamansys.sgx.shared.files.infrastructure.input.rest.backoffice;

import java.io.IOException;
import java.net.URLConnection;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.files.FileService;
import ar.lamansys.sgx.shared.files.exception.FileServiceException;
import ar.lamansys.sgx.shared.files.infrastructure.input.rest.backoffice.dto.FileInfoDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;

@RestController
@RequestMapping("backoffice/files")
public class BackofficeFileController extends AbstractBackofficeController<FileInfoDto, Long> {

	private final FileService fileService;

	private final FeatureFlagsService featureFlagsService;

	public BackofficeFileController(
			BackofficeFileStore store,
			FileService fileService, FeatureFlagsService featureFlagsService) {
		super(store, new BackofficePermissionValidatorAdapter<>(HttpMethod.GET));
		this.fileService = fileService;
		this.featureFlagsService = featureFlagsService;
	}


	@GetMapping(value = "/{id}/downloadFile")
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public ResponseEntity downloadPdf(@PathVariable Long id) throws IOException {
		if (!featureFlagsService.isOn(AppFeature.HABILITAR_DESCARGA_DOCUMENTOS_PDF))
			return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);
		var optFile = store.findById(id);
		if (optFile.isEmpty())
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		var fileInfo = optFile.get();
		Resource resource;
		long sizeFile = fileService.getFileSize(fileInfo);
		try {
			resource = fileService.loadFileRelativePath(fileInfo.getRelativePath());
		}  catch (FileServiceException e){
			resource = fileService.loadFileFromAbsolutePath(fileInfo.getOriginalPath());
		}
		var contentType = MediaType.APPLICATION_OCTET_STREAM;
		try {
			contentType = MediaType.parseMediaType(fileInfo.getContentType());
		} catch (InvalidMediaTypeException ex) {
			logger.error(ex.getMessage());
			contentType = MediaType.parseMediaType(URLConnection.guessContentTypeFromName(fileInfo.getName()));
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileInfo.getName())
				.contentType(contentType)
				.contentLength(sizeFile)
				.body(resource);
	}

}