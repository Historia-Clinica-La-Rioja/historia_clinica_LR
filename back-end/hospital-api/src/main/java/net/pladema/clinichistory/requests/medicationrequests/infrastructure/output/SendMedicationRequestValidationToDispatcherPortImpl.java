package net.pladema.clinichistory.requests.medicationrequests.infrastructure.output;

import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherSenderBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.medicationrequestvalidation.MedicationRequestValidationDispatcherSenderDto;
import ar.lamansys.sgh.shared.infrastructure.mapper.MedicationRequestValidationDispatcherMapper;
import lombok.RequiredArgsConstructor;

import net.pladema.clinichistory.requests.medicationrequests.application.port.output.SendMedicationRequestValidationToDispatcherPort;

import net.pladema.medicationrequestvalidation.application.port.input.ValidateMedicationRequestPort;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SendMedicationRequestValidationToDispatcherPortImpl implements SendMedicationRequestValidationToDispatcherPort {

	private final MedicationRequestValidationDispatcherMapper medicationRequestValidationDispatcherMapper;

	private final ValidateMedicationRequestPort validateMedicationRequestPort;

	@Override
	public List<String> sendMedicationRequestToValidate(MedicationRequestValidationDispatcherSenderBo request) {
		MedicationRequestValidationDispatcherSenderDto requestDto = medicationRequestValidationDispatcherMapper.toMedicationRequestValidationDispatcherSenderDto(request);
		return validateMedicationRequestPort.validateMedicationRequest(requestDto);
	}

}
