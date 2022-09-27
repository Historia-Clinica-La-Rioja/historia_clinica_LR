package net.pladema.medicalconsultation.appointment.repository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentUpdateRepository {

	List<Integer> getPastAppointmentsByStatesAndUpdatedBeforeDate(List<Short> stateIds, LocalDateTime lastUpdateDate, Short limit);

}
