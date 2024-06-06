package net.pladema.medicalconsultation.appointment.service;

public interface CancelRecurringAppointment {

	boolean execute(Integer appointmentId, Boolean cancelAllAppointments);

	boolean execute(Integer appointmentId);
}
