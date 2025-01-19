package net.pladema.clinichistory.requests.medicationrequests.infrastructure.input;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedMedicationRequestPort;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.medicationrequests.application.GetMedicationRequestUUIDById;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class SharedMedicationRequestPortImpl implements SharedMedicationRequestPort {

	private final GetMedicationRequestUUIDById getMedicationRequestUUIDById;

	@Override
	public UUID getMedicationRequestUUIDById(Integer medicationRequestId) {
		log.debug("Input parameters -> medicationRequestId {}", medicationRequestId);
		UUID result = getMedicationRequestUUIDById.run(medicationRequestId);
		log.debug("Output -> {}", result);
		return result;
	}

}
