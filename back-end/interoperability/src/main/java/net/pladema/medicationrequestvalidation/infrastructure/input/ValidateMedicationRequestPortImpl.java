package net.pladema.medicationrequestvalidation.infrastructure.input;

import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherSenderBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.medicationrequestvalidation.MedicationRequestValidationDispatcherSenderDto;
import ar.lamansys.sgh.shared.infrastructure.mapper.MedicationRequestValidationDispatcherMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicationrequestvalidation.application.ValidateMedicationRequest;
import net.pladema.medicationrequestvalidation.application.port.input.ValidateMedicationRequestPort;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ValidateMedicationRequestPortImpl implements ValidateMedicationRequestPort {

	private final MedicationRequestValidationDispatcherMapper medicationRequestValidationDispatcherMapper;

	private final ValidateMedicationRequest validateMedicationRequest;

	@Override
	public String validateMedicationRequest(MedicationRequestValidationDispatcherSenderDto request) {
		log.debug("Input parameters -> request {}", request);
		MedicationRequestValidationDispatcherSenderBo requestBo = medicationRequestValidationDispatcherMapper.fromMedicationRequestValidationDispatcherSenderDto(request);
		String result = validateMedicationRequest.run(requestBo);
		log.debug("Output -> {}", result);
		return result;
	}

}
