package net.pladema.medicalconsultation.appointment.controller.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import ar.lamansys.sgx.shared.dates.repository.entity.DayWeek;
import net.pladema.medicalconsultation.appointment.controller.dto.WeekDayDto;

import net.pladema.medicalconsultation.appointment.service.domain.WeekDayBo;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface WeekDayMapper {

	@Named("toWeekDayDto")
	WeekDayDto toWeekDayDto(WeekDayBo weekDayBo);

	@Named("towWeekDayBo")
	WeekDayBo toWeekDayBo(DayWeek dayWeek);
}
