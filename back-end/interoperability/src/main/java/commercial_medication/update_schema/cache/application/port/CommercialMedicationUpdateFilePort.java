package commercial_medication.update_schema.cache.application.port;

import commercial_medication.update_schema.cache.domain.CommercialMedicationFileUpdateBo;

public interface CommercialMedicationUpdateFilePort {

	boolean schemaAlreadyInitialized();

	void saveNewEntry(Long logId);

	CommercialMedicationFileUpdateBo getLastNonProcessedEntry();

    void setEntryAsProcessed(Integer id, Long lastLogId);

}
