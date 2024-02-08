package net.pladema.patient.application.mergepatient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MigrateClinicHistory {

	private final MigrateInternmentEpisode migrateInternmentEpisode;
	private final MigrateOutpatientConsultation migrateOutpatientConsultation;
	private final MigrateEmergencyCareEpisode migrateEmergencyCareEpisode;
	private final MigrateOdontologyConsultation migrateOdontologyConsultation;
	private final MigrateAppointment migrateAppointment;
	private final MigrateViolenceReport migrateViolenceReport;
	private final MigrateSurgicalReport migrateSurgicalReport;

	public void execute(List<Integer> oldPatients, Integer newPatient) {
		log.debug("Modify, oldPatients {}, newPatient {}", oldPatients, newPatient);

		migrateInternmentEpisode.execute(oldPatients,newPatient);
		migrateOutpatientConsultation.execute(oldPatients,newPatient);
		migrateEmergencyCareEpisode.execute(oldPatients,newPatient);
		migrateOdontologyConsultation.execute(oldPatients, newPatient);
		migrateAppointment.execute(oldPatients, newPatient);
		migrateViolenceReport.execute(oldPatients, newPatient);
		migrateSurgicalReport.execute(oldPatients, newPatient);
	}
}
