package ar.lamansys.sgh.publicapi.activities.holidays.infrastructure.input.rest;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.activities.holidays.application.FetchHolidaysForBilling;
import ar.lamansys.sgh.shared.infrastructure.input.service.diary.holidays.SharedHolidayDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/public-api/activities/holidays")
@Tag(name = "PublicApi Facturacion", description = "Public Api Holidays for Billing")
public class HolidaysForBillingController {

	private FetchHolidaysForBilling fetchHolidaysForBilling;


	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Collection<SharedHolidayDto> getHolidays(
		@RequestParam(name = "from", required = false) String from,
		@RequestParam(name = "to", required = false) String to
	) {
		log.debug("Fetching holidays from {} to {}", from, to);
		var result = fetchHolidaysForBilling.fetch(from, to);
		log.debug("result => {}", result);
		return result;
	}
}
