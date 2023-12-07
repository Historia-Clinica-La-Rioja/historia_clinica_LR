package net.pladema.scheduledjobs.jobs;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums.EPatientType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.PatientService;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
		value="scheduledjobs.updatepatienttype.enabled",
		havingValue = "true",
		matchIfMissing = true)
public class UpdatePatientTypeJob {

	@Value("${scheduledjobs.updatepatienttype.pastdays:180}")
	private Long PAST_DAYS;

	@Value("${scheduledjobs.updatepatienttype.limit:10}")
	private Short LIMIT;

	private final PatientService patientService;

	@Scheduled(cron = "${scheduledjobs.updatepatienttype.cron}")
	@SchedulerLock(name = "UpdatePatientTypeJob")
	public void execute(){
		log.warn("Scheduled UpdatePatientTypeJob starting at {}", new Date());
		LocalDateTime actualDate = LocalDateTime.now();
		List<Patient> patients = patientService.getLongTermTemporaryPatientIds(actualDate.minusDays(PAST_DAYS), LIMIT);
		patients.forEach(patient ->
		{
			try{
				patient.setTypeId(EPatientType.REJECTED.getId());
				patientService.addPatient(patient);
				log.debug(String.format("Patient with id %s rejected successfully", patient.getId()));
			}
			catch (Exception ex){
				log.error("Exception occurred while rejecting patient: " + patient.getId().toString(), ex);
			}
		});
		log.warn("Scheduled UpdatePatientTypeJob done at {}", new Date());
	}

}
