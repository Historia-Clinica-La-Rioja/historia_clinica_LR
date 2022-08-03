package net.pladema.medicalconsultation.appointment.service.ports;

import java.util.List;

import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.AppointmentFilterBo;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.AppointmentInfoBo;

public interface AppointmentStorage {
	List<AppointmentInfoBo> fetchAppointments(AppointmentFilterBo appointmentFilterBo);
}
