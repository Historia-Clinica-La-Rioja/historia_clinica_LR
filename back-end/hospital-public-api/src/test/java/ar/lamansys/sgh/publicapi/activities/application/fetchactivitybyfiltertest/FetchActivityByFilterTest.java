package ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyfiltertest;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.publicapi.activities.application.fetchactivitiesbyfilter.exception.ActivityByFilterAccessDeniedException;
import ar.lamansys.sgh.publicapi.activities.domain.PersonInfoExtendedBo;

import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgh.publicapi.activities.application.fetchactivitiesbyfilter.ActivitySearchFilter;
import ar.lamansys.sgh.publicapi.activities.application.fetchactivitiesbyfilter.FetchActivitiesByFilter;
import ar.lamansys.sgh.publicapi.activities.application.port.out.ActivityStorage;
import ar.lamansys.sgh.publicapi.activities.application.port.out.AttentionReadStorage;
import ar.lamansys.sgh.publicapi.activities.domain.AttentionInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.CoverageActivityInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.GenderEnum;
import ar.lamansys.sgh.publicapi.activities.domain.InternmentBo;
import ar.lamansys.sgh.publicapi.activities.domain.PersonInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.ProfessionalBo;
import ar.lamansys.sgh.publicapi.activities.domain.ScopeEnum;
import ar.lamansys.sgh.publicapi.activities.domain.SingleDiagnosticBo;
import ar.lamansys.sgh.publicapi.activities.domain.SnomedBo;
import ar.lamansys.sgh.publicapi.activities.domain.SnomedCIE10Bo;
import ar.lamansys.sgh.publicapi.activities.domain.datetimeutils.DateBo;
import ar.lamansys.sgh.publicapi.activities.domain.datetimeutils.DateTimeBo;
import ar.lamansys.sgh.publicapi.activities.domain.datetimeutils.TimeBo;
@ExtendWith(MockitoExtension.class)
public class FetchActivityByFilterTest {

	private FetchActivitiesByFilter fetchActivitiesByFilter;

	@Mock
	private ActivityStorage activityStorage;

	@Mock
	private AttentionReadStorage attentionReadStorage;

	@Mock
	private ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	@BeforeEach
	void setup() {
		fetchActivitiesByFilter = new FetchActivitiesByFilter(activityStorage, activitiesPublicApiPermissions);
	}

