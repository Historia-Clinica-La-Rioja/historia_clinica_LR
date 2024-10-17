package net.pladema.medication.application.port;

import net.pladema.medication.domain.decodedResponse.LaboratoryList;

public interface CommercialMedicationLaboratoryPort {

	void saveAll(LaboratoryList laboratories);

}
