package net.pladema.clinichistory.requests.medicationrequests.application.port.output;

import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherSenderBo;

public interface SendMedicationRequestValidationToDispatcherPort {

	void sendMedicationRequestToValidate(MedicationRequestValidationDispatcherSenderBo request);

}
