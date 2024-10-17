package net.pladema.medication.application.port;

import net.pladema.medication.domain.decodedResponse.FormList;

public interface CommercialMedicationFormPort {

	void saveAll(FormList forms);

}
