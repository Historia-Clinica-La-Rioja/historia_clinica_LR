package ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import ar.lamansys.sgh.publicapi.activities.domain.PersonInfoExtendedBo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid.exceptions.ActivityNotFoundException;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;
import ar.lamansys.sgh.publicapi.activities.application.port.out.ActivityStorage;
import ar.lamansys.sgh.publicapi.activities.domain.AttentionInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.CoverageActivityInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.GenderEnum;
import ar.lamansys.sgh.publicapi.activities.domain.InternmentBo;
import ar.lamansys.sgh.publicapi.activities.domain.PersonInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.ProfessionalBo;
import ar.lamansys.sgh.publicapi.domain.ScopeEnum;
import ar.lamansys.sgh.publicapi.activities.domain.SingleDiagnosticBo;
import ar.lamansys.sgh.publicapi.activities.domain.SnomedBo;
import ar.lamansys.sgh.publicapi.activities.domain.SnomedCIE10Bo;
import ar.lamansys.sgh.publicapi.domain.datetimeutils.DateBo;
import ar.lamansys.sgh.publicapi.domain.datetimeutils.DateTimeBo;
import ar.lamansys.sgh.publicapi.domain.datetimeutils.TimeBo;
@ExtendWith(MockitoExtension.class)
public class FetchActivityByIdTest {

	private FetchActivityById fetchActivityById;

	@Mock
	private ActivityStorage activityStorage;

	@Mock
	private ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	@BeforeEach
	void setup() {
		fetchActivityById = new FetchActivityById(
				activityStorage,
				activitiesPublicApiPermissions
		);
	}

	@Test
	void activitySuccess() throws ActivityNotFoundException {
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
						new SingleDiagnosticBo(new SnomedCIE10Bo("1", "1", "1"), true, "1234345", "2345435", LocalDateTime.now()),
						new DateTimeBo(new DateBo(2020, 1, 1), new TimeBo(20, 30, 45)),
						new PersonInfoExtendedBo("Pedro", "Perez", "jp@gmail.com", "Juan", (short)1),
						null
				)));

		setUserCanAccess(refsetCode, 10);
		AttentionInfoBo result = fetchActivityById.run(refsetCode, activityId);
		Assertions.assertNotNull(result);
	}

	private void setUserCanAccess(String refsetCode, Integer institutionId) {
		when(activitiesPublicApiPermissions.findInstitutionId(refsetCode))
				.thenReturn(Optional.of(institutionId));
		when(activitiesPublicApiPermissions.canAccess(institutionId))
				.thenReturn(true);
	}

	@Test
	void activityFailed() {
		String refsetCode = "";
		Long activityId = 10L;

		when(activityStorage.getActivityById(refsetCode, activityId))
				.thenReturn(Optional.empty());

		setUserCanAccess(refsetCode, 10);
		ActivityNotFoundException exception = Assertions.assertThrows(ActivityNotFoundException.class, () ->
				fetchActivityById.run(refsetCode, activityId)
		);

		Assertions.assertNotNull(exception);
		assertThat(exception.activityId)
				.isEqualTo(activityId);
		assertThat(exception.refsetCode)
				.isEqualTo(refsetCode);
	}
}