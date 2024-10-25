package commercialmedication.cache.application.port;

import commercialmedication.cache.domain.decodedResponse.SellTypeList;

public interface CommercialMedicationSellTypePort {

	void saveAll(SellTypeList sellTypes);

}
