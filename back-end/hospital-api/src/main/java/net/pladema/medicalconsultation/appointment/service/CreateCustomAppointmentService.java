package net.pladema.medicalconsultation.appointment.service;

import net.pladema.medicalconsultation.appointment.service.domain.CreateCustomAppointmentBo;

public interface CreateCustomAppointmentService {

	boolean execute(CreateCustomAppointmentBo bo);
}
