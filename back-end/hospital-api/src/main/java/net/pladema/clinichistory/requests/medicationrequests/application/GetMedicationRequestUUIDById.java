package net.pladema.clinichistory.requests.medicationrequests.application;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.requests.medicationrequests.application.port.output.MedicationRequestPort;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetMedicationRequestUUIDById {

	private final MedicationRequestPort medicationRequestPort;

	public UUID run(Integer medicationRequestId) {
		log.debug("Input parameters -> medicationRequestId {}", medicationRequestId);
		UUID result = medicationRequestPort.getMedicationRequestUUIDById(medicationRequestId);
		log.debug("Output -> {}", result);
		return result;
	}

}
