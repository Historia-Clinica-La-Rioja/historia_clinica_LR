package net.pladema.medicine.infrastructure.input.rest;

import net.pladema.medicine.infrastructure.input.rest.constraints.validator.BackofficeMedicineGroupMedicineEntityValidator;
import net.pladema.medicine.infrastructure.input.rest.dto.MedicineGroupMedicineDto;
import net.pladema.medicine.infrastructure.output.BackofficeMedicineGroupMedicineStore;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("backoffice/medicinegroupmedicines")
@RestController
public class BackofficeMedicineGroupMedicineController extends AbstractBackofficeController<MedicineGroupMedicineDto, Integer> {

	public BackofficeMedicineGroupMedicineController (BackofficeMedicineGroupMedicineStore store,
													  BackofficeMedicineGroupMedicineEntityValidator validator)
	{
		super(store, new BackofficePermissionValidatorAdapter<>(), validator);
	}

}
