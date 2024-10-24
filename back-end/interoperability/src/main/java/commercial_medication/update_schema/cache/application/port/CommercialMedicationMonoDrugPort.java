package commercial_medication.update_schema.cache.application.port;

import commercial_medication.update_schema.cache.domain.decodedResponse.DrugList;

public interface CommercialMedicationMonoDrugPort {

	void saveAll(DrugList drugs);

}
