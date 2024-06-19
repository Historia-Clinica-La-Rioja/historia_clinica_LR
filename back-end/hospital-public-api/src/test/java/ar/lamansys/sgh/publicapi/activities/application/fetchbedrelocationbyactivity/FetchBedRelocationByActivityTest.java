package ar.lamansys.sgh.publicapi.activities.application.fetchbedrelocationbyactivity;

import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid.exceptions.ActivitiesAccessDeniedException;
import ar.lamansys.sgh.publicapi.activities.application.port.out.ActivityInfoStorage;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;
import ar.lamansys.sgh.publicapi.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class FetchBedRelocationByActivityTest {

	private FetchBedRelocationByActivity fetchBedRelocationByActivity;

	@Mock
	private ActivityInfoStorage activityInfoStorage;

	@Mock
	private ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	@BeforeEach
	void setup() {
		fetchBedRelocationByActivity = new FetchBedRelocationByActivity(activityInfoStorage, activitiesPublicApiPermissions);
	}

	@Test
	void bedRelocationSuccess() {
		allowAccessPermission(true);
		String refsetCode = "";
		Long activityId = 10L;

		when(activitiesPublicApiPermissions.findInstitutionId(refsetCode)).thenReturn(Optional.of(1));
		when(activityInfoStorage.getBedRelocationsByActivity(refsetCode, activityId)).thenReturn(
				Arrays.asList(
						new BedRelocationInfoBo(
								1,
								LocalDateTime.now(), "Cuidados Mínimos",
								new SnomedBo("1", "1")
						),
						new BedRelocationInfoBo(
								2,
								LocalDateTime.now(), "Intensiva",
								new SnomedBo("2", "2")
						)
				)
		);

		List<BedRelocationInfoBo> result = fetchBedRelocationByActivity.run(refsetCode, activityId);
		Assertions.assertEquals(result.size(), 2);
	}

	@Test
	void bedRelocationFailed() {
		allowAccessPermission(true);
		String refsetCode = "";
		Long activityId = 10L;

		when(activitiesPublicApiPermissions.findInstitutionId(refsetCode)).thenReturn(Optional.of(1));
		when(activityInfoStorage.getBedRelocationsByActivity(refsetCode, activityId)).thenReturn(
				new ArrayList<>()
		);

		List<BedRelocationInfoBo> result = fetchBedRelocationByActivity.run(refsetCode, activityId);
		Assertions.assertEquals(result.size(), 0);
	}

	@Test
	void failBedRelocationActivityAccessDeniedException(){
		allowAccessPermission(false);
		when(activitiesPublicApiPermissions.findInstitutionId("")).thenReturn(Optional.of(1));
		TestUtils.shouldThrow(ActivitiesAccessDeniedException.class,
				() -> fetchBedRelocationByActivity.run("",10L));
	}

	private void allowAccessPermission(boolean canAccess) {
		when(activitiesPublicApiPermissions.canAccessActivityInfo(1)).thenReturn(canAccess);
	}

}