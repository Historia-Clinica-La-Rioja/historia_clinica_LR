package ar.lamansys.sgh.publicapi.activities.holidays.application;

import java.time.LocalDate;
import java.util.Collection;

import ar.lamansys.sgh.publicapi.activities.holidays.application.exception.WrongDatesException;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.activities.holidays.application.exception.FetchHolidaysForBillingAccessException;
import ar.lamansys.sgh.publicapi.activities.holidays.application.exception.JustOneDateException;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.diary.holidays.SharedHolidayDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.diary.holidays.SharedHolidaysPort;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FetchHolidaysForBilling {

	private SharedHolidaysPort sharedHolidaysPort;
	private LocalDateMapper localDateMapper;
	private ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	public Collection<SharedHolidayDto> fetch(String from, String to) {

		assertUserCanAccess();
		assertTwoDates(from, to);

		var localDateFrom = from != null ? localDateMapper.fromStringToLocalDate(from)
				: LocalDate.of(LocalDate.now().getYear(), 1, 1);
		var localDateTo = to != null ? localDateMapper.fromStringToLocalDate(to)
				: LocalDate.of(LocalDate.now().getYear(), 12, 31);

		assertValidInterval(localDateFrom, localDateTo);

		return sharedHolidaysPort.getHolidays(localDateFrom, localDateTo);

	}

	private void assertValidInterval(LocalDate localDateFrom, LocalDate localDateTo) {
		if(localDateTo.isBefore(localDateFrom)) {
			throw new WrongDatesException();
		}
	}

	private void assertUserCanAccess() {
		if (!activitiesPublicApiPermissions.canFetchHolidays()) {
			throw new FetchHolidaysForBillingAccessException();
		}
	}
	
	private void assertTwoDates(String from, String to) {
		if(from == null ^ to == null) {
			throw new JustOneDateException();
		}		
	}

}
