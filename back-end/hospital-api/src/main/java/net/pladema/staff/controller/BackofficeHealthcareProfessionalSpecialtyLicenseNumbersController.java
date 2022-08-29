package net.pladema.staff.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;
import net.pladema.staff.controller.constraints.BackofficeProfessionalLicenseNumbersValidator;
import net.pladema.staff.controller.dto.ProfessionalLicenseNumberDto;
import net.pladema.staff.controller.mapper.BackofficeHealthcareProfessionalSpecialtyLicenseNumbersStore;

@RestController
@RequestMapping("backoffice/healthcareprofessionalspecialtylicensenumbers")
public class BackofficeHealthcareProfessionalSpecialtyLicenseNumbersController
		extends AbstractBackofficeController<ProfessionalLicenseNumberDto, Integer> {

	public BackofficeHealthcareProfessionalSpecialtyLicenseNumbersController(
			BackofficeHealthcareProfessionalSpecialtyLicenseNumbersStore backofficeHealthcareProfessionalSpecialtyLicenseNumbersStore,
			BackofficeProfessionalLicenseNumbersValidator backofficeProfessionalLicenseNumbersValidator) {
		super(backofficeHealthcareProfessionalSpecialtyLicenseNumbersStore,
				new BackofficePermissionValidatorAdapter<>(),
				backofficeProfessionalLicenseNumbersValidator);
	}

}
