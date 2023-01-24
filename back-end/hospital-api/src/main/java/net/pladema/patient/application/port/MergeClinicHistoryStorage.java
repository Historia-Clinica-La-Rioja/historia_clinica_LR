package net.pladema.patient.application.port;

import java.util.List;

public interface MergeClinicHistoryStorage {

	List<Integer> getInternmentEpisodesIds(List<Integer> oldPatients);
	List<Long> getDocumentsIds(List<Integer> ids);
	void modifyInternmentEpisode(List<Integer> ids, Integer newPatient);
	void modifyHealthCondition(List<Long> ids, Integer newPatient);
	void modifyAllergyIntolerance(List<Long> ids, Integer newPatient);
	void modifyImmunization(List<Long> ids, Integer newPatient);
	void modifyMedicationStatement(List<Long> ids, Integer newPatient);
	void modifyProcedure(List<Long> ids, Integer newPatient);
	void modifyOdontologyDiagnostic(List<Long> ids, Integer newPatient);
	void modifyOdontologyProcedure(List<Long> ids, Integer newPatient);
	void modifyObservationVitalSign(List<Long> ids, Integer newPatient);
	void modifyObservationLab(List<Long> ids, Integer newPatient);
	void modifyDiagnosticReport(List<Long> ids, Integer newPatient);
	void modifyIndication(List<Long> ids, Integer newPatient);

}