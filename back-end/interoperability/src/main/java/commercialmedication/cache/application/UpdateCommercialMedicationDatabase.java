package commercialmedication.cache.application;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.javacrumbs.shedlock.core.SchedulerLock;
import commercialmedication.cache.application.port.CommercialMedicationUpdateLogPort;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;

import java.io.IOException;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateCommercialMedicationDatabase {

	private final CommercialMedicationUpdateLogPort commercialMedicationUpdateLogPort;

	private final SaveCommercialMedicationDatabase saveCommercialMedicationDatabase;

	private final UpdateCommercialMedicationSchema updateCommercialMedicationSchema;

	@Scheduled(cron = "${scheduledjobs.commercial-medication-database.cron}")
	@SchedulerLock(name = "CommercialMedicationDatabaseJob")
	public void run() throws JAXBException, IOException {
		log.warn("Scheduled CommercialMedicationDatabaseJob starting at {}", new Date());
		boolean schemaAlreadyInitialized = commercialMedicationUpdateLogPort.schemaAlreadyInitialized();
		if (!schemaAlreadyInitialized)
			saveCommercialMedicationDatabase.run();
		else
			updateCommercialMedicationSchema.run();
		log.warn("Scheduled CommercialMedicationDatabaseJob done at {}", new Date());
	}

}
