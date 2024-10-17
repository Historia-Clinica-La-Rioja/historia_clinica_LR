package net.pladema.medication.application.port;

import net.pladema.medication.domain.decodedResponse.PublicSanityInternCodeList;

public interface CommercialMedicationControlPort {

	void saveAll(PublicSanityInternCodeList controls);

}
