package net.pladema.medicalconsultation.appointment.service;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentDailyAmountBo;

import java.util.Collection;

public interface AppointmentDailyAmountService {

    Collection<AppointmentDailyAmountBo> getDailyAmounts(Integer diaryId);

}
