package commercial_medication.update_schema.cache.application.port;

import commercial_medication.update_schema.cache.domain.decodedResponse.ATCDetailList;

public interface CommercialMedicationAtcPort {

	void saveAll(ATCDetailList atcs);

}
