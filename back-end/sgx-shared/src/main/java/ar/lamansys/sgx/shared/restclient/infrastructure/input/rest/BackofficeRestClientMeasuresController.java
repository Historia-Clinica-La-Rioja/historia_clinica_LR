package ar.lamansys.sgx.shared.restclient.infrastructure.input.rest;

import ar.lamansys.sgx.shared.restclient.infrastructure.output.repository.RestClientMeasure;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/rest-client-measures")
public class BackofficeRestClientMeasuresController extends AbstractBackofficeController<RestClientMeasure, Long>{

	public BackofficeRestClientMeasuresController(BackofficeRestClientMeasuresStore store) {
		super(store, new BackofficePermissionValidatorAdapter<>(HttpMethod.GET));
	}

}
