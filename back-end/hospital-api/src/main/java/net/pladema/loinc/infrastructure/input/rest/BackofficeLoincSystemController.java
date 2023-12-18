package net.pladema.loinc.infrastructure.input.rest;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.loinc.infrastructure.output.entity.LoincSystem;
import net.pladema.loinc.infrastructure.output.repository.LoincSystemRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;

@RestController
@RequestMapping("backoffice/loinc-systems")
public class BackofficeLoincSystemController extends AbstractBackofficeController<LoincSystem, Short> {
	public BackofficeLoincSystemController(LoincSystemRepository repository) {
		super(repository, new BackofficePermissionValidatorAdapter<>(HttpMethod.GET));
	}
}
