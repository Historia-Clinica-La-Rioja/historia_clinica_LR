package net.pladema.medication.application.port;

import net.pladema.medication.domain.decodedResponse.ATCDetailList;

public interface CommercialMedicationAtcPort {

	void saveAll(ATCDetailList atcs);

}
