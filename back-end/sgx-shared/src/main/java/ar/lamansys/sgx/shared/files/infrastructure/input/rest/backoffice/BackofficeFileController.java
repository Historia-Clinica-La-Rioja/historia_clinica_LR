package ar.lamansys.sgx.shared.files.infrastructure.input.rest.backoffice;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.files.FixFileService;
import ar.lamansys.sgx.shared.files.infrastructure.input.rest.backoffice.dto.FileInfoDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;

@RestController
@RequestMapping("backoffice/files")
public class BackofficeFileController extends AbstractBackofficeController<FileInfoDto, Long> {
	private final FixFileService fixFileService;

	public BackofficeFileController(
			BackofficeFileStore store,
			FixFileService fixFileService
	) {
		super(store, new BackofficePermissionValidatorAdapter<>(HttpMethod.GET, HttpMethod.PUT));
		this.fixFileService = fixFileService;
	}

	@PutMapping(value = "/{id}/fix-metadata")
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	@ResponseStatus(HttpStatus.OK)
	public void fixMetadata(@PathVariable Long id) {
		fixFileService.fixMetadata(id);
	}

}