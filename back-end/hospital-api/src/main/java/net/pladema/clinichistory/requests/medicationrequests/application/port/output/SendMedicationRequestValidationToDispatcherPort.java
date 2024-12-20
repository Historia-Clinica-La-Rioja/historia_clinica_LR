package net.pladema.clinichistory.requests.medicationrequests.application.port.output;

import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherSenderBo;

import java.util.List;

public interface SendMedicationRequestValidationToDispatcherPort {

	List<String> sendMedicationRequestToValidate(MedicationRequestValidationDispatcherSenderBo request);

}
