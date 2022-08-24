package net.pladema.medicalconsultation.appointment.service;

import java.time.LocalDate;
import java.util.Collection;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentDailyAmountBo;

public interface AppointmentDailyAmountService {

	Collection<AppointmentDailyAmountBo> getDailyAmounts(Integer diaryId, LocalDate from, LocalDate to);

}
