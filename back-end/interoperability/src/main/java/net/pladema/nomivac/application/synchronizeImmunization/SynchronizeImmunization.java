package net.pladema.nomivac.application.synchronizeImmunization;

import net.pladema.nomivac.domain.immunization.FetchImmunizationToSynchronize;
import net.pladema.nomivac.domain.immunization.SendImmunization;
import net.pladema.nomivac.domain.immunization.UpdateSynchronizedStatus;
import net.pladema.nomivac.infrastructure.configuration.NomivacCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Conditional(NomivacCondition.class)
public class SynchronizeImmunization {

    private final Logger logger;

    private final FetchImmunizationToSynchronize fetchImmunizationToSynchronize;

    private final SendImmunization sendImmunization;

    private final UpdateSynchronizedStatus updateSynchronizedStatus;

    public SynchronizeImmunization(FetchImmunizationToSynchronize fetchImmunizationToSynchronize,
                                   SendImmunization sendImmunization,
                                   UpdateSynchronizedStatus updateSynchronizedStatus) {
        this.fetchImmunizationToSynchronize = fetchImmunizationToSynchronize;
        this.sendImmunization = sendImmunization;
        this.updateSynchronizedStatus = updateSynchronizedStatus;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public void run(){
        logger.debug("Synchronize immunization with Nomivac utc datetime -> {}", LocalDateTime.now());
        fetchImmunizationToSynchronize
                .run()
                .map(sendImmunization::run)
                .ifPresent(updateSynchronizedStatus::run);
    }
}
