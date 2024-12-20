package net.pladema.clinichistory.requests.medicationrequests.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.requests.medicationrequests.application.port.output.ValidatedMedicationRequestPort;

import net.pladema.clinichistory.requests.medicationrequests.domain.ValidatedMedicationRequestBo;

import net.pladema.clinichistory.requests.medicationrequests.infrastructure.output.repository.ValidatedMedicationRequestRepository;

import net.pladema.clinichistory.requests.medicationrequests.infrastructure.output.repository.entity.ValidatedMedicationRequest;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ValidatedMedicationRequestPortImpl implements ValidatedMedicationRequestPort {

	private final ValidatedMedicationRequestRepository validatedMedicationRequestRepository;

	@Override
	public void saveAll(List<ValidatedMedicationRequestBo> validatedMedicationRequests) {
		validatedMedicationRequestRepository.saveAll(mapToEntityList(validatedMedicationRequests));
	}

	private List<ValidatedMedicationRequest> mapToEntityList(List<ValidatedMedicationRequestBo> validatedMedicationRequests) {
		return validatedMedicationRequests.stream()
				.map(ValidatedMedicationRequest::new)
				.collect(Collectors.toList());
	}

}
