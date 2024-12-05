package net.pladema.medicationrequestvalidation.application.port.input;

import ar.lamansys.sgh.shared.infrastructure.input.service.medicationrequestvalidation.MedicationRequestValidationDispatcherSenderDto;

import java.util.List;

public interface ValidateMedicationRequestPort {

	List<String> validateMedicationRequest(MedicationRequestValidationDispatcherSenderDto medicationRequestValidationDispatcherSenderDto);

}
