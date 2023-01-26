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
public class MigrateOutpatientConsultation {

	private final MergeClinicHistoryStorage mergeClinicHistoryStorage;

	public void execute(List<Integer> oldPatients, Integer newPatient) {
		log.debug("Input parameters -> oldPatients{}, newPatient{}",oldPatients,newPatient);

		List<Integer> ocIds = mergeClinicHistoryStorage.getOutpatientConsultationIds(oldPatients);
		List<Integer> ncIds = mergeClinicHistoryStorage.getNursingConsultationIds(oldPatients);
		List<Integer> mrIds = mergeClinicHistoryStorage.getMedicationRequestIds(oldPatients);
		List<Integer> srIds = mergeClinicHistoryStorage.getServiceRequestIds(oldPatients);
		List<Integer> vcIds = mergeClinicHistoryStorage.getVaccineConsultationIds(oldPatients);
		List<Integer> crIds = mergeClinicHistoryStorage.getCounterReferenceIds(oldPatients);

		List<Integer> consultationIds = new ArrayList<>() {{
			addAll(ocIds);
			addAll(ncIds);
			addAll(mrIds);
			addAll(srIds);
			addAll(vcIds);
			addAll(crIds);
		}};

		if (!consultationIds.isEmpty()) {

			List<Long> documentsIds = mergeClinicHistoryStorage.getDocumentsIds(consultationIds, Arrays.asList(
					ESourceType.OUTPATIENT,
					ESourceType.NURSING,
					ESourceType.RECIPE,
					ESourceType.ORDER,
					ESourceType.IMMUNIZATION,
					ESourceType.COUNTER_REFERENCE));

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

				mergeClinicHistoryStorage.modifyDocument(documentsIds,newPatient);
			}

			mergeClinicHistoryStorage.modifyOutpatientConsultation(ocIds,newPatient);
			mergeClinicHistoryStorage.modifyNursingConsultation(ncIds, newPatient);
			mergeClinicHistoryStorage.modifyMedicationRequest(mrIds,newPatient);
			mergeClinicHistoryStorage.modifyServiceRequest(srIds,newPatient);
			mergeClinicHistoryStorage.modifyVaccineConsultation(vcIds,newPatient);
			mergeClinicHistoryStorage.modifyCounterReference(crIds,newPatient);

			mergeClinicHistoryStorage.modifySnvsReport(oldPatients, newPatient);
		}

	}
}
