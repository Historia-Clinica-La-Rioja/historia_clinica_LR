package net.pladema.medicalconsultation.appointment.application.port;

public interface AppointmentPort {

	void updateAppointmentState(Integer appointmentId, Short appointmentStatusId);

	Integer getAppointmentParentId(Integer appointmentId);

	Short getAppointmentStateIdByAppointmentId(Integer appointmentId);
}
