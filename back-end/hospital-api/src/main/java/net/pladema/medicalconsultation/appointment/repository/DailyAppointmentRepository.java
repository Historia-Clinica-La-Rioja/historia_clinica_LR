package net.pladema.medicalconsultation.appointment.repository;

import net.pladema.medicalconsultation.appointment.repository.domain.DailyAppointmentVo;

import java.time.LocalDate;
import java.util.List;

public interface DailyAppointmentRepository {

	List<DailyAppointmentVo> getDailyAppointmentsByDiaryIdAndDate(Integer institutionId, Integer diaryId, LocalDate date);

}
