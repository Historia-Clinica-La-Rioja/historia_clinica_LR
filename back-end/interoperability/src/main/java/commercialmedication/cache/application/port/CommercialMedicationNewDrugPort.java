package commercialmedication.cache.application.port;

import commercialmedication.cache.domain.decodedResponse.NewDrugsList;

public interface CommercialMedicationNewDrugPort {

	void saveAll(NewDrugsList newDrugs);

}
