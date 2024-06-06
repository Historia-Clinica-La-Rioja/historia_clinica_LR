package net.pladema.medicalconsultation.appointment.service;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

import java.time.LocalDate;

public interface CreateEveryWeekAppointmentService {

	boolean execute(AppointmentBo dto, LocalDate diaryEndDate);
}
