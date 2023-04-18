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
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.OdontologyDiagnosticRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.OdontologyProcedureRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ProceduresRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.patient.application.port.MergeClinicHistoryStorage;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

		if (!(ocIds.isEmpty() && ncIds.isEmpty() && mrIds.isEmpty() && srIds.isEmpty() && vcIds.isEmpty() && crIds.isEmpty())) {

			List<Long> documentsIds = new ArrayList<>() {{
				addAll(mergeClinicHistoryStorage.getDocumentsIds(ocIds, ESourceType.OUTPATIENT));
				addAll(mergeClinicHistoryStorage.getDocumentsIds(ncIds, ESourceType.NURSING));
				addAll(mergeClinicHistoryStorage.getDocumentsIds(mrIds, ESourceType.RECIPE));
				addAll(mergeClinicHistoryStorage.getDocumentsIds(srIds, ESourceType.ORDER));
				addAll(mergeClinicHistoryStorage.getDocumentsIds(vcIds, ESourceType.IMMUNIZATION));
				addAll(mergeClinicHistoryStorage.getDocumentsIds(crIds, ESourceType.COUNTER_REFERENCE));
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

				mergeClinicHistoryStorage.modifyDocument(documentsIds,newPatient);

				mergeClinicHistoryStorage.rebuildDocumentsFiles(documentsIds);
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
