package commercialmedication.cache.application.port;

import commercialmedication.cache.domain.decodedResponse.SizeList;

public interface CommercialMedicationSizePort {

	void saveAll(SizeList sizes);

}
