package ar.lamansys.sgx.shared.configuration.infrastructure.input.rest;

import ar.lamansys.sgx.shared.configuration.domain.PropertyBo;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/properties")
public class BackofficePropertiesController extends AbstractBackofficeController<PropertyBo, String>{

	public BackofficePropertiesController(BackofficePropertyStore store) {
		super(store, new BackofficePermissionValidatorAdapter<>(HttpMethod.GET));
	}

}
