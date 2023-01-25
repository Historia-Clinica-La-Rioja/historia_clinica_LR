package net.pladema.patient.infrastructure.output.repository;

import java.util.List;
import java.util.stream.Collectors;

import ar.lamansys.refcounterref.infraestructure.output.repository.counterreference.CounterReferenceRepository;
import net.pladema.clinichistory.requests.medicationrequests.repository.MedicationRequestRepository;

import net.pladema.clinichistory.requests.servicerequests.repository.ServiceRequestRepository;
import net.pladema.snvs.infrastructure.output.repository.report.SnvsReportRepository;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentAllergyIntoleranceRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentDiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentHealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentImmunizationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentIndicationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentLabRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentMedicamentionStatementRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentOdontologyDiagnosticRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentOdontologyProcedureRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentProcedureRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRiskFactorRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ESourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationRepository;
import net.pladema.medicalconsultation.appointment.repository.DocumentAppointmentRepository;
import net.pladema.patient.application.port.MergeClinicHistoryStorage;
import net.pladema.patient.application.port.MigratePatientStorage;
import net.pladema.patient.infrastructure.output.repository.entity.EMergeTable;

@Service
@Slf4j
@RequiredArgsConstructor
public class MergeClinicHistoryStorageImpl implements MergeClinicHistoryStorage {

	private final InternmentEpisodeRepository internmentEpisodeRepository;
	private final OutpatientConsultationRepository outpatientConsultationRepository;
	private final MedicationRequestRepository medicationRequestRepository;
	private final ServiceRequestRepository serviceRequestRepository;
	private final CounterReferenceRepository counterReferenceRepository;
	private final SnvsReportRepository snvsReportRepository;
	private final DocumentRepository documentRepository;
	private final DocumentHealthConditionRepository documentHealthConditionRepository;
	private final DocumentAllergyIntoleranceRepository documentAllergyIntoleranceRepository;
	private final DocumentImmunizationRepository documentImmunizationRepository;
	private final DocumentMedicamentionStatementRepository documentMedicamentionStatementRepository;
	private final DocumentProcedureRepository documentProcedureRepository;
	private final DocumentOdontologyDiagnosticRepository documentOdontologyDiagnosticRepository;
	private final DocumentOdontologyProcedureRepository documentOdontologyProcedureRepository;
	private final DocumentRiskFactorRepository documentRiskFactorRepository;
	private final DocumentLabRepository documentLabRepository;
	private final DocumentDiagnosticReportRepository documentDiagnosticReportRepository;
	private final DocumentIndicationRepository documentIndicationRepository;
	private final DocumentAppointmentRepository documentAppointmentRepository;
	private final MigratePatientStorage migratePatientStorage;

	@Override
	public List<Integer> getInternmentEpisodesIds(List<Integer> oldPatientsIds) {
		log.debug("Input parameters -> oldPatientsIds{}", oldPatientsIds);
		return internmentEpisodeRepository.getInternmentEpisodeIdsFromPatients(oldPatientsIds);
	}

	@Override
	public List<Integer> getOutpatientConsultationIds(List<Integer> oldPatients) {
		return outpatientConsultationRepository.getOutpatientConsultationIdsFromPatients(oldPatients);
	}

	@Override
	public List<Integer> getMedicationRequestIds(List<Integer> oldPatients) {
		return medicationRequestRepository.getMedicatoinRequestIdsFromPatients(oldPatients);
	}

	@Override
	public List<Integer> getServiceRequestIds(List<Integer> oldPatients) {
		return serviceRequestRepository.getServiceRequestIdsFromPatients(oldPatients);
	}


	@Override
	public List<Integer> getCounterReferenceIds(List<Integer> oldPatients) {
		return counterReferenceRepository.getCounterReferenceIdsFromPatients(oldPatients);
	}

