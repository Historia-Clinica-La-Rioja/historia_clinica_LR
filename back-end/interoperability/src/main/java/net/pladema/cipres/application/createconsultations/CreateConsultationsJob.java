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
		value="scheduledjobs.cipresconsultations.enabled",
		havingValue = "true",
		matchIfMissing = false)
@Service
public class CreateConsultationsJob {

	private final GetConsultations getConsultations;

	private final CipresConsultationStorage cipresConsultationStorage;

	@Scheduled(cron = "${scheduledjobs.cipresconsultations.seconds} " +
			"${scheduledjobs.cipresconsultations.minutes} " +
			"${scheduledjobs.cipresconsultations.hours} " +
			"${scheduledjobs.cipresconsultations.dayofmonth} " +
			"${scheduledjobs.cipresconsultations.month} " +
			"${scheduledjobs.cipresconsultations.dayofweek}")
	@SchedulerLock(name = "CreateConsultationsJob")
	public void run() {
		log.warn("Scheduled CreateConsultationsJob starting at {}", new Date());
		int sentQuantity = 0;
		try {
			List<OutpatientConsultationBo> consultations = getConsultations.run();
			sentQuantity = cipresConsultationStorage.sendOutpatientConsultations(consultations);
		}
		catch (Exception ex){
			log.debug("Exception occurred while transmitting outpatient encounters to cipres: ", ex);
		}
		log.warn("Scheduled CreateConsultationsJob done, processing {} at {}", sentQuantity, new Date());
	}

}
