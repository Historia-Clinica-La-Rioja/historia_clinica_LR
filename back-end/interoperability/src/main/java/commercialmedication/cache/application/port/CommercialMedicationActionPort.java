package commercialmedication.cache.application.port;

import commercialmedication.cache.domain.decodedResponse.ActionList;

public interface CommercialMedicationActionPort {

	void saveAll(ActionList actions);

}
