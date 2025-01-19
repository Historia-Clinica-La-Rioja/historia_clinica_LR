package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.ports.ClinicHistoryMedicationRequestPort;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedMedicationRequestPort;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ClinicHistoryMedicationRequestPortImpl implements ClinicHistoryMedicationRequestPort {

	private final SharedMedicationRequestPort sharedMedicationRequestPort;

	@Override
	public UUID fetchMedicationRequestUUIDById(Integer id) {
		return sharedMedicationRequestPort.getMedicationRequestUUIDById(id);
	}

}
