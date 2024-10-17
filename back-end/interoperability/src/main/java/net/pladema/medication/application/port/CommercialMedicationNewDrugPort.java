package net.pladema.medication.application.port;

import net.pladema.medication.domain.decodedResponse.NewDrugsList;

public interface CommercialMedicationNewDrugPort {

	void saveAll(NewDrugsList newDrugs);

}
