package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.backoffice;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.clinichistory.application.rebuildFile.RebuildFile;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.backoffice.BackofficeDocumentFileStore;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.backoffice.dto.DocumentFileDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;

@RestController
@RequestMapping("backoffice/documentfiles")
public class BackofficeDocumentFileController extends AbstractBackofficeController<DocumentFileDto, Long> {

	private final RebuildFile rebuildFile;

	public BackofficeDocumentFileController(
			BackofficeDocumentFileStore store,
			RebuildFile rebuildFile
	) {
		super(store, new BackofficePermissionValidatorAdapter<>(HttpMethod.GET));
		this.rebuildFile = rebuildFile;
	}

	@PutMapping(value = "/{id}/rebuild-file")
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	@ResponseStatus(HttpStatus.OK)
	public void rebuildFile(@PathVariable Long id) {
		rebuildFile.run(id);
	}





}