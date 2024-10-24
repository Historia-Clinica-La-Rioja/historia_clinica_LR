package commercial_medication.update_schema.cache.application.port;

import commercial_medication.update_schema.cache.domain.decodedResponse.PublicSanityInternCodeList;

public interface CommercialMedicationControlPort {

	void saveAll(PublicSanityInternCodeList controls);

}
