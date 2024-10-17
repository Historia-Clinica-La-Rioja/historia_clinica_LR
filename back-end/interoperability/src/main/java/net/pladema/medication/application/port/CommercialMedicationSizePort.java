package net.pladema.medication.application.port;

import net.pladema.medication.domain.decodedResponse.SizeList;

public interface CommercialMedicationSizePort {

	void saveAll(SizeList sizes);

}