	@Override
	public void modifyDocument(List<Long> ids, Integer newPatientId) {
		log.debug("Document ids to modify {}", ids);
		documentRepository.findAllById(ids)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId().intValue(), item.getPatientId(), newPatientId, EMergeTable.DOCUMENT));
	}

	@Override
	public List<Long> getDocumentsIds(List<Integer> ids, List<ESourceType> sourceTypes) {
		log.debug("Input parameters -> ids{}", ids);
		return documentRepository.getIdsBySourceIdType(ids, sourceTypes.stream().map(sts -> sts.getId()).collect(Collectors.toList()));
	}

	@Override
	public void modifyInternmentEpisode(List<Integer> ids, Integer newPatientId) {
		log.debug("Internment episode ids to modify {}", ids);
		internmentEpisodeRepository.findAllById(ids)
				.forEach(item-> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.INTERNMENT_EPISODE));
	}

	@Override
	public void modifyHealthCondition(List<Long> ids, Integer newPatientId) {
		log.debug("Health conditions ids to modify {}", ids);
		documentHealthConditionRepository.getHealthConditionFromDocuments(ids)
				.forEach(item-> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.HEALTH_CONDITION));
	}

	@Override
	public void modifyAllergyIntolerance(List<Long> ids, Integer newPatientId) {
		log.debug("Allergies ids to modify {}", ids);
		documentAllergyIntoleranceRepository.getAllergyIntoleranceFromDocuments(ids)
				.forEach(item-> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.ALLERGY_INTOLERANCE));
	}

	@Override
	public void modifyImmunization(List<Long> ids, Integer newPatientId) {
		log.debug("Immunizations ids to modify {}", ids);
		documentImmunizationRepository.getImmunizationFromDocuments(ids)
				.forEach(item-> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.INMUNIZATION));
	}

	@Override
	public void modifyMedicationStatement(List<Long> ids, Integer newPatientId) {
		log.debug("Medication Statements ids to modify {}", ids);
		documentMedicamentionStatementRepository.getMedicationStatementFromDocuments(ids)
				.forEach(item-> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.MEDICATION_STATEMENT));
	}

	@Override
	public void modifyProcedure(List<Long> ids, Integer newPatientId) {
		log.debug("Procedures ids to modify {}", ids);
		documentProcedureRepository.getProcedureFromDocuments(ids)
				.forEach(item-> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.PROCEDURE));
	}

	@Override
	public void modifyOdontologyDiagnostic(List<Long> ids, Integer newPatientId) {
		log.debug("Odontology Diagnostics ids to modify {}", ids);
		documentOdontologyDiagnosticRepository.getOdontologyDiagnosticFromDocuments(ids)
				.forEach(item-> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.ODONTOLOGY_DIAGNOSTIC));
	}

	@Override
	public void modifyOdontologyProcedure(List<Long> ids, Integer newPatientId) {
		log.debug("Odontology Procedures ids to modify {}", ids);
		documentOdontologyProcedureRepository.getOdontologyProcedureFromDocuments(ids)
				.forEach(item-> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.ODONTOLOGY_PROCEDURE));
	}

	@Override
	public void modifyObservationVitalSign(List<Long> ids, Integer newPatientId) {
		log.debug("Observation Vital Signs ids to modify {}", ids);
		documentRiskFactorRepository.getRiskFactorFromDocuments(ids)
				.forEach(item-> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.OBSERVATION_VITAL_SIGN));
	}

	@Override
	public void modifyObservationLab(List<Long> ids, Integer newPatientId) {
		log.debug("Observation labs ids to modify {}", ids);
		documentLabRepository.getObservationLabFromDocuments(ids)
				.forEach(item-> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.OBSERVATION_LAB));
	}

	@Override
	public void modifyDiagnosticReport(List<Long> ids, Integer newPatientId) {
		log.debug("Diagnostic Reports ids to modify {}", ids);
		documentDiagnosticReportRepository.getDiagnosticReportFromDocuments(ids)
				.forEach(item-> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.DIAGNOSTIC_REPORT));
	}

	@Override
	public void modifyIndication(List<Long> ids, Integer newPatientId) {
		log.debug("Indications ids to modify {}", ids);
		documentIndicationRepository.getIndicationFromDocuments(ids)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.INDICATION));
	}

	@Override
	public void modifyAppointment(List<Long> ids, Integer newPatientId) {
		log.debug("Appointment ids to modify {}", ids);
		documentAppointmentRepository.getAppointmentFromDocuments(ids)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.APPOINTMENT));
	}

	@Override
	public void modifyOutpatientConsultation(List<Integer> ids, Integer newPatientId) {
		log.debug("Outpatient consultation ids to modify {}", ids);
		outpatientConsultationRepository.findAllById(ids)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.OUTPATIENT_CONSULTATION));
	}


	@Override
	public void modifyMedicationRequest(List<Integer> ids, Integer newPatientId) {
		log.debug("Medication request ids to modify {}", ids);
		medicationRequestRepository.findAllById(ids)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.MEDICATION_REQUEST));
	}

	@Override
	public void modifyServiceRequest(List<Integer> ids, Integer newPatientId) {
		log.debug("Service request ids to modify {}", ids);
		serviceRequestRepository.findAllById(ids)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.SERVICE_REQUEST));
	}


	@Override
	public void modifyCounterReference(List<Integer> ids, Integer newPatientId) {
		log.debug("Counter reference ids to modify {}",ids);
		counterReferenceRepository.findAllById(ids)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.COUNTER_REFERENCE));
	}

	@Override
	public void modifySnvsReport(List<Integer> oldPatients, Integer newPatientId) {
		snvsReportRepository.findAllByPatients(oldPatients)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.SNVS_REPORT));
	}


}
