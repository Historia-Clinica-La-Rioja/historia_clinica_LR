package net.pladema.patient.application.port;

import java.util.List;

public interface MergeClinicHistoryStorage {

	List<Integer> getInternmentEpisodesIds(List<Integer> oldPatients);
	List<Long> getDocumentsIds(List<Integer> ieIds);
	void modifyInternmentEpisode(List<Integer> ieIds, Integer newPatient);
	void modifyHealthCondition(List<Long> dIds, Integer newPatient);
	void modifyAllergyIntolerance(List<Long> dIds, Integer newPatient);
	void modifyImmunization(List<Long> dIds, Integer newPatient);
	void modifyMedicationStatement(List<Long> dIds, Integer newPatient);
	void modifyProcedure(List<Long> dIds, Integer newPatient);
	void modifyOdontologyDiagnostic(List<Long> dIds, Integer newPatient);
	void modifyOdontologyProcedure(List<Long> dIds, Integer newPatient);
	void modifyObservationVitalSign(List<Long> dIds, Integer newPatient);
	void modifyObservationLab(List<Long> dIds, Integer newPatient);
	void modifyDiagnosticReport(List<Long> dIds, Integer newPatient);
	void modifyIndication(List<Long> dIds, Integer newPatient);

}