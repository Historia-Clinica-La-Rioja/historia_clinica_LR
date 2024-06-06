package net.pladema.medicalconsultation.appointment.service;
import net.pladema.medicalconsultation.appointment.service.domain.CreateCustomAppointmentBo;

import java.time.LocalDate;
import java.time.LocalTime;

public interface AppointmentValidatorService {

    boolean validateStateUpdate(Integer institutionId, Integer appointmentId, short appointmentStateId, String reason);

	boolean validateDateUpdate(Integer institutionId, Integer appointmentId, LocalDate date, LocalTime time);

	LocalDate checkAppointmentEveryWeek(String hour, String date, Integer diaryId, Integer appointmentId, Short recurringAppointmentOption, Integer openingHoursId);

	void checkCustomAppointment(CreateCustomAppointmentBo bo);
}
