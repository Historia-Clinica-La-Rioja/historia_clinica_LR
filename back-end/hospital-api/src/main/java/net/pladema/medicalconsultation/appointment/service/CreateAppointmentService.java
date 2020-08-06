package net.pladema.medicalconsultation.appointment.service;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

import java.util.Collection;

public interface CreateAppointmentService {

    AppointmentBo execute(AppointmentBo appointment);
}
