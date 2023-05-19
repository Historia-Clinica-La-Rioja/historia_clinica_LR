package net.pladema.patient.application.mergepatient;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ESourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.AllergyIntoleranceRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.HealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ImmunizationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.IndicationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.MedicationStatementRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ObservationLabRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ObservationRiskFactorRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ProceduresRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.patient.application.port.MergeClinicHistoryStorage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

		if (!(ieIds.isEmpty() && srIds.isEmpty())) {

			List<Long> documentsIds = new ArrayList<>() {{
				addAll(mergeClinicHistoryStorage.getDocumentsIds(ieIds, ESourceType.HOSPITALIZATION));
				addAll(mergeClinicHistoryStorage.getDocumentsIds(srIds, ESourceType.ORDER));
			}};

			log.debug("Documents to search and modify documentsIds{}", documentsIds);
			if ((documentsIds != null) && (!documentsIds.isEmpty())) {

				mergeClinicHistoryStorage.migratePatientIdFromItem(HealthConditionRepository.class, documentsIds, newPatient);
				mergeClinicHistoryStorage.migratePatientIdFromItem(AllergyIntoleranceRepository.class, documentsIds, newPatient);
				mergeClinicHistoryStorage.migratePatientIdFromItem(ImmunizationRepository.class, documentsIds, newPatient);
				mergeClinicHistoryStorage.migratePatientIdFromItem(MedicationStatementRepository.class, documentsIds, newPatient);
				mergeClinicHistoryStorage.migratePatientIdFromItem(ProceduresRepository.class, documentsIds, newPatient);
				mergeClinicHistoryStorage.migratePatientIdFromItem(ObservationRiskFactorRepository.class, documentsIds, newPatient);
				mergeClinicHistoryStorage.migratePatientIdFromItem(ObservationLabRepository.class, documentsIds, newPatient);
				mergeClinicHistoryStorage.migratePatientIdFromItem(DiagnosticReportRepository.class, documentsIds, newPatient);
				mergeClinicHistoryStorage.migratePatientIdFromItem(IndicationRepository.class, documentsIds, newPatient);

				mergeClinicHistoryStorage.modifyInternmentEpisode(ieIds, newPatient);
				mergeClinicHistoryStorage.modifyServiceRequest(srIds,newPatient);

				mergeClinicHistoryStorage.modifyDocument(documentsIds,newPatient);

				mergeClinicHistoryStorage.rebuildDocumentsFiles(documentsIds);
			}
		}

	}
}
