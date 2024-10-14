package net.pladema.scheduledjobs.jobs;
import ar.lamansys.sgx.auth.jwt.application.login.exceptions.BadLoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import net.pladema.establishment.service.OrchestratorService;
import net.pladema.imagenetwork.derivedstudies.service.MoveStudiesService;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
		value="scheduledjobs.massiveretry.enabled",
		havingValue = "true",
		matchIfMissing = false)
public class MassiveRetryMoveStudiesJob {


	private final MoveStudiesService moveStudiesService;

	private final OrchestratorService orchestratorService;



	@Scheduled(cron = "${scheduledjobs.massiveretry.seconds} " +
			"${scheduledjobs.massiveretry.minutes} " +
			"${scheduledjobs.massiveretry.hours} " +
			"${scheduledjobs.massiveretry.dayofmonth} " +
			"${scheduledjobs.massiveretry.month} " +
			"${scheduledjobs.massiveretry.dayofweek}")
	@SchedulerLock(name = "MassiveRetry")
	@Transactional
	public void execute() throws BadLoginException, InterruptedException {
		log.warn("Scheduled MassiveRetry  starting at {}", new Date());
		List<Integer> orchestratorsId = orchestratorService.getOrchestratorActiveMassiveRetry();
		for (Integer orchestratorId : orchestratorsId) {
			moveStudiesService.updateFailedCurrentDate(orchestratorId);
		}
		log.warn("Scheduled MassiveRetry done at {}", new Date());
	}


}


