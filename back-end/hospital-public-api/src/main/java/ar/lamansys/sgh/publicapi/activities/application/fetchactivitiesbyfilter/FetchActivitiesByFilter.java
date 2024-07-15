package ar.lamansys.sgh.publicapi.activities.application.fetchactivitiesbyfilter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import ar.lamansys.sgh.publicapi.activities.application.fetchactivitiesbyfilter.exception.ActivityByFilterAccessDeniedException;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.activities.application.port.out.ActivityStorage;
import ar.lamansys.sgh.publicapi.activities.application.port.out.AttentionReadStorage;
import ar.lamansys.sgh.publicapi.activities.domain.AttentionInfoBo;
import ar.lamansys.sgh.publicapi.infrastructure.output.AttentionReads;
import ar.lamansys.sgh.publicapi.infrastructure.output.AttentionReadsPK;
import ar.lamansys.sgx.shared.security.UserInfo;

@AllArgsConstructor
@Slf4j
@Service
public class FetchActivitiesByFilter {

	private final ActivityStorage activityStorage;

	private final AttentionReadStorage attentionReadStorage;

	private final ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	public List<AttentionInfoBo> run(ActivitySearchFilter filter) {
		Integer institutionId = findInstitutionId(filter.getRefsetCode());
		assertUserCanAccess(institutionId);
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

		activities.forEach(act -> attentionReadStorage.saveAttention(
				new AttentionReads(
						new AttentionReadsPK(act.getId(), UserInfo.getCurrentAuditor().toString()),
						Boolean.FALSE,
						act.getAttentionDate() != null ? LocalDateTime.of(act.getAttentionDate(), LocalTime.now()) : LocalDateTime.now()
				)
		));

		return activities;
	}

	private void assertUserCanAccess(Integer institutionId) {
		if (!activitiesPublicApiPermissions.canAccessActivityByFilter(institutionId)) {
			throw new ActivityByFilterAccessDeniedException();
		}
	}

	private Integer findInstitutionId(String refsetCode) {
		return activitiesPublicApiPermissions.findInstitutionId(refsetCode)
				.orElseThrow(ActivityByFilterAccessDeniedException::new);
	}
}
