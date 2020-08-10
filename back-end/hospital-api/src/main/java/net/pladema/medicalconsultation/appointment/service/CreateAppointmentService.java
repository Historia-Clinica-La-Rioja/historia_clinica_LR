package net.pladema.medicalconsultation.appointment.service;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

public interface CreateAppointmentService {

    AppointmentBo execute(AppointmentBo appointment);
}
