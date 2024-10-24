package commercial_medication.update_schema.cache.application.port;

import commercial_medication.update_schema.cache.domain.decodedResponse.ActionList;

public interface CommercialMedicationActionPort {

	void saveAll(ActionList actions);

}
