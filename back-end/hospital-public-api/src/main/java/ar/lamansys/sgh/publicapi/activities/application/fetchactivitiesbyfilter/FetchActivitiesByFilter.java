package ar.lamansys.sgh.publicapi.activities.application.fetchactivitiesbyfilter;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.activities.application.fetchactivitiesbyfilter.exception.ActivityByFilterAccessDeniedException;
import ar.lamansys.sgh.publicapi.activities.application.port.out.ActivityStorage;
import ar.lamansys.sgh.publicapi.activities.domain.AttentionInfoBo;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class FetchActivitiesByFilter {

	private final ActivityStorage activityStorage;

	private final ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	public List<AttentionInfoBo> run(ActivitySearchFilter filter) {

		var institutionId = activitiesPublicApiPermissions.findInstitutionId(filter.getRefsetCode());
		institutionId.ifPresent(this::assertUserCanAccess);

		log.debug("Input parameters -> filter {}", filter);
		List<AttentionInfoBo> result = getFromStorage(filter);
		log.debug("Output -> {}", result);
		return result;
	}

	private List<AttentionInfoBo> getFromStorage(ActivitySearchFilter filter) {
		
		List<AttentionInfoBo> activities;
		if (filter.getIdentificationNumber() != null && !filter.getIdentificationNumber().isBlank()) {
			activities = activityStorage.getActivitiesByInstitutionAndPatient(filter.getRefsetCode(), filter.getIdentificationNumber(), filter.getFrom(), filter.getTo(), filter.getReprocessing());
		} else if (filter.getCoverageCuit() != null && !filter.getCoverageCuit().isBlank()) {
			activities = activityStorage.getActivitiesByInstitutionAndCoverage(filter.getRefsetCode(), filter.getCoverageCuit(), filter.getFrom(), filter.getTo(), filter.getReprocessing());
		} else activities = activityStorage.getActivitiesByInstitution(
				filter.getRefsetCode(), filter.getFrom(), filter.getTo(), filter.getReprocessing());

		return activities;
	}

	private void assertUserCanAccess(Integer institutionId) {
		if (!activitiesPublicApiPermissions.canAccessActivityByFilter(institutionId)) {
			throw new ActivityByFilterAccessDeniedException();
		}
	}

}
