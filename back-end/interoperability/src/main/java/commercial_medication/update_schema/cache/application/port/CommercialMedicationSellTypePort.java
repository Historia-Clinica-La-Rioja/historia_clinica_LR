package commercial_medication.update_schema.cache.application.port;

import commercial_medication.update_schema.cache.domain.decodedResponse.SellTypeList;

public interface CommercialMedicationSellTypePort {

	void saveAll(SellTypeList sellTypes);

}
