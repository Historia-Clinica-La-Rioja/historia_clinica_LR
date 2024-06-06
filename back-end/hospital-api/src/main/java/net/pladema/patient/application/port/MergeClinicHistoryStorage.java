package net.pladema.patient.application.port;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ESourceType;

import ar.lamansys.sgx.shared.migratable.SGXDocumentEntityRepository;

import java.util.List;

public interface MergeClinicHistoryStorage {

	<T extends SGXDocumentEntityRepository> void migratePatientIdFromItem(Class<T> clazz, List<Long> ids, Integer newPatientId);
	List<Integer> getInternmentEpisodesIds(List<Integer> oldPatientsIds);
	List<Integer> getOutpatientConsultationIds(List<Integer> oldPatientsIds);
	List<Integer> getEmergencyCareEpisodeIds(List<Integer> oldPatientsIds);
	List<Integer> getMedicationRequestIds(List<Integer> oldPatientsIds);
	List<Integer> getServiceRequestIds(List<Integer> oldPatientsIds);
	List<Integer> getNursingConsultationIds(List<Integer> oldPatientsIds);
	List<Integer> getVaccineConsultationIds(List<Integer> oldPatientsIds);
	List<Integer> getCounterReferenceIds(List<Integer> oldPatientsIds);
	List<Integer> getServiceRequestIdsFromIdSourceType(List<Integer> ids, Short sourceType);
	List<Integer> getOdontologyConsultationIds(List<Integer> oldPatientsIds);
	void modifyDocument(List<Long> dIds, Integer newPatientId);
	List<Long> getDocumentsIds(List<Integer> ids, ESourceType sourceType);
	void modifyInternmentEpisode(List<Integer> ieIds, Integer newPatientId);
	void modifyOutpatientConsultation(List<Integer> ocIds, Integer newPatientId);
	void modifyEmergencyCareEpisode(List<Integer> eceIds, Integer newPatientId);
	void modifyMedicationRequest(List<Integer> mrIds, Integer newPatientId);
	void modifyServiceRequest(List<Integer> srIds, Integer newPatientId);
	void modifyNursingConsultation(List<Integer> ncIds, Integer newPatientId);
	void modifyVaccineConsultation(List<Integer> vcIds, Integer newPatientId);
	void modifyCounterReference(List<Integer> crIds, Integer newPatientId);
	void modifyTriageRiskFactor(List<Integer> ids, Integer newPatientId);
	void modifySnvsReport(List<Integer> oldPatientsIds, Integer newPatientId);
	void modifyOdontologyConsultation(List<Integer> ids, Integer newPatientId);
	void modifyAppointment(List<Integer> oldPatientsIds, Integer newPatientId);
	void modifyViolenceReport(List<Integer> oldPatientIds, Integer newPatientId);
	void modifySurgicalReport(List<Integer> oldPatientIds, Integer newPatientId);
	void rebuildDocumentsFiles(List<Long> documentsIds);
	void modifyOdontogram(Integer newPatientId);
	void unmergeClinicData(Integer inactivePatientId);


	}