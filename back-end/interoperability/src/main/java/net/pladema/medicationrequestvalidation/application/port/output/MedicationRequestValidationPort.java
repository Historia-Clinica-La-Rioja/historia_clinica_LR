package net.pladema.medicationrequestvalidation.application.port.output;

import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherSenderBo;

import java.util.List;

public interface MedicationRequestValidationPort {

	List<String> validateMedicationRequest(MedicationRequestValidationDispatcherSenderBo request);

}
