package net.pladema.medicalconsultation.appointment.service;

import net.pladema.medicalconsultation.diary.service.domain.CustomRecurringAppointmentBo;

public interface FetchCustomAppointment {

	CustomRecurringAppointmentBo execute(Integer appointmentId);
}
