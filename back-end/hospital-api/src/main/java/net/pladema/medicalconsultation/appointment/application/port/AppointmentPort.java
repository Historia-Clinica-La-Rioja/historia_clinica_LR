package net.pladema.medicalconsultation.appointment.application.port;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

import java.util.Optional;

public interface AppointmentPort {

	void updateAppointmentState(Integer appointmentId, Short appointmentStatusId);

	Integer getAppointmentParentId(Integer appointmentId);

	Short getAppointmentStateIdByAppointmentId(Integer appointmentId);

	Optional<AppointmentBo> getAppointmentById(Integer appointmentId);

	Integer getRecurringAppointmentQuantityByAppointmentParentId(Integer appointmentParentId);

	Short getAppointmentModalityById(Integer appointmentId);

}
