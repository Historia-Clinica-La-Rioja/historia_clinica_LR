package net.pladema.scheduledjobs.jobs;

import net.pladema.patient.controller.service.PatientExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@ConditionalOnProperty(
        value="scheduledjobs.federatepatients.enabled",
        havingValue = "true",
        matchIfMissing = false)
public class FederateValidatedPatientsJob {

    private static final Logger LOG = LoggerFactory.getLogger(FederateValidatedPatientsJob.class);

    private final PatientExternalService patientExternalService;

    public FederateValidatedPatientsJob(PatientExternalService patientExternalService) {
        this.patientExternalService = patientExternalService;
    }

    @Scheduled(cron = "${scheduledjobs.federatepatients.seconds} " +
            "${scheduledjobs.federatepatients.minutes} " +
            "${scheduledjobs.federatepatients.hours} " +
            "${scheduledjobs.federatepatients.dayofmonth} " +
            "${scheduledjobs.federatepatients.month} " +
            "${scheduledjobs.federatepatients.dayofweek}")
    public void execute(){
        LOG.debug("Executing FederateValidatedPatientsJob at {}", new Date());
        patientExternalService.federateAllValidatedPatients();
        LOG.debug("Finishing FederateValidatedPatientsJob at {}", new Date());
    }

}