	@Test
	void activitiesInstitution() {
		ActivitySearchFilter param = new ActivitySearchFilter("", "", LocalDate.ofYearDay(2020, 1), LocalDate.ofYearDay(2020, 20), false, null);

		when(activityStorage.getActivitiesByInstitution(param.getRefsetCode(), param.getFrom(), param.getTo(), param.getReprocessing())).thenReturn(
				Arrays.asList(
						new AttentionInfoBo(
								10L, 1L, LocalDate.ofYearDay(2020, 1),
								new SnomedBo("1", "1"),
								new PersonInfoBo("35555555", "Juan", "Perez", LocalDate.ofYearDay(1990, 1), GenderEnum.MALE),
								new CoverageActivityInfoBo(), ScopeEnum.AMBULATORIA,
								new InternmentBo("100", LocalDate.ofYearDay(2020, 1).atStartOfDay(), LocalDate.ofYearDay(2020, 20).atStartOfDay()),
								new ProfessionalBo(1, "Jose", "Fernandez", "DOC-30000000", "30000000"),
								new SingleDiagnosticBo(new SnomedCIE10Bo("1", "1", "1"), true, "1234345", "2345435", LocalDateTime.now()),
								new DateTimeBo(new DateBo(2020, 1, 1), new TimeBo(20, 30, 45)),
								new PersonInfoExtendedBo("Pedro", "Perez", "jp@gmail.com", "Juan", (short)1),
								null,
								true
						),
						new AttentionInfoBo(
								11L, 2L, LocalDate.ofYearDay(2020, 2),
								new SnomedBo("2", "2"),
								new PersonInfoBo("35555556", "Rocio", "Gonzalez", LocalDate.ofYearDay(1990, 1), GenderEnum.FEMALE),
								new CoverageActivityInfoBo(), ScopeEnum.INTERNACION,
								new InternmentBo("100", LocalDate.ofYearDay(2020, 1).atStartOfDay(), LocalDate.ofYearDay(2020, 20).atStartOfDay()),
								new ProfessionalBo(1, "Juan", "Perez", "DOC-30000000", "30000000"),
								new SingleDiagnosticBo(new SnomedCIE10Bo("1", "1", "1"), true, "1234345", "2345435", LocalDateTime.now()),
								new DateTimeBo(new DateBo(2020, 1, 1), new TimeBo(20, 30, 45)),
								new PersonInfoExtendedBo("Pedro", "Perez", "jp@gmail.com", "Juan", (short)1),
								null,
								true
						),
						new AttentionInfoBo(
								12L, 3L, LocalDate.ofYearDay(2020, 3),
								new SnomedBo("3", "3"),
								new PersonInfoBo("35555557", "Pedro", "Rodriguez", LocalDate.ofYearDay(1990, 1), GenderEnum.MALE),
								new CoverageActivityInfoBo(), ScopeEnum.AMBULATORIA,
								new InternmentBo("100", LocalDate.ofYearDay(2020, 1).atStartOfDay(), LocalDate.ofYearDay(2020, 20).atStartOfDay()),
								new ProfessionalBo(1, "Jose", "Fernandez", "DOC-30000000", "30000000"),
								new SingleDiagnosticBo(new SnomedCIE10Bo("1", "1", "1"), true, "1234345", "2345435", LocalDateTime.now()),
								new DateTimeBo(new DateBo(2020, 1, 1), new TimeBo(20, 30, 45)),
								new PersonInfoExtendedBo("Pedro", "Perez", "jp@gmail.com", "Juan", (short)1),
								null,
								true
						)
				)
		);

		when(activitiesPublicApiPermissions.findInstitutionId(anyString())).thenReturn(Optional.of(1));
		when(activitiesPublicApiPermissions.canAccessActivityByFilter(anyInt())).thenReturn(true);

		List<AttentionInfoBo> result = fetchActivitiesByFilter.run(param);
		Assertions.assertEquals(result.size(), 3);
	}

	@Test
	void activitiesInstitutionAndPatient() {
		ActivitySearchFilter param = new ActivitySearchFilter("", "35555555",
				LocalDate.ofYearDay(2020, 1), LocalDate.ofYearDay(2020, 20), false, null);

		when(activityStorage.getActivitiesByInstitutionAndPatient(param.getRefsetCode(), param.getIdentificationNumber(),
				param.getFrom(), param.getTo(), param.getReprocessing())).thenReturn(
				Arrays.asList(
						new AttentionInfoBo(
								10L, 1L, LocalDate.ofYearDay(2020, 1),
								new SnomedBo("1", "1"),
								new PersonInfoBo("35555555", "Juan", "Perez", LocalDate.ofYearDay(1990, 1), GenderEnum.MALE),
								new CoverageActivityInfoBo(), ScopeEnum.AMBULATORIA,
								new InternmentBo("100", LocalDate.ofYearDay(2020, 1).atStartOfDay(), LocalDate.ofYearDay(2020, 20).atStartOfDay()),
								new ProfessionalBo(1, "Jose", "Fernandez", "DOC-30000000", "30000000"),
								new SingleDiagnosticBo(new SnomedCIE10Bo("1", "1", "1"), true, "1234345", "2345435", LocalDateTime.now()),
								new DateTimeBo(new DateBo(2020, 1, 1), new TimeBo(20, 30, 45)),
								new PersonInfoExtendedBo("Pedro", "Perez", "jp@gmail.com", "Juan", (short)1),
								null,
								true
						)
				)
		);

		when(activitiesPublicApiPermissions.findInstitutionId(anyString())).thenReturn(Optional.of(1));
		when(activitiesPublicApiPermissions.canAccessActivityByFilter(anyInt())).thenReturn(true);

		List<AttentionInfoBo> result = fetchActivitiesByFilter.run(param);
		Assertions.assertEquals(result.size(), 1);

		Assertions.assertEquals(result.get(0).getPatient().getIdentificationNumber(), param.getIdentificationNumber());
	}

