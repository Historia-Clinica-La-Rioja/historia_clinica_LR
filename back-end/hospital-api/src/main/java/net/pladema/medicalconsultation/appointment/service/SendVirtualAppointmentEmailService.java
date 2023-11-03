package net.pladema.medicalconsultation.appointment.service;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

public interface SendVirtualAppointmentEmailService {

	void run(AppointmentBo appointment);

}
