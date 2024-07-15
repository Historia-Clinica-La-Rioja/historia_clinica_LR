package ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid;

import ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid.exceptions.ActivitiesAccessDeniedException;
import ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid.exceptions.ActivityNotFoundException;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;
import ar.lamansys.sgh.publicapi.activities.application.port.out.ActivityStorage;
import ar.lamansys.sgh.publicapi.activities.domain.AttentionInfoBo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class FetchActivityById {
	private final ActivityStorage activityStorage;
	private final ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	public AttentionInfoBo run(String refsetCode, Long activityId) throws ActivityNotFoundException {
		log.debug("Find institutionId from refsetCode {}", refsetCode);
		Integer institutionId = findInstitutionId(refsetCode);

		log.debug("Checking permissions for institutionId {}", institutionId);
		assertUserCanAccess(institutionId);

		log.debug("Get Attention Info from refsetCode={} and activityId={}", refsetCode, activityId);
		AttentionInfoBo result = getAttentionInfoBo(refsetCode, activityId);
		log.debug("Got Attention Info -> {}", result);
		return result;
	}

	private AttentionInfoBo getAttentionInfoBo(String refsetCode, Long activityId) throws ActivityNotFoundException {
		return activityStorage.getActivityById(refsetCode, activityId)
				.orElseThrow(() -> new ActivityNotFoundException(refsetCode, activityId));
	}

	private void assertUserCanAccess(Integer institutionId) {
		if (!activitiesPublicApiPermissions.canAccess(institutionId)) {
			throw new ActivitiesAccessDeniedException();
		}
	}

	private Integer findInstitutionId(String refsetCode) {
		return activitiesPublicApiPermissions.findInstitutionId(refsetCode)
				.orElseThrow(ActivitiesAccessDeniedException::new);
	}
}
