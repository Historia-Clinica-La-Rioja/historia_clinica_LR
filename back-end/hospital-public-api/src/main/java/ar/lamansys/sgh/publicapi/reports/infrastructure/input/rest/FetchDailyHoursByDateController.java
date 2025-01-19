package ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.FetchDailyHoursByDate;
import ar.lamansys.sgh.publicapi.reports.domain.fetchdailyhoursbydate.DayReportBo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Tag(name = "PublicApi Reportes", description = "Public Api General Reports")
@RequestMapping("/public-api/reports/daily-hours")
@RestController
public class FetchDailyHoursByDateController {

	private FetchDailyHoursByDate fetchDailyHours;

	@GetMapping
	public @ResponseBody List<DayReportBo> fetchDailyHoursByDate(
			@RequestParam(name = "dateFrom") String dateFrom,
			@RequestParam(name = "dateUntil") String dateUntil,
			@RequestParam(name = "institutionRefsetCode", required = false) String institutionRefsetCode,
			@RequestParam(name = "hierarchicalUnitId", required = false) Integer hierarchicalUnitId) {
		log.debug("Input parameters -> dateFrom {}, dateUntil{}, refsetCode {}, hierarchicalUnitId {}",
				dateFrom, dateUntil, institutionRefsetCode, hierarchicalUnitId);
		var result = fetchDailyHours.run(dateFrom, dateUntil, institutionRefsetCode, hierarchicalUnitId);
		log.debug("Daily hours By Date {}", result);
		return result;
	}

}