	@Test
	void activitiesInstitutionAndCoverage() {
		ActivitySearchFilter param = new ActivitySearchFilter("", "",
				LocalDate.ofYearDay(2020, 1), LocalDate.ofYearDay(2020, 20), false, "30-25555555-0");

		when(activityStorage.getActivitiesByInstitutionAndCoverage(param.getRefsetCode(), param.getCoverageCuit(), param.getFrom(), param.getTo(), param.getReprocessing())).thenReturn(
				Arrays.asList(
						new AttentionInfoBo(
								10L, 1L, LocalDate.ofYearDay(2020, 1),
								new SnomedBo("1", "1"),
								new PersonInfoBo("35555555", "Juan", "Perez", LocalDate.ofYearDay(1990, 1), GenderEnum.MALE),
								new CoverageActivityInfoBo(), ScopeEnum.AMBULATORIA,
								new InternmentBo("100", LocalDate.ofYearDay(2020, 1).atStartOfDay(), LocalDate.ofYearDay(2020, 20).atStartOfDay()),
								new ProfessionalBo(1, "Jose", "Fernandez", "DOC-30000000", "30000000"),
								new SingleDiagnosticBo(new SnomedCIE10Bo("1", "1", "1"), true, "1234345", "2345435", LocalDateTime.now()),
								new DateTimeBo(new DateBo(2020, 1, 1), new TimeBo(20, 30, 45)),
								new PersonInfoExtendedBo("Pedro", "Perez", "jp@gmail.com", "Juan", (short)1),
								null,
								true
						),
						new AttentionInfoBo(
								11L,2L, LocalDate.ofYearDay(2020, 2),
								new SnomedBo("2", "2"),
								new PersonInfoBo("35555556", "Rocio", "Gonzalez", LocalDate.ofYearDay(1990, 1), GenderEnum.FEMALE),
								new CoverageActivityInfoBo(), ScopeEnum.INTERNACION,
								new InternmentBo("100", LocalDate.ofYearDay(2020, 1).atStartOfDay(), LocalDate.ofYearDay(2020, 20).atStartOfDay()),
								new ProfessionalBo(1, "Juan", "Perez", "DOC-30000000", "30000000"),
								new SingleDiagnosticBo(new SnomedCIE10Bo("1", "1", "1"), true, "1234345", "2345435", LocalDateTime.now()),
								new DateTimeBo(new DateBo(2020, 1, 1), new TimeBo(20, 30, 45)),
								new PersonInfoExtendedBo("Pedro", "Perez", "jp@gmail.com", "Juan", (short)1),
								null,
								true
						)
				));

		when(activitiesPublicApiPermissions.findInstitutionId(anyString())).thenReturn(Optional.of(1));
		when(activitiesPublicApiPermissions.canAccessActivityByFilter(anyInt())).thenReturn(true);

		List<AttentionInfoBo> result = fetchActivitiesByFilter.run(param);
		Assertions.assertEquals(result.size(), 2);
	}

	@Test
	void testActivityByFilterAccessDeniedException(){
		ActivitySearchFilter param = new ActivitySearchFilter("", "",
				LocalDate.ofYearDay(2020, 1), LocalDate.ofYearDay(2020, 20), false, "30-25555555-0");
		allowAccessPermission(false);
		when(activitiesPublicApiPermissions.findInstitutionId("")).thenReturn(Optional.of(1));
		TestUtils.shouldThrow(ActivityByFilterAccessDeniedException.class,
				() -> fetchActivitiesByFilter.run(param));
	}

	private void allowAccessPermission(boolean canAccess) {
		when(activitiesPublicApiPermissions.canAccessActivityByFilter(1)).thenReturn(canAccess);
	}
}