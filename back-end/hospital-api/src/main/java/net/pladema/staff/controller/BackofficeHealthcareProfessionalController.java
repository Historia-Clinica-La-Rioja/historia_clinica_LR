package net.pladema.staff.controller;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;
import net.pladema.staff.controller.constraints.BackofficeHealthcareProfessionalEntityValidator;
import net.pladema.staff.controller.dto.BackofficeHealthcareProfessionalCompleteDto;
import net.pladema.staff.controller.mapper.BackofficeHealthcareProfessionalStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/healthcareprofessionals")
public class BackofficeHealthcareProfessionalController
		extends AbstractBackofficeController<BackofficeHealthcareProfessionalCompleteDto, Integer> {

	public BackofficeHealthcareProfessionalController(BackofficeHealthcareProfessionalStore backofficeHealthcareProfessionalStore,
													  BackofficeHealthcareProfessionalEntityValidator healthcareProfessionalEntityValidator) {
		super(backofficeHealthcareProfessionalStore,
				new BackofficePermissionValidatorAdapter<>(),
				healthcareProfessionalEntityValidator);
	}

}
