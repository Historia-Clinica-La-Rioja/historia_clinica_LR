package commercial_medication.update_schema.cache.application.port;

import commercial_medication.update_schema.cache.domain.decodedResponse.NewDrugsList;

public interface CommercialMedicationNewDrugPort {

	void saveAll(NewDrugsList newDrugs);

}
