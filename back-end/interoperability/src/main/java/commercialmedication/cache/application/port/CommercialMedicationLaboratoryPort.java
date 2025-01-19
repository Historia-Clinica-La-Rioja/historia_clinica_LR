package commercialmedication.cache.application.port;

import commercialmedication.cache.domain.decodedResponse.LaboratoryList;

public interface CommercialMedicationLaboratoryPort {

	void saveAll(LaboratoryList laboratories);

}
