package net.pladema.medicalconsultation.appointment.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.service.FetchWeekDay;
import net.pladema.medicalconsultation.appointment.service.domain.WeekDayBo;
import net.pladema.medicalconsultation.appointment.service.ports.WeekDayStorage;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FetchWeekDayImpl implements FetchWeekDay {

	private final WeekDayStorage weekDayStorage;

	public static final String OUTPUT = "Output -> {}";

	public FetchWeekDayImpl(WeekDayStorage weekDayStorage) {
		this.weekDayStorage = weekDayStorage;
	}

	@Override
	public List<WeekDayBo> run() {
		List<WeekDayBo> result = weekDayStorage.getWeekDay();
		log.debug(OUTPUT, result);
		return result;
	}
}
