package net.pladema.medicine.infrastructure.input.rest;

import net.pladema.medicine.infrastructure.input.rest.constraints.validator.BackofficeMedicineGroupEntityValidator;
import net.pladema.medicine.infrastructure.output.repository.MedicineGroupRepository;
import net.pladema.medicine.infrastructure.output.repository.entity.MedicineGroup;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("backoffice/medicinegroups")
@RestController
public class BackofficeMedicineGroupController extends AbstractBackofficeController<MedicineGroup, Integer> {

	public BackofficeMedicineGroupController(MedicineGroupRepository repository, BackofficeMedicineGroupEntityValidator validator){
		super(new BackofficeRepository<>(repository, new SingleAttributeBackofficeQueryAdapter<>("name")),
				new BackofficePermissionValidatorAdapter<>(),
				validator);
	}

}
