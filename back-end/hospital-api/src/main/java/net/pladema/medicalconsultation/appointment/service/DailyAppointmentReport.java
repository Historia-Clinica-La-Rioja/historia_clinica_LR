package net.pladema.medicalconsultation.appointment.service;

import net.pladema.medicalconsultation.appointment.service.domain.AttentionTypeReportBo;

import java.time.LocalDate;
import java.util.List;

public interface DailyAppointmentReport {

    List<AttentionTypeReportBo> execute(Integer institutionId, Integer diaryId, LocalDate date);

}
