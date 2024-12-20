package commercialmedication.cache.application.port;

import commercialmedication.cache.domain.decodedResponse.FormList;

public interface CommercialMedicationFormPort {

	void saveAll(FormList forms);

}
