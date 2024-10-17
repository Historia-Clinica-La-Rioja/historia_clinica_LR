package net.pladema.medication.application.port;

import net.pladema.medication.domain.decodedResponse.PotencyList;

public interface CommercialMedicationPotencyPort {

	void saveAll(PotencyList potencies);

}
