package commercial_medication.update_schema.cache.application.port;

import commercial_medication.update_schema.cache.domain.decodedResponse.FormList;

public interface CommercialMedicationFormPort {

	void saveAll(FormList forms);

}
