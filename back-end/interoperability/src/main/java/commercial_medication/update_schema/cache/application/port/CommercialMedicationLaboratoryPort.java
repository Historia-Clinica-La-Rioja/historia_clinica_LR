package commercial_medication.update_schema.cache.application.port;

import commercial_medication.update_schema.cache.domain.decodedResponse.LaboratoryList;

public interface CommercialMedicationLaboratoryPort {

	void saveAll(LaboratoryList laboratories);

}
