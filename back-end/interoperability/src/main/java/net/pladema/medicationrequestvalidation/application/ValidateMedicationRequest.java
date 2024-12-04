package net.pladema.medicationrequestvalidation.application;

import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherSenderBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicationrequestvalidation.application.port.output.MedicationRequestValidationPort;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ValidateMedicationRequest {

	private final MedicationRequestValidationPort medicationRequestValidationPort;

	public String run(MedicationRequestValidationDispatcherSenderBo request) {
		log.debug("Input parameters -> request {}", request);
		String result = medicationRequestValidationPort.validateMedicationRequest(request);
		log.debug("Output -> {}", result);
		return result;
	}

}
