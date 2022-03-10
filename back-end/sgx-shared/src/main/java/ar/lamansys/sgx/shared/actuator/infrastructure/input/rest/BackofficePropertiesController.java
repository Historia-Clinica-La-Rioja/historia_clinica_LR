package ar.lamansys.sgx.shared.actuator.infrastructure.input.rest;

import ar.lamansys.sgx.shared.actuator.domain.PropertyBo;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/properties")
public class BackofficePropertiesController extends AbstractBackofficeController<PropertyBo, Integer>{

	public BackofficePropertiesController(BackofficePropertyStore store) {
		super(store, new BackofficePermissionValidatorAdapter<>(HttpMethod.GET));
	}

}
