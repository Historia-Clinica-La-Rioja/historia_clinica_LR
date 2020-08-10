package net.pladema.medicalconsultation.appointment.service;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

public interface UpdateAppointmentOpeningHoursService {

    AppointmentBo execute(AppointmentBo appointment);
}
