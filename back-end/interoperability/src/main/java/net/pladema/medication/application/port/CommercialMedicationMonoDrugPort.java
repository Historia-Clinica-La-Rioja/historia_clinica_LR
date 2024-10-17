package net.pladema.medication.application.port;

import net.pladema.medication.domain.decodedResponse.DrugList;

public interface CommercialMedicationMonoDrugPort {

	void saveAll(DrugList drugs);

}
