package ar.lamansys.sgh.publicapi.activities.application.fetchsuppliesbyactivity;

import java.util.List;

import ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid.exceptions.ActivitiesAccessDeniedException;

import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.activities.application.port.out.ActivityInfoStorage;
import ar.lamansys.sgh.publicapi.activities.domain.SupplyInformationBo;

@Slf4j
@AllArgsConstructor
@Service
public class FetchSuppliesByActivity {

	private final ActivityInfoStorage activityInfoStorage;
	private final ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	public List<SupplyInformationBo> run(String refsetCode, Long activityId) {
		var institutionId = activitiesPublicApiPermissions.findInstitutionId(refsetCode);

		institutionId.ifPresent(this::assertUserCanAccess);

		log.debug("Input parameters -> refsetCode {}, activityId {}", refsetCode, activityId);
		var result = activityInfoStorage.getSuppliesByActivity(refsetCode, activityId);
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertUserCanAccess(Integer institutionId) {
		if (!activitiesPublicApiPermissions.canAccessActivityInfo(institutionId)){
			throw new ActivitiesAccessDeniedException();
		}
	}
}
