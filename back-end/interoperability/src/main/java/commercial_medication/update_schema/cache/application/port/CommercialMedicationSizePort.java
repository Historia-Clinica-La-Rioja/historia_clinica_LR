package commercial_medication.update_schema.cache.application.port;

import commercial_medication.update_schema.cache.domain.decodedResponse.SizeList;

public interface CommercialMedicationSizePort {

	void saveAll(SizeList sizes);

}
