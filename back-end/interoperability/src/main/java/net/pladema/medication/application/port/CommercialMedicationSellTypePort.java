package net.pladema.medication.application.port;

import net.pladema.medication.domain.decodedResponse.SellTypeList;

public interface CommercialMedicationSellTypePort {

	void saveAll(SellTypeList sellTypes);

}
