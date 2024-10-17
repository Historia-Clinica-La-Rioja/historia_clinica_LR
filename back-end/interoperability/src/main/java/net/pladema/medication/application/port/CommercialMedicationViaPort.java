package net.pladema.medication.application.port;

import net.pladema.medication.domain.decodedResponse.ViaList;

public interface CommercialMedicationViaPort {

	void saveAll(ViaList vias);

}
