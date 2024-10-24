package commercial_medication.update_schema.cache.application.port;

import commercial_medication.update_schema.cache.domain.decodedResponse.PotencyList;

public interface CommercialMedicationPotencyPort {

	void saveAll(PotencyList potencies);

}
