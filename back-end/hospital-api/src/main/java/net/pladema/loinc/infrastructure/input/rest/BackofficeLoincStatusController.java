package net.pladema.loinc.infrastructure.input.rest;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.loinc.infrastructure.output.entity.LoincStatus;
import net.pladema.loinc.infrastructure.output.repository.LoincStatusRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;

@RestController
@RequestMapping("backoffice/loinc-statuses")
public class BackofficeLoincStatusController extends AbstractBackofficeController<LoincStatus, Short> {
	public BackofficeLoincStatusController(LoincStatusRepository repository) {
		super(repository, new BackofficePermissionValidatorAdapter<>(HttpMethod.GET));
	}
}
