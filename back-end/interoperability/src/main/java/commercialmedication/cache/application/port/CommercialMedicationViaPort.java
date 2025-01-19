package commercialmedication.cache.application.port;

import commercialmedication.cache.domain.decodedResponse.ViaList;

public interface CommercialMedicationViaPort {

	void saveAll(ViaList vias);

}
