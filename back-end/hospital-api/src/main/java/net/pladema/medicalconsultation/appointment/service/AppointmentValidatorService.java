package net.pladema.medicalconsultation.appointment.service;

public interface AppointmentValidatorService {

    boolean validateStateUpdate(Integer institutionId, Integer appointmentId, short appointmentStateId, String reason);
}
