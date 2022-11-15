package net.pladema.scheduledjobs.jobs;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import net.pladema.federar.controller.FederarExternalService;
import net.pladema.federar.services.domain.FederarResourceAttributes;
import net.pladema.patient.service.PatientService;

@Service
@ConditionalOnProperty(
        value="scheduledjobs.federatepatients.enabled",
        havingValue = "true",
        matchIfMissing = false)
public class FederateValidatedPatientsJob {

    private static final Logger LOG = LoggerFactory.getLogger(FederateValidatedPatientsJob.class);

    //TODO: change for PatientExternalService
    private final PatientService patientService;

    private final FederarExternalService federarExternalService;

    public FederateValidatedPatientsJob(PatientService patientService,
                                        FederarExternalService federarExternalService) {
        this.patientService = patientService;
        this.federarExternalService = federarExternalService;
    }

    @Scheduled(cron = "${scheduledjobs.federatepatients.seconds} " +
            "${scheduledjobs.federatepatients.minutes} " +
            "${scheduledjobs.federatepatients.hours} " +
            "${scheduledjobs.federatepatients.dayofmonth} " +
            "${scheduledjobs.federatepatients.month} " +
            "${scheduledjobs.federatepatients.dayofweek}")
    public void execute() {
        LOG.debug("Executing FederateValidatedPatientsJob at {}", new Date());
        patientService.getAllValidatedPatients().forEach(p -> {
            FederarResourceAttributes attributes = new FederarResourceAttributes();
            BeanUtils.copyProperties(p, attributes);

			try {
				Optional<Integer> optionalNationalId = federarExternalService.federatePatient(attributes, p.getId());
				optionalNationalId.ifPresent(nationalId -> patientService.updatePatientPermanent(p, nationalId));
			} catch (Exception ex) {
				LOG.error("Fallo en la comunicación => {}", ex.getMessage());
			}

        });
        LOG.debug("Finishing FederateValidatedPatientsJob at {}", new Date());
    }

}
