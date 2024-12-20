package commercialmedication.cache.application.port;

import commercialmedication.cache.domain.decodedResponse.ATCDetailList;

public interface CommercialMedicationAtcPort {

	void saveAll(ATCDetailList atcs);

}
