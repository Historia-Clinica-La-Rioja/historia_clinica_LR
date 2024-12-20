package ar.lamansys.sgh.shared.infrastructure.input.service.medicationstatement;

import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;

import java.util.List;

public interface CommercialMedicationDosageFormUnitSharedPort {

	List<String> getAllValuesByDosageFormName(String name);

}
