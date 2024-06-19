package ar.lamansys.sgh.publicapi.activities.application.processactivity;

import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid.exceptions.ActivitiesAccessDeniedException;
import ar.lamansys.sgh.publicapi.activities.application.port.out.ProcessActivityStorage;

import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProcessActivityTest {
	private ProcessActivity processActivity;;

	@Mock
	private ProcessActivityStorage processActivityStorage;

	@Mock
	private ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	@BeforeEach
	void setUp() {
		processActivity = new ProcessActivity(processActivityStorage, activitiesPublicApiPermissions);
	}

	@Test
	void processActivitySuccess(){
		allowAccessPermission(true);
		String refsetCode = "";
		Long activityId = 10L;
		when(activitiesPublicApiPermissions.findInstitutionId(refsetCode)).thenReturn(Optional.of(1));
		processActivity.run(refsetCode, activityId);
		verify(processActivityStorage).processActivity(refsetCode, activityId);
	}

	@Test
	void failProcessActivityAccessDeniedException(){
		allowAccessPermission(false);
		when(activitiesPublicApiPermissions.findInstitutionId("")).thenReturn(Optional.of(1));
		TestUtils.shouldThrow(ActivitiesAccessDeniedException.class,
				() -> processActivity.run("",10L));
	}

	private void allowAccessPermission(boolean canAccess) {
		when(activitiesPublicApiPermissions.canAccessProcessActivityInfo(1)).thenReturn(canAccess);
	}

}
