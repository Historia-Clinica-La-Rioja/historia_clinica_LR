package commercialmedication.cache.application.port;

import commercialmedication.cache.domain.decodedResponse.PotencyList;

public interface CommercialMedicationPotencyPort {

	void saveAll(PotencyList potencies);

}
