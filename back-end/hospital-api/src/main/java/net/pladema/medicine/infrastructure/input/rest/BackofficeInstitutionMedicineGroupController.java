package net.pladema.medicine.infrastructure.input.rest;


import net.pladema.medicine.infrastructure.input.rest.constraints.validator.BackofficeInstitutionMedicineGroupEntityValidator;
import net.pladema.medicine.infrastructure.input.rest.dto.InstitutionMedicineGroupDto;
import net.pladema.medicine.infrastructure.output.BackofficeInstitutionMedicineGroupStore;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
@RequestMapping("backoffice/institutionmedicinegroups")
@RestController
public class BackofficeInstitutionMedicineGroupController extends AbstractBackofficeController<InstitutionMedicineGroupDto, Integer> {

	public BackofficeInstitutionMedicineGroupController(BackofficeInstitutionMedicineGroupStore store,
														BackofficeInstitutionMedicineGroupEntityValidator validator)
	{
		super(store, new BackofficePermissionValidatorAdapter<>(), validator);
	}

}
