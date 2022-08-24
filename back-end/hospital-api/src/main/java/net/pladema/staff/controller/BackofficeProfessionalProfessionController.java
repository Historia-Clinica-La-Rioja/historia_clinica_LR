package net.pladema.staff.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;
import net.pladema.staff.controller.constraints.BackofficeProfessionalProfessionEntityValidator;
import net.pladema.staff.controller.dto.ProfessionalProfessionBackofficeDto;
import net.pladema.staff.controller.mapper.BackofficeProfessionalProfessionStore;

@RestController
@RequestMapping("backoffice/professionalprofessions")
public class BackofficeProfessionalProfessionController
		extends AbstractBackofficeController<ProfessionalProfessionBackofficeDto, Integer> {

	public BackofficeProfessionalProfessionController(BackofficeProfessionalProfessionStore backofficeProfessionalProfessionStore,
													  BackofficeProfessionalProfessionEntityValidator healthcareProfessionalEntityValidator) {
		super(backofficeProfessionalProfessionStore,
				new BackofficePermissionValidatorAdapter<>(),
				healthcareProfessionalEntityValidator);
	}

}
