package net.pladema.clinichistory.requests.medicationrequests.application;

import ar.lamansys.sgh.clinichistory.domain.document.impl.MedicationRequestBo;

public interface SendMedicationRequestValidation {

	void run(MedicationRequestBo medicationRequest);

}
