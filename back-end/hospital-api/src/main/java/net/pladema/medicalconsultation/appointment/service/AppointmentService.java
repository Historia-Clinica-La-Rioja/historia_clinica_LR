package net.pladema.medicalconsultation.appointment.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

public interface AppointmentService {

    Optional<AppointmentBo> getAppointment(Integer appointmentId);
	
    Collection<AppointmentBo> getAppointmentsByDiaries(List<Integer> diaryIds);

    boolean existAppointment(Integer diaryId, Integer openingHoursId, LocalDate date, LocalTime hour);
    
    Collection<AppointmentBo> getFutureActiveAppointmentsByDiary(Integer diaryId);

    boolean updateState(Integer appointmentId, short appointmentStateId, Integer userId, String reason);

    boolean hasConfirmedAppointment(Integer patientId, Integer healthcareProfessionalId);

    List<Integer> getAppointmentsId(Integer patientId, Integer healthcareProfessionalId);

    boolean updatePhoneNumber(Integer appointmentId, String phoneNumber, Integer userId);
}
