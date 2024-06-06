package net.pladema.medicalconsultation.appointment.application.port;

import net.pladema.medicalconsultation.appointment.service.domain.RecurringTypeBo;

import java.util.List;

public interface RecurringAppointmentTypeStorage {

	List<RecurringTypeBo> getRecurringAppointmentType();
}
