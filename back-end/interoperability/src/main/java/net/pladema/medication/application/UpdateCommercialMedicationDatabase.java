package net.pladema.medication.application;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.javacrumbs.shedlock.core.SchedulerLock;
import net.pladema.medication.application.port.CommercialMedicationUpdateFilePort;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;

import java.io.IOException;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateCommercialMedicationDatabase {

	private final CommercialMedicationUpdateFilePort commercialMedicationUpdateFilePort;

	private final SaveCommercialMedicationDatabase saveCommercialMedicationDatabase;

	private final UpdateCommercialMedicationSchema updateCommercialMedicationSchema;

	@Scheduled(cron = "${scheduledjobs.commercial-medication-database.cron}")
	@SchedulerLock(name = "CommercialMedicationDatabaseJob")
	public void run() throws JAXBException, IOException {
		log.warn("Scheduled CommercialMedicationDatabaseJob starting at {}", new Date());
		boolean schemaAlreadyInitialized = commercialMedicationUpdateFilePort.schemaAlreadyInitialized();
		if (!schemaAlreadyInitialized)
			saveCommercialMedicationDatabase.run();
		else
			updateCommercialMedicationSchema.run();
		log.warn("Scheduled CommercialMedicationDatabaseJob done at {}", new Date());
	}

}
