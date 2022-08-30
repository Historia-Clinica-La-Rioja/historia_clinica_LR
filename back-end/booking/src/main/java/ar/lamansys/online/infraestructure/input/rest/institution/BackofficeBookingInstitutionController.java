package ar.lamansys.online.infraestructure.input.rest.institution;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.online.infraestructure.output.repository.institution.BackofficeBookingInstitutionStore;
import ar.lamansys.online.infraestructure.output.repository.institution.BackofficeBookingInstitutionDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;

@RestController
@RequestMapping("backoffice/booking-institution")
public class BackofficeBookingInstitutionController extends AbstractBackofficeController<BackofficeBookingInstitutionDto, Integer>{

	public BackofficeBookingInstitutionController(BackofficeBookingInstitutionStore store) {
		super(store, new BackofficePermissionValidatorAdapter<>(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE));
	}

}
