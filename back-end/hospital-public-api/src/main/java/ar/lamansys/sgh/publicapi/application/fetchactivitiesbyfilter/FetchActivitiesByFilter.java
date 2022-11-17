package ar.lamansys.sgh.publicapi.application.fetchactivitiesbyfilter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.application.port.out.ActivityStorage;
import ar.lamansys.sgh.publicapi.application.port.out.AttentionReadStorage;
import ar.lamansys.sgh.publicapi.domain.AttentionInfoBo;
import ar.lamansys.sgh.publicapi.infrastructure.output.AttentionReads;
import ar.lamansys.sgh.publicapi.infrastructure.output.AttentionReadsPK;
import ar.lamansys.sgx.shared.security.UserInfo;

@Service
public class FetchActivitiesByFilter {

	private final Logger logger;
	private final ActivityStorage activityStorage;

	private final AttentionReadStorage attentionReadStorage;

	public FetchActivitiesByFilter(ActivityStorage activityStorage, AttentionReadStorage attentionReadStorage) {
		this.logger = LoggerFactory.getLogger(FetchActivitiesByFilter.class);
		this.activityStorage = activityStorage;
		this.attentionReadStorage = attentionReadStorage;
	}

	public List<AttentionInfoBo> run(ActivitySearchFilter filter) {
		logger.debug("Input parameters -> filter {}", filter);
		List<AttentionInfoBo> result = getFromStorage(filter);
		logger.debug("Output -> {}", result);
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
}
