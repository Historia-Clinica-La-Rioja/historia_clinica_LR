package net.pladema.medicalconsultation.appointment.application.port;

import net.pladema.medicalconsultation.appointment.domain.NewAppointmentNotificationBo;

public interface NewAppointmentNotification {
	void run(NewAppointmentNotificationBo newAppointmentNotification);
}