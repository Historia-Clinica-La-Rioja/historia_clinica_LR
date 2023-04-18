package net.pladema.patient.application.mergepatient;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ESourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.HealthConditionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.patient.application.port.MergeClinicHistoryStorage;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MigrateEmergencyCareEpisode {

	private final MergeClinicHistoryStorage mergeClinicHistoryStorage;

	public void execute(List<Integer> oldPatients, Integer newPatient) {
		log.debug("Input parameters -> oldPatients{}, newPatient{}",oldPatients,newPatient);

		List<Integer> eceIds = mergeClinicHistoryStorage.getEmergencyCareEpisodeIds(oldPatients);

		if (!eceIds.isEmpty()) {

			List<Long> documentsIds = mergeClinicHistoryStorage.getDocumentsIds(eceIds, ESourceType.EMERGENCY_CARE);

			log.debug("Documents to search and modify documentsIds{}", documentsIds);
			if ((documentsIds != null) && (!documentsIds.isEmpty())) {

				mergeClinicHistoryStorage.migratePatientIdFromItem(HealthConditionRepository.class, documentsIds, newPatient);

				mergeClinicHistoryStorage.modifyDocument(documentsIds,newPatient);

				mergeClinicHistoryStorage.rebuildDocumentsFiles(documentsIds);
			}

			mergeClinicHistoryStorage.modifyTriageRiskFactor(eceIds,newPatient);

			mergeClinicHistoryStorage.modifyEmergencyCareEpisode(eceIds,newPatient);
		}
	}
}
