package ar.lamansys.sgh.clinichistory.application.ports;

import java.util.UUID;

public interface ClinicHistoryMedicationRequestPort {

	UUID fetchMedicationRequestUUIDById(Integer id);

}
