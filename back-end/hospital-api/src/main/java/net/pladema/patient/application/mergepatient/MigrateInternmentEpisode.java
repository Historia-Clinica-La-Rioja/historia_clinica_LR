package net.pladema.patient.application.mergepatient;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ESourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.patient.application.port.MergeClinicHistoryStorage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MigrateInternmentEpisode {

	private final MergeClinicHistoryStorage mergeClinicHistoryStorage;

	public void execute(List<Integer> oldPatients, Integer newPatient) {
		log.debug("Input parameters -> oldPatients{}, newPatient{}",oldPatients,newPatient);

		List<Integer> ieIds = mergeClinicHistoryStorage.getInternmentEpisodesIds(oldPatients);
		List<Integer> srIds = mergeClinicHistoryStorage.getServiceRequestIdsFromIdSourceType(ieIds,ESourceType.HOSPITALIZATION.getId());

		List<Integer> consultationIds = new ArrayList<>() {{
			addAll(ieIds);
			addAll(srIds);
		}};

		if (!consultationIds.isEmpty()) {

			List<Long> documentsIds = mergeClinicHistoryStorage.getDocumentsIds(consultationIds, Arrays.asList(
					ESourceType.HOSPITALIZATION,
					ESourceType.ORDER));

			log.debug("Documents to search and modify documentsIds{}", documentsIds);
			if ((documentsIds != null) && (!documentsIds.isEmpty())) {

				mergeClinicHistoryStorage.modifyHealthCondition(documentsIds, newPatient);
				mergeClinicHistoryStorage.modifyAllergyIntolerance(documentsIds, newPatient);
				mergeClinicHistoryStorage.modifyImmunization(documentsIds, newPatient);
				mergeClinicHistoryStorage.modifyMedicationStatement(documentsIds, newPatient);
				mergeClinicHistoryStorage.modifyProcedure(documentsIds, newPatient);
				mergeClinicHistoryStorage.modifyObservationRiskFactor(documentsIds, newPatient);
				mergeClinicHistoryStorage.modifyObservationLab(documentsIds, newPatient);
				mergeClinicHistoryStorage.modifyDiagnosticReport(documentsIds, newPatient);
				mergeClinicHistoryStorage.modifyIndication(documentsIds, newPatient);

				mergeClinicHistoryStorage.modifyInternmentEpisode(ieIds, newPatient);
				mergeClinicHistoryStorage.modifyServiceRequest(srIds,newPatient);

				mergeClinicHistoryStorage.modifyDocument(documentsIds,newPatient);
			}
		}

	}
}
