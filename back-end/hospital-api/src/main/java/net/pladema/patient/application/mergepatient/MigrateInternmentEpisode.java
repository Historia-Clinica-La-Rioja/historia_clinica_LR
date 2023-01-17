package net.pladema.patient.application.mergepatient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.patient.application.port.MergeClinicHistoryStorage;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MigrateInternmentEpisode {

	private final MergeClinicHistoryStorage mergeClinicHistoryStorage;

	public void execute(List<Integer> oldPatients, Integer newPatient) {
		log.debug("Modify, oldPatients {}, newPatient {}", oldPatients, newPatient);

		List<Integer> ieIds = mergeClinicHistoryStorage.getInternmentEpisodesIds(oldPatients);

		if ((ieIds != null) && (!ieIds.isEmpty())) {

			List<Long> documentsIds = mergeClinicHistoryStorage.getDocumentsIds(ieIds);
			if ((documentsIds != null) && (!documentsIds.isEmpty())) {

				mergeClinicHistoryStorage.modifyHealthCondition(documentsIds, newPatient);
				mergeClinicHistoryStorage.modifyAllergyIntolerance(documentsIds, newPatient);
				mergeClinicHistoryStorage.modifyImmunization(documentsIds, newPatient);
				mergeClinicHistoryStorage.modifyMedicationStatement(documentsIds, newPatient);
				mergeClinicHistoryStorage.modifyProcedure(documentsIds, newPatient);
				mergeClinicHistoryStorage.modifyObservationVitalSign(documentsIds, newPatient);
				mergeClinicHistoryStorage.modifyObservationLab(documentsIds, newPatient);
				mergeClinicHistoryStorage.modifyDiagnosticReport(documentsIds, newPatient);
				mergeClinicHistoryStorage.modifyIndication(documentsIds, newPatient);

				mergeClinicHistoryStorage.modifyInternmentEpisode(ieIds, newPatient);
			}
		}

	}
}
