package net.pladema.medicalconsultation.appointment.service.ports;

import net.pladema.medicalconsultation.appointment.service.domain.WeekDayBo;

import java.util.List;

public interface WeekDayStorage {

	List<WeekDayBo> getWeekDay();
}
