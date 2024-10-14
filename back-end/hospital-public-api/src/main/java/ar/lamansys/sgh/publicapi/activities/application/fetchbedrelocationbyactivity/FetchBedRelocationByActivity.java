package ar.lamansys.sgh.publicapi.activities.application.fetchbedrelocationbyactivity;

import ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid.exceptions.ActivitiesAccessDeniedException;
import ar.lamansys.sgh.publicapi.activities.application.port.out.ActivityInfoStorage;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;
import ar.lamansys.sgh.publicapi.activities.domain.BedRelocationInfoBo;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class FetchBedRelocationByActivity {

	private final ActivityInfoStorage activityInfoStorage;
	private final ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	public List<BedRelocationInfoBo> run(String refsetCode, Long activityId) {
		var institutionId = activitiesPublicApiPermissions.findInstitutionId(refsetCode);

		institutionId.ifPresent(this::assertUserCanAccess);

		log.debug("Input parameters -> refsetCode {}, activityId {}", refsetCode, activityId);
		List<BedRelocationInfoBo> result = activityInfoStorage.getBedRelocationsByActivity(refsetCode, activityId);
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertUserCanAccess(Integer institutionId) {
		if (!activitiesPublicApiPermissions.canAccessActivityInfo(institutionId)){
			throw new ActivitiesAccessDeniedException();
		}
	}
}
