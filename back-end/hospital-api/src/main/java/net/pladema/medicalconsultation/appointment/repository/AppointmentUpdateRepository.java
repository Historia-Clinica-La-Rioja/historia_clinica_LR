package net.pladema.medicalconsultation.appointment.repository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentUpdateRepository {

	List<Integer> getAppointmentsBeforeDateByStates(List<Short> stateIds, LocalDateTime maxAppointmentDate, Short limit);

}
