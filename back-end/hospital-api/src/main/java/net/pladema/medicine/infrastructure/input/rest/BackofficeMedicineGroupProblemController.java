package net.pladema.medicine.infrastructure.input.rest;

import net.pladema.medicine.infrastructure.input.rest.constraints.validator.BackofficeMedicineGroupProblemEntityValidator;
import net.pladema.medicine.infrastructure.input.rest.dto.MedicineGroupProblemDto;
import net.pladema.medicine.infrastructure.output.BackofficeMedicineGroupProblemStore;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
@RequestMapping("backoffice/medicinegroupproblems")
@RestController
public class BackofficeMedicineGroupProblemController extends AbstractBackofficeController<MedicineGroupProblemDto, Integer> {

	public BackofficeMedicineGroupProblemController(BackofficeMedicineGroupProblemStore store,
													BackofficeMedicineGroupProblemEntityValidator validator)
	{
		super(store, new BackofficePermissionValidatorAdapter<>(), validator);
	}

}
