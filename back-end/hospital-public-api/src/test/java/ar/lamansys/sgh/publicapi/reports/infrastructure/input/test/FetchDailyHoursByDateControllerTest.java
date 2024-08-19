package ar.lamansys.sgh.publicapi.reports.infrastructure.input.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.exceptions.FetchDailyHoursByDateAccessDeniedException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.FetchDailyHoursByDate;
import ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.exceptions.FetchConsultationsAccessDeniedException;
import ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.exceptions.InstitutionNotFoundException;
import ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.exceptions.WrongDateFilterException;
import ar.lamansys.sgh.publicapi.reports.application.port.out.FetchDailyHoursStorage;
import ar.lamansys.sgh.publicapi.reports.domain.ClinicalSpecialtyBo;
import ar.lamansys.sgh.publicapi.reports.domain.HierarchicalUnitBo;
import ar.lamansys.sgh.publicapi.reports.domain.fetchdailyhoursbydate.DailyHoursBo;
import ar.lamansys.sgh.publicapi.reports.domain.fetchdailyhoursbydate.DayReportBo;
import ar.lamansys.sgh.publicapi.reports.domain.fetchdailyhoursbydate.ProfessionalDataBo;
import ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.FetchDailyHoursByDateController;
import ar.lamansys.sgh.publicapi.reports.infrastructure.input.service.DailyHoursByDatePublicApiPermissions;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

@ExtendWith(MockitoExtension.class)
public class FetchDailyHoursByDateControllerTest {

	private FetchDailyHoursByDateController fetchDailyHoursByDateController;

	@Mock
	private FetchDailyHoursStorage fetchDailyHoursStorage;

	@Mock
	private DailyHoursByDatePublicApiPermissions dailyHoursByDatePublicApiPermissions;

	@Mock
	private LocalDateMapper localDateMapper;

	@BeforeEach
	public void setup() {
		FetchDailyHoursByDate fetchDailyHoursByDate =
				new FetchDailyHoursByDate(fetchDailyHoursStorage, localDateMapper, dailyHoursByDatePublicApiPermissions);
		this.fetchDailyHoursByDateController =
				new FetchDailyHoursByDateController(fetchDailyHoursByDate);
	}

	@Test
	void errorWrongDateIntervalException(){
		allowAccessPermission(true);
		when(localDateMapper.fromStringToLocalDate("2024-02-03")).thenReturn(LocalDate.of(2024, 2, 3));
		when(localDateMapper.fromStringToLocalDate("2024-03-06")).thenReturn(LocalDate.of(2024, 3, 6));
		TestUtils.shouldThrow(WrongDateFilterException.class,
				() -> fetchDailyHoursByDateController.fetchDailyHoursByDate(
						"2024-02-03",
						"2024-03-06",
						null,
						null
				));
	}

	@Test
	void errorInstitutionNotFoundException(){
		allowAccessPermission(true);
		when(dailyHoursByDatePublicApiPermissions.getInstitutionId(any())).thenReturn(Optional.empty());
		TestUtils.shouldThrow(InstitutionNotFoundException.class,
				() -> fetchDailyHoursByDateController.fetchDailyHoursByDate(
						"2024-02-03",
						"2024-03-03",
						"refset",
						null
				));
	}

	@Test
	void fetchDailyHoursByDateDeniedAccessException() {
		allowAccessPermission(false);
		TestUtils.shouldThrow(FetchDailyHoursByDateAccessDeniedException.class,
				() -> fetchDailyHoursByDateController.fetchDailyHoursByDate(
						"2024-02-03",
						"2024-02-04",
						null,
						null)
		);
	}

	@Test
	void successFetchDailyHoursByDate(){
		allowAccessPermission(true);
		when(fetchDailyHoursStorage.fetchDiaryHoursByDay(
				LocalDate.of(2024, 2, 3).toString(),
				LocalDate.of(2024, 2, 4).toString(),
				null,
				null)).thenReturn(mockResult());

		when(localDateMapper.fromStringToLocalDate("2024-02-03")).thenReturn(LocalDate.of(2024, 2, 3));
		when(localDateMapper.fromStringToLocalDate("2024-02-04")).thenReturn(LocalDate.of(2024, 2, 4));

		var result = fetchDailyHoursByDateController.fetchDailyHoursByDate(
				"2024-02-03",
				"2024-02-04",
				null,
				null
		);

		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(result.size(), 1);
	}

	private void allowAccessPermission(boolean canAccess) {
		when(dailyHoursByDatePublicApiPermissions.canFetchDailyHoursByDate())
				.thenReturn(canAccess);
	}

	private List<DayReportBo> mockResult(){
		return List.of(DayReportBo.builder()
						.day(LocalDate.of(2024,8,20))
						.dailyHours(List.of(DailyHoursBo.builder()
									.diaryId(1)
									.institutionCode("refset")
									.institutionName("institution")
									.hierarchicalUnit(new HierarchicalUnitBo(
											1, "HU", (short)8
									))
									.serviceHierarchicalUnit(new HierarchicalUnitBo(
											1, "HU", (short)8
									))
									.professionalCuil("2033333338")
									.professionalData(
										ProfessionalDataBo.builder()
												.id(1)
												.identificationNumber("33333333")
												.identificationType("DNI")
												.cuil("2033333338")
												.lastName("lastName")
												.middleNames("middleName")
												.firstName("firstName")
												.selfPerceivedName("selfPerceivedName")
												.build()
									)
									.clinicalSpecialty(
										ClinicalSpecialtyBo.builder()
											.id(1)
											.description("Specialty")
											.snomedId("1")
											.build())
									.diaryType("diaryType")
									.interruptions((long)2)
									.possibleAppointments((long)30)
									.minutesInAppointments((long)45)
									.interruptionsDescriptions(List.of("interruption"))
									.build()
						)).build()
				);
	}
}
