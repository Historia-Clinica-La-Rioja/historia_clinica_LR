package net.pladema.staff.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;
import net.pladema.staff.controller.constraints.BackofficeProfessionalLicenseNumbersValidator;
import net.pladema.staff.controller.dto.ProfessionalLicenseNumberDto;
import net.pladema.staff.controller.mapper.BackofficeHealthcareProfessionalLicenseNumbersStore;

@RestController
@RequestMapping("backoffice/healthcareprofessionallicensenumbers")
public class BackofficeHealthcareProfessionalLicenseNumbersController
		extends AbstractBackofficeController<ProfessionalLicenseNumberDto, Integer> {

	public BackofficeHealthcareProfessionalLicenseNumbersController(
			BackofficeHealthcareProfessionalLicenseNumbersStore backofficeHealthcareProfessionalStore,
			BackofficeProfessionalLicenseNumbersValidator backofficeProfessionalLicenseNumbersValidator) {
		super(backofficeHealthcareProfessionalStore,
			new BackofficePermissionValidatorAdapter<>(),
			backofficeProfessionalLicenseNumbersValidator);
	}

}
