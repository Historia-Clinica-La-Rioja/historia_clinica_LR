package ar.lamansys.sgh.publicapi.reports.application.fetchconsultations;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.exceptions.FetchDailyHoursByDateAccessDeniedException;
import ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.exceptions.InstitutionNotFoundException;
import ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.exceptions.WrongDateFilterException;
import ar.lamansys.sgh.publicapi.reports.application.port.out.FetchDailyHoursStorage;
import ar.lamansys.sgh.publicapi.reports.domain.fetchdailyhoursbydate.DayReportBo;
import ar.lamansys.sgh.publicapi.reports.infrastructure.input.service.DailyHoursByDatePublicApiPermissions;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class FetchDailyHoursByDate {

	private FetchDailyHoursStorage fetchDailyHoursStorage;
	private LocalDateMapper localDateMapper;
	private DailyHoursByDatePublicApiPermissions dailyHoursByDatePublicApiPermissions;

	public List<DayReportBo> run(String dateFrom, String dateUntil, String refsetCode, Integer hierarchicalUnitId) {

		log.debug("Checking permissions");
		assertUserCanAccess();

		log.debug("Find institutionId from refsetCode {}", refsetCode);
		Integer institutionId = refsetCode != null ? findInstitutionId(refsetCode) : null;

		var localDateFrom = localDateMapper.fromStringToLocalDate(dateFrom);
		var localDateUntil = localDateMapper.fromStringToLocalDate(dateUntil);

		assertDates(localDateFrom, localDateUntil);

		return fetchDailyHoursStorage.fetchDiaryHoursByDay(dateFrom, dateUntil, institutionId, hierarchicalUnitId);
	}

	private void assertDates(LocalDate localDateFrom, LocalDate localDateUntil) {
		if(localDateUntil.minusDays(30).isAfter(localDateFrom))
			throw new WrongDateFilterException();
	}

	private void assertUserCanAccess() {
		if(!dailyHoursByDatePublicApiPermissions.canFetchDailyHoursByDate()) {
			throw new FetchDailyHoursByDateAccessDeniedException();
		}
	}

	private Integer findInstitutionId(String refsetCode) {
		return dailyHoursByDatePublicApiPermissions.getInstitutionId(refsetCode)
				.orElseThrow(InstitutionNotFoundException::new);
	}
}
