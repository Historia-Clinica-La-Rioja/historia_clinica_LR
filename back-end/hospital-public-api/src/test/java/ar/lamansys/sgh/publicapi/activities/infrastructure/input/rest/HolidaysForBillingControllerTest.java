package ar.lamansys.sgh.publicapi.activities.infrastructure.input.rest;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.publicapi.activities.holidays.application.FetchHolidaysForBilling;
import ar.lamansys.sgh.publicapi.activities.holidays.application.exception.FetchHolidaysForBillingAccessException;
import ar.lamansys.sgh.publicapi.activities.holidays.application.exception.JustOneDateException;
import ar.lamansys.sgh.publicapi.activities.holidays.application.exception.WrongDatesException;
import ar.lamansys.sgh.publicapi.activities.holidays.infrastructure.input.rest.HolidaysForBillingController;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.diary.holidays.SharedHolidayDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.diary.holidays.SharedHolidaysPort;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapperImpl;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;

@ExtendWith(MockitoExtension.class)
public class HolidaysForBillingControllerTest {
	@Mock
	private SharedHolidaysPort sharedHolidaysPort;

	@Mock
	private ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	private HolidaysForBillingController holidaysForBillingController;

	private final String MOTIVE = "Día del trabajador";

	@BeforeEach
	public void setUp() {
		LocalDateMapperImpl localDateMapper = new LocalDateMapperImpl();
		FetchHolidaysForBilling fetchHolidaysForBilling = new FetchHolidaysForBilling(sharedHolidaysPort, localDateMapper, activitiesPublicApiPermissions);
		holidaysForBillingController = new HolidaysForBillingController(fetchHolidaysForBilling);
	}

	@Test
	void successWithDefaultDates() {
		allowAccessPermission(true);
		when(sharedHolidaysPort.getHolidays(LocalDate.of(LocalDate.now().getYear(), 1, 1), LocalDate.of(LocalDate.now().getYear(), 12, 31)))
				.thenReturn(List.of(SharedHolidayDto.builder()
						.date(new DateDto(LocalDate.now().getYear(), 5, 1))
						.description(MOTIVE)
						.build()));
		var result = holidaysForBillingController.getHolidays(null, null);
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(MOTIVE, result.stream().findFirst().get().getDescription());
	}

	@Test
	void successWithGivenDates() {
		allowAccessPermission(true);
		when(sharedHolidaysPort.getHolidays(LocalDate.of(2024, 1, 1),
				LocalDate.of(2024, 12, 31)))
				.thenReturn(List.of(SharedHolidayDto.builder()
						.date(new DateDto(2024, 5, 1))
						.description(MOTIVE)
						.build()));
		var result = holidaysForBillingController.getHolidays("2024-01-01", "2024-12-31");
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(MOTIVE, result.stream().findFirst().get().getDescription());
	}

	@Test
	void failureWithJustOneDate() {
		allowAccessPermission(true);
		TestUtils.shouldThrow(JustOneDateException.class,
				() -> holidaysForBillingController.getHolidays(null, "2024-12-31"));
		TestUtils.shouldThrow(JustOneDateException.class,
				() -> holidaysForBillingController.getHolidays("2024-01-01", null));
	}

	@Test
	void failureAccessDenied() {
		allowAccessPermission(false);
		TestUtils.shouldThrow(FetchHolidaysForBillingAccessException.class,
				() -> holidaysForBillingController.getHolidays(null, null));
		TestUtils.shouldThrow(FetchHolidaysForBillingAccessException.class,
				() -> holidaysForBillingController.getHolidays("2024-01-01", "2024-12-31"));
	}

	@Test
	void failureWrongDatesInterval() {
		allowAccessPermission(true);
		TestUtils.shouldThrow(WrongDatesException.class,
				() -> holidaysForBillingController.getHolidays("2024-01-01", "2023-12-12"));
	}

	private void allowAccessPermission(boolean canAccess) {
		when(activitiesPublicApiPermissions.canFetchHolidays())
				.thenReturn(canAccess);
	}
}
