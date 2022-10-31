package ar.lamansys.sgh.publicapi.application;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import ar.lamansys.sgh.publicapi.application.port.out.AttentionReadStorage;
import ar.lamansys.sgh.publicapi.domain.SingleDiagnosticBo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgh.publicapi.application.fetchactivitiesbyfilter.ActivitySearchFilter;
import ar.lamansys.sgh.publicapi.application.fetchactivitiesbyfilter.FetchActivitiesByFilter;
import ar.lamansys.sgh.publicapi.application.port.out.ActivityStorage;
import ar.lamansys.sgh.publicapi.domain.AttentionInfoBo;
import ar.lamansys.sgh.publicapi.domain.CoverageActivityInfoBo;
import ar.lamansys.sgh.publicapi.domain.GenderEnum;
import ar.lamansys.sgh.publicapi.domain.InternmentBo;
import ar.lamansys.sgh.publicapi.domain.PersonInfoBo;
import ar.lamansys.sgh.publicapi.domain.ProfessionalBo;
import ar.lamansys.sgh.publicapi.domain.ScopeEnum;
import ar.lamansys.sgh.publicapi.domain.SnomedBo;
@ExtendWith(MockitoExtension.class)
public class FetchActivityByFilterTest {

	private FetchActivitiesByFilter fetchActivitiesByFilter;

	@Mock
	private ActivityStorage activityStorage;

	@Mock
	private AttentionReadStorage attentionReadStorage;

	@BeforeEach
	void setup() {
		fetchActivitiesByFilter = new FetchActivitiesByFilter(activityStorage, attentionReadStorage);
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
								new SingleDiagnosticBo(new SnomedBo("1", "1"), true, "1234345", "2345435", LocalDateTime.now())
						),
						new AttentionInfoBo(
								11L, 2L, LocalDate.ofYearDay(2020, 2),
								new SnomedBo("2", "2"),
								new PersonInfoBo("35555556", "Rocio", "Gonzalez", LocalDate.ofYearDay(1990, 1), GenderEnum.FEMALE),
								new CoverageActivityInfoBo(), ScopeEnum.INTERNACION,
								new InternmentBo("100", LocalDate.ofYearDay(2020, 1).atStartOfDay(), LocalDate.ofYearDay(2020, 20).atStartOfDay()),
								new ProfessionalBo(1, "Juan", "Perez", "DOC-30000000", "30000000"),
								new SingleDiagnosticBo(new SnomedBo("1", "1"), true, "1234345", "2345435", LocalDateTime.now())
						),
						new AttentionInfoBo(
								12L, 3L, LocalDate.ofYearDay(2020, 3),
								new SnomedBo("3", "3"),
								new PersonInfoBo("35555557", "Pedro", "Rodriguez", LocalDate.ofYearDay(1990, 1), GenderEnum.MALE),
								new CoverageActivityInfoBo(), ScopeEnum.AMBULATORIA,
								new InternmentBo("100", LocalDate.ofYearDay(2020, 1).atStartOfDay(), LocalDate.ofYearDay(2020, 20).atStartOfDay()),
								new ProfessionalBo(1, "Jose", "Fernandez", "DOC-30000000", "30000000"),
								new SingleDiagnosticBo(new SnomedBo("1", "1"), true, "1234345", "2345435", LocalDateTime.now())
						)
				)
		);

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
								new SingleDiagnosticBo(new SnomedBo("1", "1"), true, "1234345", "2345435", LocalDateTime.now())
						)
				)
		);

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
								new SingleDiagnosticBo(new SnomedBo("1", "1"), true, "1234345", "2345435", LocalDateTime.now())
						),
						new AttentionInfoBo(
								11L,2L, LocalDate.ofYearDay(2020, 2),
								new SnomedBo("2", "2"),
								new PersonInfoBo("35555556", "Rocio", "Gonzalez", LocalDate.ofYearDay(1990, 1), GenderEnum.FEMALE),
								new CoverageActivityInfoBo(), ScopeEnum.INTERNACION,
								new InternmentBo("100", LocalDate.ofYearDay(2020, 1).atStartOfDay(), LocalDate.ofYearDay(2020, 20).atStartOfDay()),
								new ProfessionalBo(1, "Juan", "Perez", "DOC-30000000", "30000000"),
								new SingleDiagnosticBo(new SnomedBo("1", "1"), true, "1234345", "2345435", LocalDateTime.now())
						)
				));

		List<AttentionInfoBo> result = fetchActivitiesByFilter.run(param);
		Assertions.assertEquals(result.size(), 2);
	}
}