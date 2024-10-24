package net.pladema.medicalconsultation.appointment.application.port;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

import java.util.Optional;

public interface AppointmentPort {

	void updateAppointmentState(Integer appointmentId, Short appointmentStatusId);

	Integer getAppointmentParentId(Integer appointmentId);

	Optional<AppointmentBo> getAppointmentById(Integer appointmentId);

	Integer getRecurringAppointmentQuantityByAppointmentParentId(Integer appointmentParentId);

	Short getAppointmentModalityById(Integer appointmentId);

	void deleteAppointmentById(Integer appointmentId);

	void updateOpeningHoursId(Integer appointmentId, Integer openingHoursId);

}
