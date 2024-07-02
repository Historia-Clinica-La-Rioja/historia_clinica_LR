package net.pladema.medicine.infrastructure.input.rest;

import net.pladema.medicine.infrastructure.input.rest.dto.MedicineFinancingStatusDto;
import net.pladema.medicine.infrastructure.output.BackofficeMedicineFinancingStatusStore;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("backoffice/medicinefinancingstatus")
@RestController
public class BackofficeMedicineFinancingStatusController extends AbstractBackofficeController<MedicineFinancingStatusDto, Integer> {

	private BackofficeMedicineFinancingStatusStore backofficeMedicineStore;

	public BackofficeMedicineFinancingStatusController(BackofficeMedicineFinancingStatusStore backofficeMedicineStore){
		super(backofficeMedicineStore);
	}
	
}
