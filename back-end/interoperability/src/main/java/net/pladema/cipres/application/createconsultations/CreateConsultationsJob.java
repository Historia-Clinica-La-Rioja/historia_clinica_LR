package net.pladema.cipres.application.createconsultations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import net.pladema.cipres.domain.OutpatientConsultationBo;
import net.javacrumbs.shedlock.core.SchedulerLock;
import net.pladema.cipres.application.getconsultations.GetConsultations;
import net.pladema.cipres.application.port.CipresConsultationStorage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
		value="scheduledjobs.cipres-consultations.enabled",
		havingValue = "true",
		matchIfMissing = false)
@Service
public class CreateConsultationsJob {

	private final GetConsultations getConsultations;

	private final CipresConsultationStorage cipresConsultationStorage;

	@Scheduled(cron = "${scheduledjobs.cipres-consultations.seconds} " +
			"${scheduledjobs.cipres-consultations.minutes} " +
			"${scheduledjobs.cipres-consultations.hours} " +
			"${scheduledjobs.cipres-consultations.dayofmonth} " +
			"${scheduledjobs.cipres-consultations.month} " +
			"${scheduledjobs.cipres-consultations.dayofweek}")
	@SchedulerLock(name = "CreateConsultationsJob")
	public void run() {
		log.warn("Scheduled CreateConsultationsJob starting at {}", new Date());
		List<OutpatientConsultationBo> consultations = getConsultations.run();
		cipresConsultationStorage.createOutpatientConsultations(consultations);
		log.warn("Scheduled CreateConsultationsJob done at {}", new Date());
	}

}
