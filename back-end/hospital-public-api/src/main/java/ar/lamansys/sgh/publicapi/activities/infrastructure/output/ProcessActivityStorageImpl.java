package ar.lamansys.sgh.publicapi.activities.infrastructure.output;

import ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid.exceptions.ActivitiesAccessDeniedException;
import ar.lamansys.sgh.publicapi.activities.application.port.out.ProcessActivityStorage;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;
import ar.lamansys.sgh.publicapi.infrastructure.output.AttentionReads;
import ar.lamansys.sgh.publicapi.infrastructure.output.AttentionReadsRepository;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ProcessActivityStorageImpl implements ProcessActivityStorage {
	private final AttentionReadsRepository attentionReadsRepository;
	private final ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	@Override
	public void processActivity(String refsetCode, Long activityId) {
		log.debug("update ActivityInfoStorage -> refsetCode {}, activityId {}", refsetCode, activityId);
		List<AttentionReads> toUpdate = attentionReadsRepository.findByAttentionId(activityId);
		toUpdate.forEach(ar -> ar.setProcessed(true));
		log.trace("Updated -> {}", attentionReadsRepository.saveAll(toUpdate));
	}

}
