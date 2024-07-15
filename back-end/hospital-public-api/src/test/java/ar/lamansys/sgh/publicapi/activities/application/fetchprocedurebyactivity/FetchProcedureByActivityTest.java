package ar.lamansys.sgh.publicapi.activities.application.fetchprocedurebyactivity;

import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid.exceptions.ActivitiesAccessDeniedException;
import ar.lamansys.sgh.publicapi.activities.application.fetchproceduresbyactivity.FetchProcedureByActivity;
import ar.lamansys.sgh.publicapi.activities.application.port.out.ActivityInfoStorage;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;
import ar.lamansys.sgh.publicapi.activities.domain.ProcedureInformationBo;
import ar.lamansys.sgh.publicapi.activities.domain.SnomedBo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FetchProcedureByActivityTest {

	private FetchProcedureByActivity fetchProcedureByActivity;

	@Mock
	private ActivityInfoStorage activityInfoStorage;

	@Mock
	private ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	@BeforeEach
	void setup() {
		fetchProcedureByActivity = new FetchProcedureByActivity(activityInfoStorage,activitiesPublicApiPermissions);
	}

	@Test
	void procedureSuccess() {
		allowAccessPermission(true);
		String refsetCode = "";
		Long activityId = 10L;

		when(activitiesPublicApiPermissions.findInstitutionId(refsetCode)).thenReturn(Optional.of(1));
		when(activityInfoStorage.getProceduresByActivity(refsetCode, activityId)).thenReturn(
				Collections.singletonList(ProcedureInformationBo.builder()
						.snomedBo(new SnomedBo("1", "1"))
						.administrationTime(LocalDateTime.now())
						.build()));

		List<ProcedureInformationBo> result = fetchProcedureByActivity.run(refsetCode, activityId);
		Assertions.assertEquals(result.size(), 1);
	}

	@Test
	void procedureFailed() {
		allowAccessPermission(true);
		String refsetCode = "";
		Long activityId = 10L;

		when(activitiesPublicApiPermissions.findInstitutionId(refsetCode)).thenReturn(Optional.of(1));
		when(activityInfoStorage.getProceduresByActivity(refsetCode, activityId)).thenReturn(
				Collections.emptyList()
		);

		List<ProcedureInformationBo> result = fetchProcedureByActivity.run(refsetCode, activityId);
		Assertions.assertEquals(result.size(), 0);
	}

	@Test
	void failProcedureActivityAccessDeniedException(){
		allowAccessPermission(false);
		when(activitiesPublicApiPermissions.findInstitutionId("")).thenReturn(Optional.of(1));
		TestUtils.shouldThrow(ActivitiesAccessDeniedException.class,
				() -> fetchProcedureByActivity.run("",10L));
	}

	private void allowAccessPermission(boolean canAccess) {
		when(activitiesPublicApiPermissions.canAccessActivityInfo(1)).thenReturn(canAccess);
	}


}
