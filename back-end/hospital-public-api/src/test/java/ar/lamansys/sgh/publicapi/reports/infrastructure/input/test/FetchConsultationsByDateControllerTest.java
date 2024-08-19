package ar.lamansys.sgh.publicapi.reports.infrastructure.input.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ar.lamansys.sgh.publicapi.reports.domain.fetchconsultationsbydate.ConsultationItemWithDateBo;

import ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.dto.ConsultationDto;

import ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.mapper.FetchConsultationsByDateMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.publicapi.activities.domain.datetimeutils.DateBo;
import ar.lamansys.sgh.publicapi.activities.domain.datetimeutils.DateTimeBo;
import ar.lamansys.sgh.publicapi.activities.domain.datetimeutils.TimeBo;
import ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.FetchConsultationsByDate;
import ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.exceptions.FetchConsultationsAccessDeniedException;
import ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.exceptions.InstitutionNotFoundException;
import ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.exceptions.WrongDateFilterException;
import ar.lamansys.sgh.publicapi.reports.application.port.out.ConsultationsByDateStorage;
import ar.lamansys.sgh.publicapi.reports.domain.ClinicalSpecialtyBo;
import ar.lamansys.sgh.publicapi.reports.domain.IdentificationBo;
import ar.lamansys.sgh.publicapi.reports.domain.MedicalCoverageBo;
import ar.lamansys.sgh.publicapi.reports.domain.fetchconsultationsbydate.ConsultationBo;
import ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.FetchConsultationsByDateController;
import ar.lamansys.sgh.publicapi.reports.infrastructure.input.service.ConsultationsByDatePublicApiPermissions;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

@ExtendWith(MockitoExtension.class)
public class FetchConsultationsByDateControllerTest {

	private FetchConsultationsByDateController fetchConsultationsByDateController;

	@Mock
	private ConsultationsByDateStorage consultationsByDateStorage;

	@Mock
	private ConsultationsByDatePublicApiPermissions consultationsByDatePublicApiPermissions;

	@Mock
	private LocalDateMapper localDateMapper;

	@BeforeEach
	public void setup() {
		FetchConsultationsByDate fetchConsultationsByDate =
				new FetchConsultationsByDate(consultationsByDateStorage, consultationsByDatePublicApiPermissions, localDateMapper);
		this.fetchConsultationsByDateController =
				new FetchConsultationsByDateController(fetchConsultationsByDate);
	}

	@Test
	void errorWrongDateIntervalException(){
		allowAccessPermission(true);
		when(localDateMapper.fromStringToLocalDate("2024-02-03")).thenReturn(LocalDate.of(2024, 2, 3));
		when(localDateMapper.fromStringToLocalDate("2024-03-06")).thenReturn(LocalDate.of(2024, 3, 6));
		TestUtils.shouldThrow(WrongDateFilterException.class,
				() -> fetchConsultationsByDateController.fetchConsultationsByInstitution(
						"2024-02-03",
						"2024-03-06",
						null,
						null
				));
	}

	@Test
	void errorInstitutionNotFoundException(){
		allowAccessPermission(true);
		when(consultationsByDatePublicApiPermissions.getInstitutionId(any())).thenReturn(Optional.empty());
		TestUtils.shouldThrow(InstitutionNotFoundException.class,
				() -> fetchConsultationsByDateController.fetchConsultationsByInstitution(
						"2024-02-03",
						"2024-03-03",
						"refset",
						null
				));
	}

	@Test
	void fetchConsultationsByDateDeniedAccessException() {
		allowAccessPermission(false);
		TestUtils.shouldThrow(FetchConsultationsAccessDeniedException.class,
				() -> fetchConsultationsByDateController.fetchConsultationsByInstitution(
						"2024-02-03",
						"2024-02-04",
						null,
						null)
		);
	}

	@Test
	void successFetchConsultations(){
		allowAccessPermission(true);
		var mockedResult = mockResult();
		when(consultationsByDateStorage.fetchConsultations(
				LocalDate.of(2024, 2, 3),
				LocalDate.of(2024, 2, 4),
				null,
				null)).thenReturn(mockedResult);

		when(localDateMapper.fromStringToLocalDate("2024-02-03")).thenReturn(LocalDate.of(2024, 2, 3));
		when(localDateMapper.fromStringToLocalDate("2024-02-04")).thenReturn(LocalDate.of(2024, 2, 4));

		var result = fetchConsultationsByDateController.fetchConsultationsByInstitution(
				"2024-02-03",
				"2024-02-04",
				null,
				null
		);

		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(result.size(), 1);
		Assertions.assertEquals(result.get(0), FetchConsultationsByDateMapper.fromBo(mockResult().get(0)));
	}

	private void allowAccessPermission(boolean canAccess) {
		when(consultationsByDatePublicApiPermissions.canFetchConsultations())
				.thenReturn(canAccess);
	}

	private List<ConsultationDto> mapToDto(List<ConsultationBo> consultations) {
		return	consultations.stream()
				.map(FetchConsultationsByDateMapper::fromBo)
				.collect(Collectors.toList());
	}

	private List<ConsultationBo> mockResult(){
		return List.of(new ConsultationBo(
				1,
				1,
				new DateTimeBo(new DateBo(2024, 2,3), new TimeBo(12,0,0)),
				null,
				null,
				null,
				null,
				new ClinicalSpecialtyBo(1, "Clínica médica", "11111111"),
				new IdentificationBo("DNI", "33333333"),
				new MedicalCoverageBo("Prueba", 123),
				null,
				new DateBo(1988, 5,22),
				null,
				null,
				null,
				null,
				null,
				List.of(new ConsultationItemWithDateBo(
						"100",
						"Item",
						"CIE10",
						LocalDate.of(2024, 8, 19),
						LocalDate.of(2024, 8, 19)
				)),
				List.of(),
				List.of()
		));
	}
}
