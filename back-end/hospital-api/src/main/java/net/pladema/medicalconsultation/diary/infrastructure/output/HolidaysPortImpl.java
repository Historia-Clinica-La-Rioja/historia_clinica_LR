package net.pladema.medicalconsultation.diary.infrastructure.output;

import ar.lamansys.sgh.shared.infrastructure.input.service.diary.holidays.SharedHolidayDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.diary.holidays.SharedHolidaysPort;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.AllArgsConstructor;
import net.pladema.medicalconsultation.diary.service.HolidaysService;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class HolidaysPortImpl implements SharedHolidaysPort {

	private HolidaysService holidaysService;
	private LocalDateMapper localDateMapper;

	@Override
	public List<SharedHolidayDto> getHolidays(LocalDate start, LocalDate end) {
		return holidaysService.getHolidays(start, end).stream()
				.map(holiday -> SharedHolidayDto.builder()
						.date(localDateMapper.toDateDto(holiday.getDate()))
						.description(holiday.getDescription())
						.build())
				.collect(Collectors.toList());
	}
}
