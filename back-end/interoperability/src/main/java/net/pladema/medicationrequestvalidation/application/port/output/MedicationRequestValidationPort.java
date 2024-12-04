package net.pladema.medicationrequestvalidation.application.port.output;

import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherSenderBo;

public interface MedicationRequestValidationPort {

	String validateMedicationRequest(MedicationRequestValidationDispatcherSenderBo request);

}
