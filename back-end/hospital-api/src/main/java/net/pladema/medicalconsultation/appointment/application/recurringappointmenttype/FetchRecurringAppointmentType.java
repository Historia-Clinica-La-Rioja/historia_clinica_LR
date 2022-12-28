package net.pladema.medicalconsultation.appointment.application.recurringappointmenttype;

import net.pladema.medicalconsultation.appointment.service.domain.RecurringTypeBo;

import java.util.List;

public interface FetchRecurringAppointmentType {

	List<RecurringTypeBo> run();
}
