package commercialmedication.cache.application.port;

import commercialmedication.cache.domain.decodedResponse.DrugList;

public interface CommercialMedicationMonoDrugPort {

	void saveAll(DrugList drugs);

}
