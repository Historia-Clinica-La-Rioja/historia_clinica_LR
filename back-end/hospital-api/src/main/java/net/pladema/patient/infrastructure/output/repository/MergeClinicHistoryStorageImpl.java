package net.pladema.patient.infrastructure.output.repository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SurgicalReportRepository;
import net.pladema.violencereport.infrastructure.output.repository.ViolenceReportRepository;

import org.springframework.stereotype.Service;

import ar.lamansys.refcounterref.infraestructure.output.repository.counterreference.CounterReferenceRepository;
import ar.lamansys.sgh.clinichistory.application.ports.DocumentFileStorage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ESourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.OdontologyDiagnosticRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.OdontologyProcedureRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.OdontologyDiagnostic;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.OdontologyProcedure;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.SharedImmunizationPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.nursing.SharedNursingConsultationPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.odontology.OdontologyDiagnosticProcedureInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.odontology.SharedOdontologyConsultationPort;
import ar.lamansys.sgx.shared.migratable.SGXDocumentEntity;
import ar.lamansys.sgx.shared.migratable.SGXDocumentEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationRepository;
import net.pladema.clinichistory.requests.medicationrequests.repository.MedicationRequestRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.ServiceRequestRepository;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.patient.application.port.MergeClinicHistoryStorage;
import net.pladema.patient.application.port.MigratePatientStorage;
import net.pladema.patient.infrastructure.output.repository.entity.EMergeTable;
import net.pladema.snvs.infrastructure.output.repository.report.SnvsReportRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class MergeClinicHistoryStorageImpl implements MergeClinicHistoryStorage {

	private final InternmentEpisodeRepository internmentEpisodeRepository;
	private final OutpatientConsultationRepository outpatientConsultationRepository;
	private final EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;
	private final AppointmentRepository appointmentRepository;
	private final MedicationRequestRepository medicationRequestRepository;
	private final ServiceRequestRepository serviceRequestRepository;
	private final SharedNursingConsultationPort sharedNursingConsultationPort;
	private final SharedImmunizationPort sharedImmunizationPort;
	private final SharedOdontologyConsultationPort sharedOdontologyConsultationPort;
	private final CounterReferenceRepository counterReferenceRepository;
	private final SnvsReportRepository snvsReportRepository;
	private final DocumentRepository documentRepository;
	private final MigratePatientStorage migratePatientStorage;
	private final DocumentFileStorage documentFileStorage;
	private final SharedDocumentPort sharedDocumentPort;
	private final MigratableRepositoryMap repositoryMap;
	private final OdontologyProcedureRepository odontologyProcedureRepository;
	private final OdontologyDiagnosticRepository odontologyDiagnosticRepository;
	private final ViolenceReportRepository violenceReportRepository;
	private final SurgicalReportRepository surgicalReportRepository;


	@Override
	public <T extends SGXDocumentEntityRepository> void migratePatientIdFromItem(Class<T> clazz, List<Long> ids, Integer newPatientId) {
		List<SGXDocumentEntity> entities = repositoryMap.get(clazz).getEntitiesByDocuments(ids);
		entities.forEach(item-> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.map(item.getClass().getSimpleName())));
	}

	@Override
	public List<Integer> getInternmentEpisodesIds(List<Integer> oldPatientsIds) {
		log.debug("Input parameters -> oldPatientsIds{}", oldPatientsIds);
		return internmentEpisodeRepository.getInternmentEpisodeIdsFromPatients(oldPatientsIds);
	}

	@Override
	public List<Integer> getOutpatientConsultationIds(List<Integer> oldPatientsIds) {
		return outpatientConsultationRepository.getOutpatientConsultationIdsFromPatients(oldPatientsIds);
	}

	@Override
	public List<Integer> getEmergencyCareEpisodeIds(List<Integer> oldPatientsIds) {
		return emergencyCareEpisodeRepository.getEmergencyCareEpisodeIdsFromPatients(oldPatientsIds);
	}

	@Override
	public List<Integer> getMedicationRequestIds(List<Integer> oldPatientsIds) {
		return medicationRequestRepository.getMedicatoinRequestIdsFromPatients(oldPatientsIds);
	}

	@Override
	public List<Integer> getServiceRequestIds(List<Integer> oldPatientsIds) {
		return serviceRequestRepository.getServiceRequestIdsFromPatients(oldPatientsIds);
	}

	@Override
	public List<Integer> getNursingConsultationIds(List<Integer> oldPatientsIds) {
		return sharedNursingConsultationPort.getNursingConsultationIdsFromPatients(oldPatientsIds);
	}
	
	@Override
	public List<Integer> getVaccineConsultationIds(List<Integer> oldPatientsIds) {
		return sharedImmunizationPort.getVaccineConsultationIdsFromPatients(oldPatientsIds);
	}

	@Override
	public List<Integer> getCounterReferenceIds(List<Integer> oldPatientsIds) {
		return counterReferenceRepository.getCounterReferenceIdsFromPatients(oldPatientsIds);
	}

	@Override
	public List<Integer> getServiceRequestIdsFromIdSourceType(List<Integer> ids, Short sourceType) {
		return serviceRequestRepository.getServiceRequestIdsFromIdSourceType(ids,sourceType);
	}

	@Override
	public List<Integer> getOdontologyConsultationIds(List<Integer> oldPatientsIds) {
		return sharedOdontologyConsultationPort.getOdontologyConsultationIdsFromPatients(oldPatientsIds);
	}

	@Override
	public void modifyDocument(List<Long> ids, Integer newPatientId) {
		log.debug("Document ids to modify {}", ids);
		documentRepository.findAllById(ids)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId().intValue(), item.getPatientId(), newPatientId, EMergeTable.DOCUMENT));
	}

	@Override
	public List<Long> getDocumentsIds(List<Integer> ids, ESourceType sourceType) {
		log.debug("Input parameters -> ids{}", ids);
		return documentRepository.getIdsBySourceIdType(ids, sourceType.getId());
	}

	@Override
	public void modifyInternmentEpisode(List<Integer> ids, Integer newPatientId) {
		log.debug("Internment episode ids to modify {}", ids);
		internmentEpisodeRepository.findAllById(ids)
				.forEach(item-> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.INTERNMENT_EPISODE));
	}

	@Override
	public void modifyOutpatientConsultation(List<Integer> ids, Integer newPatientId) {
		log.debug("Outpatient consultation ids to modify {}", ids);
		outpatientConsultationRepository.findAllById(ids)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.OUTPATIENT_CONSULTATION));
	}

	@Override
	public void modifyEmergencyCareEpisode(List<Integer> eceIds, Integer newPatientId) {
		log.debug("Emergency care episode ids to modify {}", eceIds);
		emergencyCareEpisodeRepository.findAllById(eceIds)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.EMERGENCY_CARE_EPISODE));
	}


	@Override
	public void modifyMedicationRequest(List<Integer> ids, Integer newPatientId) {
		log.debug("Medication request ids to modify {}", ids);
		medicationRequestRepository.findAllById(ids)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.MEDICATION_REQUEST));
	}

	@Override
	public void modifyServiceRequest(List<Integer> srIds, Integer newPatientId) {
		log.debug("Service request ids to modify {}", srIds);
		serviceRequestRepository.findAllById(srIds)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.SERVICE_REQUEST));
	}

	@Override
	public void modifyNursingConsultation(List<Integer> ids, Integer newPatientId) {
		log.debug("Nursing consultation ids to modify {}", ids);
		sharedNursingConsultationPort.findAllById(ids)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.NURSING_CONSULTATION));
	}

	@Override
	public void modifyVaccineConsultation(List<Integer> vcIds, Integer newPatientId) {
		log.debug("Vaccine consultation ids to modify {}", vcIds);
		sharedImmunizationPort.findAllVaccineConsultationByIds(vcIds)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.VACCINE_CONSULTATION));
	}

	@Override
	public void modifyCounterReference(List<Integer> ids, Integer newPatientId) {
		log.debug("Counter reference ids to modify {}",ids);
		counterReferenceRepository.findAllById(ids)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.COUNTER_REFERENCE));
	}

	@Override
	public void modifyTriageRiskFactor(List<Integer> ids, Integer newPatientId) {
		emergencyCareEpisodeRepository.getObservationRiskFactorFromEmergencyCareEpisodes(ids)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.OBSERVATION_VITAL_SIGN));
	}

	@Override
	public void modifySnvsReport(List<Integer> oldPatientsIds, Integer newPatientId) {
		snvsReportRepository.findAllByPatients(oldPatientsIds)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.SNVS_REPORT));
	}

	@Override
	public void modifyOdontologyConsultation(List<Integer> ids, Integer newPatientId) {
		log.debug("Odontology consultation ids to modify {}",ids);
		sharedOdontologyConsultationPort.findAllById(ids)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.ODONTOLOGY_CONSULTATION));
	}

	@Override
	public void modifyAppointment(List<Integer> oldPatientsIds, Integer newPatientId) {
		appointmentRepository.getAppointmentsFromPatients(oldPatientsIds)
				.forEach(item -> migratePatientStorage.migrateItem(item.getId(), item.getPatientId(), newPatientId, EMergeTable.APPOINTMENT));
	}

	@Override
	public void modifyViolenceReport(List<Integer> oldPatientIds, Integer newPatientId) {
		log.debug("Input parameters -> oldPatientIds {}, newPatientId {}", oldPatientIds, newPatientId);
		violenceReportRepository.getAllPatientsSituationIds(oldPatientIds)
				.forEach(violenceReport -> migratePatientStorage.migrateItem(violenceReport.getId(), violenceReport.getPatientId(), newPatientId, EMergeTable.VIOLENCE_REPORT));
	}

	@Override
	public void modifySurgicalReport(List<Integer> oldPatientIds, Integer newPatientId) {
		log.debug("Input parameters -> oldPatientIds {}, newPatientId {}", oldPatientIds, newPatientId);
		surgicalReportRepository.getPatientsSurgicalReportIds(oldPatientIds)
				.forEach(surgicalReport -> migratePatientStorage.migrateItem(surgicalReport.getId(), surgicalReport.getPatientId(), newPatientId, EMergeTable.SURGICAL_REPORT));
	}

	@Override
	public void rebuildDocumentsFiles(List<Long> documentsIds) {
		log.debug("Document files ids to modify {}",documentsIds);
		List<Long> documentFileIds = documentFileStorage.getIdsByDocumentsIds(documentsIds);
		documentFileIds.forEach(sharedDocumentPort::rebuildFile);
	}

	@Override
	public void modifyOdontogram(Integer newPatientId) {
		sharedOdontologyConsultationPort.deleteLastOdontogramDrawingFromPatient(newPatientId);
		sharedOdontologyConsultationPort.deleteToothIndicesFromPatient(newPatientId);

		List<OdontologyDiagnostic> od = odontologyDiagnosticRepository.findAllByPatientId(newPatientId);
		List<OdontologyProcedure> op = odontologyProcedureRepository.findAllByPatientId(newPatientId);

		List<OdontologyDiagnosticProcedureInfoDto> odp = od.stream().map(o -> new OdontologyDiagnosticProcedureInfoDto(
				o.getId(),
				o.getPatientId(),
				o.getSnomedId(),
				o.getToothId(),
				o.getSurfaceId(),
				o.getCie10Codes(),
				o.getPerformedDate(),
				o.getNoteId(),
				true
		)).collect(Collectors.toList());

		odp.addAll(op.stream().map(o -> new OdontologyDiagnosticProcedureInfoDto(
				o.getId(),
				o.getPatientId(),
				o.getSnomedId(),
				o.getToothId(),
				o.getSurfaceId(),
				o.getCie10Codes(),
				o.getPerformedDate(),
				o.getNoteId(),
				false
		)).collect(Collectors.toList()));

		odp.sort(Comparator.comparing(OdontologyDiagnosticProcedureInfoDto::getPerformedDate));

		if (!odp.isEmpty())
			sharedOdontologyConsultationPort.modifyLastOdontogramDrawing(odp,newPatientId);

	}

	@Override
	public void unmergeClinicData(Integer inactivePatientId) {
		log.debug("Input parameters -> inactivePatientId {}", inactivePatientId);
		migratePatientStorage.undoMigrateByInactivePatient(inactivePatientId);
	}


}
