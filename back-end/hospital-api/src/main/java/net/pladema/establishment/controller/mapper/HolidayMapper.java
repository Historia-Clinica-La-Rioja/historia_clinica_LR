package net.pladema.establishment.controller.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import net.pladema.medicalconsultation.diary.controller.dto.HolidayDto;

import net.pladema.medicalconsultation.diary.service.domain.HolidayBo;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface HolidayMapper {

	@Named("toHolidayDto")
	HolidayDto toHolidayDto(HolidayBo holidayBo);

	@Named("toListHolidayDto")
	@IterableMapping(qualifiedByName = "toHolidayDto")
	List<HolidayDto> toListHolidayDto(List<HolidayBo> holidayBoList);

}
