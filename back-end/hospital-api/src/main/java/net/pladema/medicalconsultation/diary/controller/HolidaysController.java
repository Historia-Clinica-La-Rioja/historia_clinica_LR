package net.pladema.medicalconsultation.diary.controller;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.controller.mapper.HolidayMapper;
import net.pladema.medicalconsultation.diary.controller.dto.HolidayDto;
import net.pladema.medicalconsultation.diary.service.HolidaysService;

import net.pladema.medicalconsultation.diary.service.domain.HolidayBo;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/holidays")
@Tag(name = "Holidays", description = "Holidays")
@Validated
public class HolidaysController {

	public static final String OUTPUT = "Output -> {}";

	private final HolidaysService holidaysService;
	private final HolidayMapper holidayMapper;
	private final LocalDateMapper localDateMapper;

	public HolidaysController(
			HolidaysService holidaysService,
			HolidayMapper holidayMapper,
			LocalDateMapper localDateMapper
	) {
		this.holidaysService = holidaysService;
		this.holidayMapper = holidayMapper;
		this.localDateMapper = localDateMapper;
	}

	@GetMapping
	public ResponseEntity<List<HolidayDto>> getHolidays(
			@RequestParam(name = "startDate", required = false) String startDate,
			@RequestParam(name = "endDate", required = false) String endDate
	){
		log.debug("Input parameters -> startDate {}, endDate {}", startDate, endDate);
		LocalDate startLocalDate = (startDate != null) ? localDateMapper.fromStringToLocalDate(startDate) : null;
		LocalDate endLocalDate = (endDate != null) ? localDateMapper.fromStringToLocalDate(endDate) : null;
		List<HolidayBo> holidays = holidaysService.getHolidays(startLocalDate, endLocalDate);
		List<HolidayDto> result = holidayMapper.toListHolidayDto(holidays);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

}
