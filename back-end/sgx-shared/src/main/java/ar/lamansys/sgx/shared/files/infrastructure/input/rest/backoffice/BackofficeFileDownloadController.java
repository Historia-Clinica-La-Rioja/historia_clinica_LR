package ar.lamansys.sgx.shared.files.infrastructure.input.rest.backoffice;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
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
import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileInfo;
import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileInfoRepository;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("backoffice/files")
public class BackofficeFileDownloadController {

	private final FileInfoRepository fileInfoRepository;
	private final FileService fileService;

	private final FeatureFlagsService featureFlagsService;

	@GetMapping(value = "/{id}/downloadFile")
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public ResponseEntity<Resource> downloadPdf(@PathVariable Long id) throws IOException {
		if (!featureFlagsService.isOn(AppFeature.HABILITAR_DESCARGA_DOCUMENTOS_PDF))
			return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);
		var optFile = fileInfoRepository.findById(id);
		if (optFile.isEmpty())
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		var fileInfo = optFile.get();

		return StoredFileResponse.sendFile(
				loadFile(fileInfo)
		);
	}

	private StoredFileBo loadFile(FileInfo fileInfo) {
		Resource resource;
		try {
			resource = fileService.loadFileRelativePath(
					fileService.buildCompletePath(fileInfo.getRelativePath())
			);
		}  catch (FileServiceException e){
			resource = fileService.loadFileFromAbsolutePath(
					fileService.buildCompletePath(fileInfo.getOriginalPath())
			);
		}
		return new StoredFileBo(
				resource,
				MediaType.APPLICATION_OCTET_STREAM.toString(),
				fileInfo.getName()
		);
	}

}