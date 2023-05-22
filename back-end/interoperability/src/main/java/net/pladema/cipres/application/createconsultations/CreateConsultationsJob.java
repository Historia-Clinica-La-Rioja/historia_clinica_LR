package net.pladema.cipres.application.createconsultations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import net.pladema.cipres.domain.OutpatientConsultationBo;
import net.javacrumbs.shedlock.core.SchedulerLock;
import net.pladema.cipres.application.getpatientid.GetPatientId;
import net.pladema.cipres.application.getconsultations.GetConsultations;
import net.pladema.cipres.application.port.CipresConsultationStorage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
		value="scheduledjobs.cipres-consultations.enabled",
		havingValue = "true",
		matchIfMissing = false)
public class CreateConsultationsJob {

	private final GetConsultations getConsultations;

	private final GetPatientId getPatientId;

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
		Map<Integer, List<OutpatientConsultationBo>> consultations = getConsultations.run();
		if (!consultations.isEmpty()) {
			consultations.keySet().stream().forEach(patientId -> {
				Integer apiPatientId = getPatientId.run(consultations.get(patientId).get(0).getPatient().getPerson());
				if (apiPatientId != null)
					cipresConsultationStorage.createOutpatientConsultations(consultations.get(patientId).stream().peek(c -> c.setApiPatientId(apiPatientId)).collect(Collectors.toList()));
			});
		}
		log.warn("Scheduled CreateConsultationsJob done at {}", new Date());
	}

}
