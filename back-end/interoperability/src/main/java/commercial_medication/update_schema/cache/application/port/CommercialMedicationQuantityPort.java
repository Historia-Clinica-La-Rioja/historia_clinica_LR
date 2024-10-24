package commercial_medication.update_schema.cache.application.port;

import commercial_medication.update_schema.cache.domain.decodedResponse.QuantityList;

public interface CommercialMedicationQuantityPort {

	void saveAll(QuantityList quantities);

}
