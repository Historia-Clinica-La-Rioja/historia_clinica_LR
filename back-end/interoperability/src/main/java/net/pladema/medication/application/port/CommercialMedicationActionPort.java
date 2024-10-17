package net.pladema.medication.application.port;

import net.pladema.medication.domain.decodedResponse.ActionList;

public interface CommercialMedicationActionPort {

	void saveAll(ActionList actions);

}
