package ar.lamansys.sgh.shared.infrastructure.input.service.diary.holidays;

import java.time.LocalDate;
import java.util.List;

public interface SharedHolidaysPort {
	List<SharedHolidayDto> getHolidays(LocalDate start, LocalDate end);
}
