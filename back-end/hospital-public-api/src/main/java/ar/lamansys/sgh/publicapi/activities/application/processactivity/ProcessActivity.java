package ar.lamansys.sgh.publicapi.activities.application.processactivity;

import ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid.exceptions.ActivitiesAccessDeniedException;
import ar.lamansys.sgh.publicapi.activities.application.port.out.ProcessActivityStorage;

import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class ProcessActivity {

	private final ProcessActivityStorage processActivityStorage;
	private final ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	public void run(String refsetCode, Long activityId) {
		var institutionId = activitiesPublicApiPermissions.findInstitutionId(refsetCode);

		institutionId.ifPresent(this::assertUserCanAccess);

		log.debug("Input parameters -> refsetCode {}, activityId {}", refsetCode, activityId);
		processActivityStorage.processActivity(refsetCode, activityId);
	}

	private void assertUserCanAccess(Integer institutionId) {
		if (!activitiesPublicApiPermissions.canAccessProcessActivityInfo(institutionId)){
			throw new ActivitiesAccessDeniedException();
		}
	}
}
