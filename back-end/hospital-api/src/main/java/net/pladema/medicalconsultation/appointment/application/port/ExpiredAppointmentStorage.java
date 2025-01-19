package net.pladema.medicalconsultation.appointment.application.port;

public interface ExpiredAppointmentStorage {

	void save(Integer appointmentId, Short reasonTypeId, String reason);

}
