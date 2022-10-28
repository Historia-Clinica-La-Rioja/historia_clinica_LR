package ar.lamansys.sgh.publicapi.application;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import ar.lamansys.sgh.publicapi.domain.SingleDiagnosticBo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgh.publicapi.application.fetchactivitybyid.FetchActivityById;
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
public class FetchActivityByIdTest {

	private FetchActivityById fetchActivityById;

	@Mock
	private ActivityStorage activityStorage;

	@BeforeEach
	void setup() {
		fetchActivityById = new FetchActivityById(activityStorage);
	}

	@Test
	void activitySuccess() {
		String refsetCode = "";
		Long activityId = 10L;

		when(activityStorage.getActivityById(refsetCode, activityId)).thenReturn(
				Optional.of(new AttentionInfoBo(
						10L, 1L, LocalDate.now(),
						new SnomedBo("1", "1"),
						new PersonInfoBo("35555555", "Juan", "Perez", LocalDate.ofYearDay(1990, 1), GenderEnum.MALE),
						new CoverageActivityInfoBo(), ScopeEnum.AMBULATORIA,
						new InternmentBo("100", LocalDate.ofYearDay(2020, 1).atStartOfDay(), LocalDate.ofYearDay(2020, 20).atStartOfDay()),
						new ProfessionalBo(1, "Juan", "Perez", "DOC-30000000", "30000000"),
						new SingleDiagnosticBo(new SnomedBo("1", "1"), true, "1234345", "2345435", LocalDateTime.now())
				)));

		AttentionInfoBo result = fetchActivityById.run(refsetCode, activityId);
		Assertions.assertNotNull(result);
	}

	@Test
	void activityFailed() {
		String refsetCode = "";
		Long activityId = 10L;

		when(activityStorage.getActivityById(refsetCode, activityId)).thenReturn(
				Optional.empty());

		AttentionInfoBo result = fetchActivityById.run(refsetCode, activityId);
		Assertions.assertNull(result);
	}
}