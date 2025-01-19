package commercialmedication.cache.application.port;

import commercialmedication.cache.domain.decodedResponse.QuantityList;

public interface CommercialMedicationQuantityPort {

	void saveAll(QuantityList quantities);

}
