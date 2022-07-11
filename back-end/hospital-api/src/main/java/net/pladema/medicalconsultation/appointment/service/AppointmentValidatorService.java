package net.pladema.medicalconsultation.appointment.service;

import java.time.LocalDate;
import java.time.LocalTime;

public interface AppointmentValidatorService {

    boolean validateStateUpdate(Integer institutionId, Integer appointmentId, short appointmentStateId, String reason);

	boolean validateDateUpdate(Integer institutionId, Integer appointmentId, LocalDate date, LocalTime time);
}
