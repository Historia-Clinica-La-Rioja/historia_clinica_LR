package net.pladema.medication.application.port;

import net.pladema.medication.domain.CommercialMedicationFileUpdateBo;

public interface CommercialMedicationUpdateFilePort {

	boolean schemaAlreadyInitialized();

	void saveNewEntry(Long logId);

	CommercialMedicationFileUpdateBo getLastNonProcessedEntry();

    void setEntryAsProcessed(Integer id, Long lastLogId);

}
