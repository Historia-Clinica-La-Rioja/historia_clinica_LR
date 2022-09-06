package net.pladema.medicalconsultation.diary.service;

import net.pladema.medicalconsultation.diary.service.domain.HolidayBo;

import java.time.LocalDate;
import java.util.List;

public interface HolidaysService {

	List<HolidayBo> getHolidays(LocalDate startDate, LocalDate endDate);

}
