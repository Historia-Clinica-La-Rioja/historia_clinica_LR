package net.pladema.medicalconsultation.appointment.service;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface AppointmentService {

    Collection<AppointmentBo> getAppointmentsByDiaries(List<Integer> diaryIds);

    boolean existAppointment(Integer diaryId, Integer openingHoursId, LocalDate date, LocalTime hour);

    boolean updateState(Integer appointmentId, short appointmentStateId, Integer userId);
}
