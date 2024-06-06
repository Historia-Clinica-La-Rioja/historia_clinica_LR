package net.pladema.medicalconsultation.appointment.infraestructure.output.repository.appointment;

import ar.lamansys.sgx.shared.dates.repository.DayWeekRepository;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.controller.mapper.WeekDayMapper;
import net.pladema.medicalconsultation.appointment.service.domain.WeekDayBo;
import net.pladema.medicalconsultation.appointment.service.ports.WeekDayStorage;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WeekDayStorageImpl implements WeekDayStorage {

	private final DayWeekRepository dayWeekRepository;

	private final WeekDayMapper weekDayMapper;

	public static final String OUTPUT = "Output -> {}";

	public WeekDayStorageImpl(DayWeekRepository dayWeekRepository, WeekDayMapper weekDayMapper) {
		this.dayWeekRepository = dayWeekRepository;
		this.weekDayMapper = weekDayMapper;
	}

	@Override
	public List<WeekDayBo> getWeekDay() {
		List<WeekDayBo> result = dayWeekRepository.findAll()
				.stream()
				.map(weekDayMapper::toWeekDayBo)
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}
}
