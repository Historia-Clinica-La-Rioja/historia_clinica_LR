package net.pladema.nomivac.infrastructure.input.rest.scheduling;

import net.javacrumbs.shedlock.core.SchedulerLock;
import net.pladema.nomivac.application.synchronizeImmunization.SynchronizeImmunization;
import net.pladema.nomivac.infrastructure.configuration.NomivacCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Conditional(NomivacCondition.class)
public class NomivacSynchronizationService {
	private final Logger logger;
	private final SynchronizeImmunization synchronizeImmunization;

	public NomivacSynchronizationService(SynchronizeImmunization synchronizeImmunization) {
		this.synchronizeImmunization = synchronizeImmunization;
		this.logger = LoggerFactory.getLogger(this.getClass());
	}

	@Scheduled(cron = "${ws.nomivac.synchronization.cron.config:-}")
	@SchedulerLock(name = "NomivacSynchronizeImmunizations")
	public void sincronizarLote() {
		logger.info("Se va a sincronizar vacunas");
		synchronizeImmunization.run();
	}
}