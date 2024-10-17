package net.pladema.medication.application.port;

import net.pladema.medication.domain.decodedResponse.QuantityList;

public interface CommercialMedicationQuantityPort {

	void saveAll(QuantityList quantities);

}
