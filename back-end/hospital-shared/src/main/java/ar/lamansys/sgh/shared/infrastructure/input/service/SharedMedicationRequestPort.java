package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.util.UUID;

public interface SharedMedicationRequestPort {

	UUID getMedicationRequestUUIDById(Integer medicationRequestId);

}
