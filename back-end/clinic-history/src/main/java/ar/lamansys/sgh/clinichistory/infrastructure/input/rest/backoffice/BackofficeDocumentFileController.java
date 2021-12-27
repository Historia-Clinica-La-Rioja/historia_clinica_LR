package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.backoffice;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.backoffice.BackofficeDocumentFileStore;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.backoffice.dto.DocumentFileDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/documentfiles")
public class BackofficeDocumentFileController extends AbstractBackofficeController<DocumentFileDto, Long> {

	public BackofficeDocumentFileController(
			BackofficeDocumentFileStore store
	) {
		super(store, new BackofficePermissionValidatorAdapter<>(HttpMethod.GET));
	}


}