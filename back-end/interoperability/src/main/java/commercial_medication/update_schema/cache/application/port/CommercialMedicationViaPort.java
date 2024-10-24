package commercial_medication.update_schema.cache.application.port;

import commercial_medication.update_schema.cache.domain.decodedResponse.ViaList;

public interface CommercialMedicationViaPort {

	void saveAll(ViaList vias);

}
