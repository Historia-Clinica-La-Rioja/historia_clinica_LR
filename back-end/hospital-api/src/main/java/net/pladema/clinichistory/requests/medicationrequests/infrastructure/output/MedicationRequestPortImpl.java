package net.pladema.clinichistory.requests.medicationrequests.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.requests.medicationrequests.application.port.output.MedicationRequestPort;

import net.pladema.clinichistory.requests.medicationrequests.repository.MedicationRequestRepository;

import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MedicationRequestPortImpl implements MedicationRequestPort {

	private final MedicationRequestRepository medicationRequestRepository;

	@Override
	public UUID getMedicationRequestUUIDById(Integer medicationRequestId) {
		return medicationRequestRepository.fetchUUIDById(medicationRequestId);
	}

}
