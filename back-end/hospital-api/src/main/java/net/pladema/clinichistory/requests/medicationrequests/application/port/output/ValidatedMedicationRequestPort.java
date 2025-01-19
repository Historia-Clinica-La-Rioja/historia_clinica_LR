package net.pladema.clinichistory.requests.medicationrequests.application.port.output;

import net.pladema.clinichistory.requests.medicationrequests.domain.ValidatedMedicationRequestBo;

import java.util.List;

public interface ValidatedMedicationRequestPort {

	void saveAll(List<ValidatedMedicationRequestBo> validatedMedicationRequests);

}
