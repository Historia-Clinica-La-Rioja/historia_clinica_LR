package net.pladema.medicalconsultation.diary.service.impl;

import net.pladema.establishment.repository.HolidayRepository;
import net.pladema.establishment.repository.domain.HolidayVo;
import net.pladema.medicalconsultation.diary.service.HolidaysService;
import net.pladema.medicalconsultation.diary.service.domain.HolidayBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HolidaysServiceImpl implements HolidaysService {

	private HolidayRepository holidayRepository;
	private static final String OUTPUT = "Output -> {}";
	private static final Logger LOG = LoggerFactory.getLogger(HolidaysServiceImpl.class);

	public HolidaysServiceImpl(HolidayRepository holidayRepository) {
		this.holidayRepository = holidayRepository;
	}

	@Override
	public List<HolidayBo> getHolidays(LocalDate startDate, LocalDate endDate) {
		List<HolidayBo> result = holidayRepository.getHolidays(startDate, endDate).stream().map(this::generateHolidayBo).collect(Collectors.toList());
		LOG.debug(OUTPUT, result);
		return result;
	}

	private HolidayBo generateHolidayBo(HolidayVo holiday) {
		HolidayBo result = new HolidayBo();
		result.setDate(holiday.getDate());
		result.setDescription(holiday.getDescription());
		return result;
	}

}
