package commercialmedication.cache.application.port;

import commercialmedication.cache.domain.decodedResponse.PublicSanityInternCodeList;

public interface CommercialMedicationControlPort {

	void saveAll(PublicSanityInternCodeList controls);

}
