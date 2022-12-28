package net.pladema.medicalconsultation.appointment.service;

import net.pladema.medicalconsultation.appointment.service.domain.WeekDayBo;

import java.util.List;

public interface FetchWeekDay {

	List<WeekDayBo> run();
}
