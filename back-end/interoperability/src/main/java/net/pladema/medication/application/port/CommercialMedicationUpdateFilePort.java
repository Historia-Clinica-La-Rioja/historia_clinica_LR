package net.pladema.medication.application.port;

public interface CommercialMedicationUpdateFilePort {

	Long getLastNonProcessedLogId();

	void saveNewEntry(Long logId);

}
